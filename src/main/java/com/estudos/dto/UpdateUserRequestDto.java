package com.estudos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import com.estudos.enums.Cargo;

public record UpdateUserRequestDto(
        @Size(min = 3, max = 50, message = "Nome deve ter entre 3 e 50 caracteres")
        String firstname,

        @Size(min = 3, max = 50, message = "Sobrenome deve ter entre 3 e 50 caracteres")
        String lastname,

        @Email(message = "Email deve ser válido")
        @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
        String email,

        Cargo cargo
) {
}