package com.estudos.dto;

import com.estudos.enums.Cargo;

public record LoginResponseDto(

        String token,
        String username,
        String email,
        String firstName,
        String lastName,
        Cargo cargo
) {

}
