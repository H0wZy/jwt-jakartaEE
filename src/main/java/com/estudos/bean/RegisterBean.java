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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@RequestScoped
public class RegisterBean {
    private static final Logger logger = LoggerFactory.getLogger(RegisterBean.class);

    // ✅ Campos para binding com o formulário XHTML
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

    private boolean buscandoCep = false;
    private boolean cepEncontrado = false;

    // ✅ Injetar SEU AuthService (não fazer HTTP!)
    @Inject
    private IAuthService authService;

    @Inject
    private IViaCepService viaCepService;

    /**
     * Métodos chamados quando o usuário clicar em "Registrar"
     */
    public String register() {
        try {

            if (!password.equals(confirmPassword)) {
                setErrorMessage("As senhas não coincidem");
                return null;
            }

            // ✅ Criar DTO usando seus campos
            RegisterRequestDto request = new RegisterRequestDto(
                    username, email, firstName, lastName,
                    cep, rua, numero, bairro, cidade, uf,
                    cargo, password, confirmPassword);

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

        } catch (Exception ex) {
            setErrorMessage("Erro interno. Tente novamente.");
            logger.error("Erro interno. Tente novamente, {}", ex.getMessage(), ex);
        }

        return null;
    }

    public void buscarCep() {
        try {
            setBuscandoCep(true);
            setCepEncontrado(false);
            limparCamposEndereco();

            if (cep == null || cep.trim().isEmpty()) {
                return;
            }

            ApiResponseDto<ViaCepResponseDto> response = viaCepService.buscarEnderecoPorCep(cep);

            if (response.success() && response.data() != null) {
                ViaCepResponseDto endereco = response.data();

                this.rua = endereco.logradouro();
                this.bairro = endereco.bairro();
                this.cidade = endereco.localidade();
                this.uf = endereco.uf();

                setCepEncontrado(true);
                setSuccessMessage("Endereço encontrado com sucesso!");
            } else {
                setErrorMessage(response.message());
                limparCamposEndereco();
            }
        } catch (Exception ex) {
            setErrorMessage("Erro ao buscar CEP. Tente novamente.");
            logger.error("Erro ao buscar CEP. Tente novamente, {}", ex.getMessage(), ex);
        } finally {
            setBuscandoCep(false);
        }
    }

    private void limparCamposEndereco() {
        this.rua = null;
        this.bairro = null;
        this.cidade = null;
        this.uf = null;
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
        cepEncontrado = false;
        buscandoCep = false;
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