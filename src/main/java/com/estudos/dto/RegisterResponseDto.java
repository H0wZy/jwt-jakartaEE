package com.estudos.dto;

import com.estudos.enums.Cargo;

public record RegisterResponseDto(
        Long id,
        String username,
        String email,
        String firstname,
        String lastname,
        Cargo cargo
) {
}
