package com.estudos.controller;

import com.estudos.dto.*;
import com.estudos.service.AuthService.IAuthService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    private IAuthService authService;

    /**
     * Endpoint de registro
     * POST /api/auth/register
     */
    @POST
    @Path("/register")
    public Response register(@Valid RegisterRequestDto registerRequestDto) {
        try {
            ApiResponseDto<RegisterResponseDto> result = authService.register(registerRequestDto);

            if (result.success()) {
                return Response.status(201).entity(result).build(); // 201 Created
            } else {
                return Response.status(result.statusCode()).entity(result).build();
            }

        } catch (Exception e) {
            ApiResponseDto<RegisterResponseDto> errorResponse = ApiResponseDto.error(
                    "Erro interno do servidor", 500
            );
            return Response.status(500).entity(errorResponse).build();
        }
    }

    /**
     * Endpoint de login
     * POST /api/auth/login
     */
    @POST
    @Path("/login")
    public Response login(@Valid LoginRequestDto loginRequestDto) {
        try {
            ApiResponseDto<LoginResponseDto> result = authService.login(loginRequestDto);

            // Retorna status HTTP baseado no resultado
            if (result.success()) {
                return Response.ok(result).build();
            } else {
                return Response.status(result.statusCode()).entity(result).build();
            }

        } catch (Exception e) {
            ApiResponseDto<LoginResponseDto> errorResponse = ApiResponseDto.error(
                    "Erro interno do servidor", 500
            );
            return Response.status(500).entity(errorResponse).build();
        }
    }

    /**
     * Endpoint de logout
     * POST /api/auth/logout
     */
    @POST
    @Path("/logout")
    public Response logout(@HeaderParam("Authorization") String authHeader) {
        try {
            // Extrair token do header "Bearer TOKEN"
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                ApiResponseDto<Void> errorResponse = ApiResponseDto.error(
                        "Token não fornecido", 401
                );
                return Response.status(401).entity(errorResponse).build();
            }

            String token = authHeader.substring(7); // Remove "Bearer "
            ApiResponseDto<Void> result = authService.logout(token);

            return Response.ok(result).build();

        } catch (Exception e) {
            ApiResponseDto<Void> errorResponse = ApiResponseDto.error(
                    "Erro interno do servidor", 500
            );
            return Response.status(500).entity(errorResponse).build();
        }
    }

    /**
     * Endpoint para validar token
     * GET /api/auth/validate
     */
    @GET
    @Path("/validate")
    public Response validateToken(@HeaderParam("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                ApiResponseDto<Boolean> errorResponse = ApiResponseDto.error(
                        "Token não fornecido", 401
                );
                return Response.status(401).entity(errorResponse).build();
            }

            String token = authHeader.substring(7);
            boolean isValid = authService.validateToken(token);

            if (isValid) {
                ApiResponseDto<Boolean> successResponse = ApiResponseDto.success(
                        true, "Token válido"
                );
                return Response.ok(successResponse).build();
            } else {
                ApiResponseDto<Boolean> errorResponse = ApiResponseDto.error(
                        "Token inválido ou expirado", 401
                );
                return Response.status(401).entity(errorResponse).build();
            }

        } catch (Exception e) {
            ApiResponseDto<Boolean> errorResponse = ApiResponseDto.error(
                    "Erro interno do servidor", 500
            );
            return Response.status(500).entity(errorResponse).build();
        }
    }
}