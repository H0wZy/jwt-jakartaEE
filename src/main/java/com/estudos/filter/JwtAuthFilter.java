package com.estudos.filter;

import com.estudos.util.JwtHelper;
import jakarta.inject.Inject;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*") // Intercepta todas as URLs
public class JwtAuthFilter extends HttpFilter {

    @Inject
    private JwtHelper jwtHelper;

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        // ✅ Pular autenticação para endpoints públicos
        if (isPublicEndpoint(requestURI, method)) {
            chain.doFilter(request, response);
            return;
        }

        // Extrair token do header
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendErrorResponse(response, "Token não fornecido");
            return;
        }

        String token = authHeader.substring(7);

        // Validar token
        if (!jwtHelper.validateToken(token) || jwtHelper.isTokenExpired(token)) {
            sendErrorResponse(response, "Token inválido ou expirado");
            return;
        }

        // Adicionar informações do usuário ao request
        String username = jwtHelper.getUsernameFromToken(token);
        String cargo = jwtHelper.getCargoFromToken(token);
        String email = jwtHelper.getEmailFromToken(token);
        Long userId = jwtHelper.getUserIdFromToken(token);

        request.setAttribute("username", username);
        request.setAttribute("cargo", cargo);
        request.setAttribute("email", email);
        request.setAttribute("userId", userId);

        // Continuar com a requisição
        chain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String requestURI, String method) {
        // ✅ SEMPRE permitir OPTIONS (CORS preflight)
        if ("OPTIONS".equals(method)) {
            return true;
        }

        // 🔓 APIs REST públicas
        if (requestURI.contains("/api/auth/login") ||
                requestURI.contains("/api/auth/register")) {
            return true;
        }

        // 🔓 Páginas JSF públicas (apenas GET)
        if ("GET".equals(method)) {
            if (requestURI.endsWith("/") ||                          // Página inicial
                    requestURI.endsWith("/index.xhtml") ||               // Registro
                    requestURI.endsWith("/register.xhtml") ||               // Registro
                    requestURI.endsWith("/login.xhtml") ||               // Login
                    requestURI.contains("/faces/index.xhtml") ||         // Com /faces/
                    requestURI.contains("/faces/register.xhtml") ||         // Com /faces/
                    requestURI.contains("/faces/login.xhtml")) {         // Com /faces/
                return true;
            }
        }

        // 🔓 Recursos estáticos (apenas GET)
        if ("GET".equals(method)) {
            if (requestURI.endsWith(".css") ||
                    requestURI.endsWith(".js") ||
                    requestURI.endsWith(".png") ||
                    requestURI.endsWith(".jpg") ||
                    requestURI.endsWith(".jpeg") ||
                    requestURI.endsWith(".gif") ||
                    requestURI.endsWith(".ico") ||
                    requestURI.endsWith(".woff") ||
                    requestURI.endsWith(".woff2") ||
                    requestURI.endsWith(".ttf") ||
                    requestURI.endsWith(".map")) {
                return true;
            }
        }

        // 🔓 Recursos do JSF/PrimeFaces (GET)
        if ("GET".equals(method)) {
            if (requestURI.contains("/jakarta.faces.resource/") ||
                    requestURI.contains("/javax.faces.resource/") ||
                    requestURI.contains("/primefaces_resource/")) {
                return true;
            }
        }

        // 🔓 Documentação (GET)
        if ("GET".equals(method)) {
            if (requestURI.contains("/api-docs") ||
                    requestURI.contains("/swagger")) {
                return true;
            }
        }

        // 🔓 POST para formulários de registro/login JSF
        if ("POST".equals(method)) {
            return requestURI.endsWith("/index.xhtml") ||
                    requestURI.endsWith("/register.xhtml") ||
                    requestURI.endsWith("/login.xhtml") ||
                    requestURI.contains("/faces/index.xhtml") ||
                    requestURI.contains("/faces/register.xhtml") ||
                    requestURI.contains("/faces/login.xhtml");
        }

        // 🔒 Todas as outras combinações precisam de autenticação
        return false;
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(401);
        response.setContentType("application/json");
        response.getWriter().write("{\"success\":false,\"message\":\"" + message + "\",\"statusCode\":401}");
    }
}