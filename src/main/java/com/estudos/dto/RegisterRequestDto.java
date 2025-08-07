package com.estudos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(
        @NotBlank(message = "Nome de usuário é obrigatório")
        @Size(min = 3, max = 50, message = "Username deve ter entre 3 e 50 caracteres")
        String username,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ser válido")
        @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
        String email,

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 50, message = "Nome deve ter entre 3 e 50 caracteres")
        String firstName,

        @NotBlank(message = "Sobrenome é obrigatório")
        @Size(min = 3, max = 50, message = "Sobrenome deve ter entre 3 e 50 caracteres")
        String lastName,

        @NotBlank(message = "Senha é obrigatório")
        @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
        String password,

        @NotBlank(message = "É obrigatório confirmar sua senha")
        @Size(min = 8, message = "A senha confirmada deve ter no mínimo 8 caracteres")
        String confirmPassword

) {
}
