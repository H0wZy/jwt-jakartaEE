package com.estudos.bean;

import com.estudos.dto.RegisterRequestDto;
import com.estudos.dto.RegisterResponseDto;
import com.estudos.dto.ApiResponseDto;
import com.estudos.dto.ViaCepResponseDto;
import com.estudos.enums.Cargo;
import com.estudos.service.AuthService.IAuthService;
import com.estudos.service.ViaCepService.IViaCepService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.regex.Pattern;

@Named
@RequestScoped
public class RegisterBean {

    // Campos para binding
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String cep;
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String uf;
    private Cargo cargo;
    private String password;
    private String confirmPassword;
    private String message;
    private boolean success;
    private boolean showMessage;

    // Estados de validação
    private boolean buscandoCep = false;
    private boolean cepEncontrado = false;
    private boolean usernameChecked = false;
    private boolean emailChecked = false;

    // Padrões de validação
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,50}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-ZÀ-ÿ\\s]{2,50}$");
    private static final Pattern CEP_PATTERN = Pattern.compile("^\\d{5}-?\\d{3}$");

    @Inject
    private IAuthService authService;

    @Inject
    private IViaCepService viaCepService;

    // MÉTODOS DE VALIDAÇÃO

    public boolean isUsernameValid() {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }

    public boolean isUsernameInvalid() {
        return usernameChecked && username != null && !username.trim().isEmpty() && !isUsernameValid();
    }

    public boolean isEmailValid() {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean isEmailInvalid() {
        return emailChecked && email != null && !email.trim().isEmpty() && !isEmailValid();
    }

    public boolean isFirstNameValid() {
        return firstName != null && NAME_PATTERN.matcher(firstName).matches();
    }

    public boolean isFirstNameInvalid() {
        return firstName != null && !firstName.trim().isEmpty() && !isFirstNameValid();
    }

    public boolean isLastNameValid() {
        return lastName != null && NAME_PATTERN.matcher(lastName).matches();
    }

    public boolean isLastNameInvalid() {
        return lastName != null && !lastName.trim().isEmpty() && !isLastNameValid();
    }

    public boolean isCepValid() {
        return cep != null && CEP_PATTERN.matcher(cep).matches();
    }

    public boolean isCepInvalid() {
        return cep != null && !cep.trim().isEmpty() && !isCepValid();
    }

    public boolean isPasswordValid() {
        return password != null && password.length() >= 8;
    }

    public boolean isPasswordInvalid() {
        return password != null && !password.trim().isEmpty() && !isPasswordValid();
    }

    public boolean isConfirmPasswordValid() {
        return confirmPassword != null && password != null &&
                confirmPassword.equals(password);
    }

    public boolean isConfirmPasswordInvalid() {
        // Mostra erro se confirmPassword foi preenchida E é diferente da senha
        return confirmPassword != null && !confirmPassword.trim().isEmpty() &&
                (password == null || !confirmPassword.equals(password));
    }

    public boolean isCargoValid() {
        return cargo != null;
    }

    public boolean isCargoInvalid() {
        return false; // Dropdown sempre válido se selecionado
    }

    // MÉTODOS AJAX PARA VALIDAÇÃO EM TEMPO REAL

    public void validateUsername() {
        usernameChecked = true;
        System.out.println("Validando username: " + username + " - Válido: " + isUsernameValid());
    }

    public void validateEmail() {
        emailChecked = true;
        System.out.println("Validando email: " + email + " - Válido: " + isEmailValid());
    }

    public void buscarCep() {
        try {
            System.out.println("Buscando CEP: " + cep);

            setBuscandoCep(true);
            setCepEncontrado(false);
            setShowMessage(false);
            limparCamposEndereco();

            if (cep == null || cep.trim().isEmpty()) {
                setBuscandoCep(false);
                return;
            }

            if (!isCepValid()) {
                setBuscandoCep(false);
                return;
            }

            // Pequeno delay para mostrar o loading
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            ApiResponseDto<ViaCepResponseDto> result = viaCepService.buscarEnderecoPorCep(cep);

            if (result.success() && result.data() != null) {
                ViaCepResponseDto endereco = result.data();

                this.rua = endereco.logradouro();
                this.bairro = endereco.bairro();
                this.cidade = endereco.localidade();
                this.uf = endereco.uf();

                setCepEncontrado(true);
                System.out.println("Endereço encontrado: " + endereco.localidade());

            } else {
                System.out.println("Erro: " + result.message());
                limparCamposEndereco();
            }

        } catch (Exception e) {
            System.out.println("Exceção: " + e.getMessage());
            e.printStackTrace();

        } finally {
            setBuscandoCep(false);
        }
    }

    public String register() {
        try {
            if (!password.equals(confirmPassword)) {
                setErrorMessage("As senhas não coincidem");
                return null;
            }

            RegisterRequestDto request = new RegisterRequestDto(
                    username, email, firstName, lastName,
                    cep, rua, numero, bairro, cidade, uf,
                    cargo, password, confirmPassword
            );

            ApiResponseDto<RegisterResponseDto> result = authService.register(request);

            if (result.success()) {
                setSuccessMessage(result.message());
                clearForm();
                return "login?faces-redirect=true";
            } else {
                setErrorMessage(result.message());
            }

        } catch (Exception e) {
            setErrorMessage("Erro interno. Tente novamente.");
            e.printStackTrace();
        }

        return null;
    }

    // MÉTODOS AUXILIARES

    private void limparCamposEndereco() {
        this.rua = null;
        this.bairro = null;
        this.cidade = null;
        this.uf = null;
    }

    private void clearForm() {
        username = null;
        email = null;
        firstName = null;
        lastName = null;
        cep = null;
        rua = null;
        numero = null;
        bairro = null;
        cidade = null;
        uf = null;
        cargo = null;
        password = null;
        confirmPassword = null;
        cepEncontrado = false;
        buscandoCep = false;
        usernameChecked = false;
        emailChecked = false;
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

    // GETTERS E SETTERS

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

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public boolean isBuscandoCep() {
        return buscandoCep;
    }

    public void setBuscandoCep(boolean buscandoCep) {
        this.buscandoCep = buscandoCep;
    }

    public boolean isCepEncontrado() {
        return cepEncontrado;
    }

    public void setCepEncontrado(boolean cepEncontrado) {
        this.cepEncontrado = cepEncontrado;
    }

    public boolean isUsernameChecked() {
        return usernameChecked;
    }

    public void setUsernameChecked(boolean usernameChecked) {
        this.usernameChecked = usernameChecked;
    }

    public boolean isEmailChecked() {
        return emailChecked;
    }

    public void setEmailChecked(boolean emailChecked) {
        this.emailChecked = emailChecked;
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