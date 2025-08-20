package com.estudos.service.ViaCepService;

import com.estudos.dto.ApiResponseDto;
import com.estudos.dto.ViaCepResponseDto;

public interface IViaCepService {
    ApiResponseDto<ViaCepResponseDto> buscarEnderecoPorCep(String cep);

    boolean validarCep(String cep);

    String limparCep(String cep);
}
