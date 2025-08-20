package com.estudos.service.AuthService;

import com.estudos.bean.RegisterBean;
import com.estudos.dto.*;
import com.estudos.entity.User;
import com.estudos.repository.UserRepository.IUserRepository;
import com.estudos.service.UserService.IUserService;
import com.estudos.util.JwtHelper;
import com.estudos.util.PasswordHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Optional;

@ApplicationScoped
public class AuthService implements IAuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Inject
    private IUserService userService;

    @Inject
    private IUserRepository userRepository;

    @Inject
    private JwtHelper jwtHelper;

    @Override
    public ApiResponseDto<RegisterResponseDto> register(RegisterRequestDto registerRequestDto) {
        try {
            // 1. Verificar se email existe
            if (userRepository.existsByUsername(registerRequestDto.username())) {
                return ApiResponseDto.error("Este nome de usuário já está em uso", 409);
            }
            // 2. Verificar se username existe
            if (userRepository.existsByEmail(registerRequestDto.email())) {
                return ApiResponseDto.error("Este email já está em uso", 409);
            }

            User newUser = buildUserFromDto(registerRequestDto);

            // 4. Salvar usuário
            User savedUser = userService.createUser(newUser, registerRequestDto.password());

            // 5. Criar resposta
            RegisterResponseDto responseDto = new RegisterResponseDto(
                    savedUser.getId(),
                    savedUser.getUsername(),
                    savedUser.getEmail(),
                    savedUser.getFirstName(),
                    savedUser.getLastName(),
                    savedUser.getCargo()
            );

            return ApiResponseDto.success(responseDto, "Usuário criado com sucesso!");

        } catch (Exception e) {
            return ApiResponseDto.error("Erro interno do servidor", 500);
        }
    }

    private User buildUserFromDto(RegisterRequestDto registerRequestDto) {
        User newUser = new User();
        newUser.setUsername(registerRequestDto.username());
        newUser.setEmail(registerRequestDto.email());
        newUser.setFirstName(registerRequestDto.firstName());
        newUser.setLastName(registerRequestDto.lastName());
        newUser.setCargo(registerRequestDto.cargo());
        newUser.setCep(limparCep(registerRequestDto.cep()));
        newUser.setRua(registerRequestDto.rua());
        newUser.setNumero(registerRequestDto.numero());
        newUser.setBairro(registerRequestDto.bairro());
        newUser.setCidade(registerRequestDto.cidade());
        newUser.setUf(registerRequestDto.uf().toUpperCase());
        return newUser;
    }

    @Override
    public ApiResponseDto<LoginResponseDto> login(LoginRequestDto loginRequestDto) {
        try {
            // ✅ Usando records corretamente
            Optional<User> userOpt = userRepository.findByUsernameOrEmail(
                    loginRequestDto.usernameOrEmail()
            );

            if (userOpt.isEmpty()) {
                return ApiResponseDto.error("Credenciais inválidas", 401);
            }

            User user = userOpt.get();

            if (user.isUserDisabled()) {
                return ApiResponseDto.error("Usuário desativado", 403);
            }

            // ✅ Usando records corretamente
            boolean isPasswordValid = PasswordHelper.verifyPassword(
                    loginRequestDto.password(),
                    user.getSaltPassword(),
                    user.getHashPassword()
            );

            if (!isPasswordValid) {
                return ApiResponseDto.error("Credenciais inválidas", 401);
            }

            user.setLastLoginAt(new Date());
            userService.updateLastLogin(user.getId());

            String token = jwtHelper.generateToken(
                    user.getUsername(),
                    user.getEmail(),
                    user.getCargo().toString(),
                    user.getId()
            );

            LoginResponseDto loginResponseDto = new LoginResponseDto(
                    token,
                    user.getUsername(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getCargo()
            );

            return ApiResponseDto.success(
                    loginResponseDto,
                    "Login realizado com sucesso!"
            );

        } catch (Exception e) {
            logger.error("Erro ao realizar login: {}", e.getMessage(), e);
            return ApiResponseDto.error("Erro interno do servidor", 500);
        }
    }

    @Override
    public ApiResponseDto<Void> logout(String token) {
        return ApiResponseDto.success(null, "Logout realizado com sucesso");
    }

    @Override
    public boolean validateToken(String token) {
        return jwtHelper.validateToken(token) && !jwtHelper.isTokenExpired(token);
    }

    private String limparCep(String cep) {
        if (cep == null) {
            return "";
        }
        return cep.replaceAll("\\D", "");
    }
}