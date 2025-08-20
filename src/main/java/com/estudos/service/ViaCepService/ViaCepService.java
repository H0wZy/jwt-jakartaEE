package com.estudos.service.ViaCepService;

import com.estudos.dto.ApiResponseDto;
import com.estudos.dto.ViaCepResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

@ApplicationScoped
public class ViaCepService implements IViaCepService {

    private static final String VIACEP_URL = "https://viacep.com.br/ws/";
    private static final Pattern CEP_PATTERN = Pattern.compile("^\\d{8}$");
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(ViaCepService.class);

    @Override
    public ApiResponseDto<ViaCepResponseDto> buscarEnderecoPorCep(String cep) {
        try {
            if (cep == null || cep.trim().isEmpty()) {
                return ApiResponseDto.error("CEP é obrigatório.", 400);
            }

            String cepLimpo = limparCep(cep);
            if (!validarCep(cepLimpo)) {
                return ApiResponseDto.error("CEP deve conter exatamente 8 dígitos.", 400);
            }

            Client client = ClientBuilder.newBuilder().connectTimeout(5, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).build();

            try {
                String url = String.format("%s%s/json/", VIACEP_URL, cepLimpo);

                Response response = client.target(url).request(MediaType.APPLICATION_JSON).get();

                if (response.getStatus() != 200) {
                    return ApiResponseDto.error("Erro ao consultar ViaCEP", response.getStatus());
                }

                String jsonResponse = response.readEntity(String.class);
                ViaCepResponseDto viaCepResponse = objectMapper.readValue(jsonResponse, ViaCepResponseDto.class);

                if (viaCepResponse.hasError()) {
                    return ApiResponseDto.error("CEP não encontrado.", 404);
                }

                if (!viaCepResponse.isValid()) {
                    return ApiResponseDto.error("CEP encontrado, mas alguns dados estão incompletos.", 422);
                }

                return ApiResponseDto.success(viaCepResponse, "Endereço encontrado com sucesso!");

            } finally {
                client.close();
            }
        } catch (Exception ex) {
            logger.error("Erro ao buscar CEP {}: {}", cep, ex.getMessage(), ex);
            return ApiResponseDto.error("Erro interno ao consultar CEP ", 500);
        }
    }

    @Override
    public boolean validarCep(String cep) {
        if (cep == null) {
            return false;
        }
        return CEP_PATTERN.matcher(cep).matches();
    }

    @Override
    public String limparCep(String cep) {
        if (cep == null) {
            return "";
        }
        return cep.replaceAll("\\D", "");
    }
}
