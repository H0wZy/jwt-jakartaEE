package com.estudos.bean;

import com.estudos.dto.RegisterRequestDto;
import com.estudos.dto.RegisterResponseDto;
import com.estudos.dto.ApiResponseDto;
import com.estudos.enums.Cargo;
import com.estudos.service.AuthService.IAuthService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class RegisterBean {

    // ✅ Campos para binding com o formulário XHTML
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String cep;
    private Cargo cargo;
    private String password;
    private String confirmPassword;
    private String message;
    private boolean success;
    private boolean showMessage;

    // ✅ Injetar SEU AuthService (não fazer HTTP!)
    @Inject
    private IAuthService authService;

    /**
     * Métodos chamados quando o usuário clicar em "Registrar"
     */
    public String register() {
        try {
            // ✅ Criar DTO usando seus campos
            RegisterRequestDto request = new RegisterRequestDto(username, email, firstName, lastName, cep, cargo, password, confirmPassword);

            // ✅ Chamar SEU AuthService.register() - ele já faz tudo!
            ApiResponseDto<RegisterResponseDto> result = authService.register(request);

            // ✅ Processar resultado para a UI
            if (result.success()) {
                setSuccessMessage(result.message());
                clearForm();
                return "login?faces-redirect=true";
            } else {
                setErrorMessage(result.message());
            }

        } catch (Exception e) {
            setErrorMessage("Erro interno. Tente novamente.");
            e.printStackTrace(); // Para debug
        }

        return null;
    }

    // ✅ Métodos auxiliares para controle da UI
    private void clearForm() {
        username = null;
        email = null;
        firstName = null;
        lastName = null;
        cep = null;
        cargo = null;
        password = null;
        confirmPassword = null;
    }

    private void setSuccessMessage(String msg) {
        this.message = msg;
        this.success = true;
        this.showMessage = true;
    }

    private void setErrorMessage(String msg) {
        this.message = msg;
        this.success = false;
        this.showMessage = true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isShowMessage() {
        return showMessage;
    }

    public void setShowMessage(boolean showMessage) {
        this.showMessage = showMessage;
    }
}