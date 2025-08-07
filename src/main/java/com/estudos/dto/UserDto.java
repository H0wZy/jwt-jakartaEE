package com.estudos.dto;

import com.estudos.enums.Cargo;

public record UserDto(
        Long id,
        String username,
        String email,
        String firstName,
        String lastName,
        Cargo cargo,
        boolean active
) {
}