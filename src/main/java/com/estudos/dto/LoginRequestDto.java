package com.estudos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
        @NotBlank(message = "Nome de usuário ou email é obrigatório")
        @Size(min = 3, max = 100, message = "Nome de usuário ou email deve ter entre 3 e 100 caracteres")
        String usernameOrEmail,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
        String password
) {

}
