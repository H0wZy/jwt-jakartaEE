package com.estudos.controller;

import com.estudos.dto.ApiResponseDto;
import com.estudos.dto.ViaCepResponseDto;
import com.estudos.service.ViaCepService.IViaCepService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;

@Path("/cep")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ViaCepController {
    @Inject
    private IViaCepService viaCepService;

    @GET
    @Path("/{cep}")
    public Response buscarEnderecoPorCep(@PathParam("cep") String cep) {
        try {
            ApiResponseDto<ViaCepResponseDto> response = viaCepService.buscarEnderecoPorCep(cep);

            if (response.success()) {
                return Response.ok(response).build();
            } else {
                return Response.status(response.statusCode()).entity(response).build();
            }
        } catch (Exception e) {
            ApiResponseDto<ViaCepResponseDto> errorResponse = ApiResponseDto.error(
                    "Erro interno do servidor", 500
            );
            return Response.status(500).entity(errorResponse).build();
        }
    }

    @GET
    @Path("/validar/{cep}")
    public Response validarCep(@PathParam("cep") String cep) {
        try {
            boolean valido = viaCepService.validarCep(viaCepService.limparCep(cep));

            ApiResponseDto<Boolean> response = valido ? ApiResponseDto.success(true, "CEP válido!") : ApiResponseDto.error("Formato de CEP inválido.", 400);

            return Response.ok(response).build();
        } catch (Exception e) {
            ApiResponseDto<Boolean> errorResponse = ApiResponseDto.error(
                    "Erro interno do servidor", 500
            );
            return Response.status(500).entity(errorResponse).build();
        }
    }
}
