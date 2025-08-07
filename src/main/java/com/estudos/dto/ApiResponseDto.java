package com.estudos.dto;

public record ApiResponseDto<T>(
        T data,
        String message,
        boolean success,
        int statusCode
) {
    // Construtor compacto para validação
    public ApiResponseDto {
        if (statusCode <= 0) {
            statusCode = success ? 200 : 400;
        }
    }

    // Construtor alternativo (sem statusCode)
    public ApiResponseDto(T data, String message, boolean success) {
        this(data, message, success, success ? 200 : 400);
    }

    // ✅ Métodos factory estáticos funcionam perfeitamente em Records!
    public static <T> ApiResponseDto<T> success(T data, String message) {
        return new ApiResponseDto<>(data, message, true, 200);
    }

    public static <T> ApiResponseDto<T> success(T data, String message, int statusCode) {
        return new ApiResponseDto<>(data, message, true, statusCode);
    }

    public static <T> ApiResponseDto<T> error(String message) {
        return new ApiResponseDto<>(null, message, false, 400);
    }

    public static <T> ApiResponseDto<T> error(String message, int statusCode) {
        return new ApiResponseDto<>(null, message, false, statusCode);
    }

    // Méthod de conveniência
    public static <T> ApiResponseDto<T> created(T data, String message) {
        return new ApiResponseDto<>(data, message, true, 201);
    }
}