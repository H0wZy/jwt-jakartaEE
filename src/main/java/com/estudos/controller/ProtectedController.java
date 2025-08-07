package com.estudos.controller;

import com.estudos.dto.ApiResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

@Path("/protected")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProtectedController {

    /**
     * Endpoint protegido - só funciona com token válido
     * GET /api/protected/profile
     */
    @GET
    @Path("/profile")
    public Response getProfile(@Context HttpServletRequest request) {
        try {
            // Pegar informações do usuário do request (colocadas pelo filtro)
            String username = (String) request.getAttribute("username");
            String email = (String) request.getAttribute("email");
            String cargo = (String) request.getAttribute("cargo");
            Long userId = (Long) request.getAttribute("userId");

            // Criar resposta com dados do usuário
            Map<String, Object> userProfile = new HashMap<>();
            userProfile.put("id", userId);
            userProfile.put("username", username);
            userProfile.put("email", email);
            userProfile.put("cargo", cargo);
            userProfile.put("message", "Acesso autorizado! Dados do token JWT:");

            ApiResponseDto<Map<String, Object>> response = ApiResponseDto.success(
                    userProfile,
                    "Profile recuperado com sucesso!"
            );

            return Response.ok(response).build();

        } catch (Exception e) {
            ApiResponseDto<String> errorResponse = ApiResponseDto.error(
                    "Erro interno do servidor", 500
            );
            return Response.status(500).entity(errorResponse).build();
        }
    }

    /**
     * Endpoint só para ADMIN
     * GET /api/protected/admin
     */
    @GET
    @Path("/admin")
    public Response adminOnly(@Context HttpServletRequest request) {
        try {
            String cargo = (String) request.getAttribute("cargo");
            String username = (String) request.getAttribute("username");

            // Verificar se é ADMIN
            if (!"ADMIN".equals(cargo)) {
                ApiResponseDto<String> errorResponse = ApiResponseDto.error(
                        "Acesso negado! Apenas ADMIN pode acessar", 403
                );
                return Response.status(403).entity(errorResponse).build();
            }

            Map<String, Object> adminData = new HashMap<>();
            adminData.put("message", "Bem-vindo à área ADMIN!");
            adminData.put("user", username);
            adminData.put("permissions", "FULL_ACCESS");

            ApiResponseDto<Map<String, Object>> response = ApiResponseDto.success(
                    adminData,
                    "Acesso ADMIN autorizado!"
            );

            return Response.ok(response).build();

        } catch (Exception e) {
            ApiResponseDto<String> errorResponse = ApiResponseDto.error(
                    "Erro interno do servidor", 500
            );
            return Response.status(500).entity(errorResponse).build();
        }
    }
}