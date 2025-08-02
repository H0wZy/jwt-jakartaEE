package com.estudos.controller;

import com.estudos.entity.User;
import com.estudos.enums.Cargo;
import com.estudos.service.UserService.IUserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("testController")
@RequestScoped
public class TestController {

    @Inject
    private IUserService userService;

    // Campos para o formulário
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String message;

    public void createTestUser() {
        try {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setFirstName(firstName != null ? firstName : "Teste");
            user.setLastName(lastName != null ? lastName : "Usuario");
            user.setCargo(Cargo.ADMIN);

            User savedUser = userService.createUser(user, password);
            message = "Usuário criado com sucesso! ID: " + savedUser.getId();

            // Limpar campos
            clearFields();

        } catch (Exception e) {
            message = "Erro: " + e.getMessage();
        }
    }

    private void clearFields() {
        username = null;
        email = null;
        firstName = null;
        lastName = null;
        password = null;
    }

    // Getters e Setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}