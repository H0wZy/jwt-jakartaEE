package com.estudos.dto;

import com.estudos.enums.Cargo;
import jakarta.validation.constraints.*;

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

        @NotBlank(message = "CEP é obrigatório")
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP deve estar no formato 00000-000 ou 00000000")
        String cep,

        @NotBlank(message = "Rua é obrigatória")
        @Size(max = 200, message = "Rua deve ter no máximo 200 caracteres")
        String rua,

        @NotBlank(message = "Número é obrigatório")
        @Size(max = 10, message = "Número deve ter no máximo 10 caracteres")
        String numero,

        @NotBlank(message = "Bairro é obrigatório")
        @Size(max = 100, message = "Bairro deve ter no máximo 100 caracteres")
        String bairro,

        @NotBlank(message = "Cidade é obrigatória")
        @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
        String cidade,

        @NotBlank(message = "UF é obrigatória")
        @Size(min = 2, max = 2, message = "UF deve ter exatamente 2 caracteres")
        String uf,

        /* Enum deve ser @NotNull (não use @NotBlank em tipos não-String) */
        @NotNull(message = "Cargo é obrigatório")
        Cargo cargo,

        @NotBlank(message = "Senha é obrigatório")
        @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
        String password,

        @NotBlank(message = "É obrigatório confirmar sua senha")
        @Size(min = 8, message = "A senha confirmada deve ter no mínimo 8 caracteres")
        String confirmPassword

) {
    public String getEnderecoCompleto() {
        return String.format("%s, %s - %s, %s - %s", rua, numero, bairro, cidade, uf);
    }

    public boolean passwordMatch() {
        return password != null && password.equals(confirmPassword);
    }
}
