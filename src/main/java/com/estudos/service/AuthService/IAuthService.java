package com.estudos.service.AuthService;

import com.estudos.dto.*;

public interface IAuthService {
    ApiResponseDto<RegisterResponseDto> register(RegisterRequestDto registerRequestDto);

    ApiResponseDto<LoginResponseDto> login(LoginRequestDto loginRequestDto);

    ApiResponseDto<Void> logout(String token);

    boolean validateToken(String token);
}
