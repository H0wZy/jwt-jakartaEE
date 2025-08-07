package com.estudos.service.UserService;

import com.estudos.entity.User;
import com.estudos.repository.UserRepository.IUserRepository;
import com.estudos.util.PasswordHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserService implements IUserService {

    @Inject
    private IUserRepository userRepository;

    @Override
    public User createUser(User user, String password) throws Exception {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new Exception("Username é obrigatório.");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new Exception("Email é obrigatório");
        }

        if (!isUsernameAvailable(user.getUsername())) {
            throw new Exception("Username já está em uso");
        }

        if (!isEmailAvailable(user.getEmail())) {
            throw new Exception("Email já está em uso");
        }

        byte[] salt = PasswordHelper.generateSalt();
        byte[] hashPassword = PasswordHelper.hashPassword(password, salt);

        user.setSaltPassword(salt);
        user.setHashPassword(hashPassword);

        return userRepository.save(user);

    }

    @Override
    public User authenticateUser(String usernameOrEmail, String password) throws Exception {
        return null;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<User> findAllUsers() {
        return List.of();
    }

    @Override
    public User updateUser(User user) throws Exception {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    @Override
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    @Override
    public void updateLastLogin(Long userId) {
        userRepository.updateLastLogin(userId);

    }
}
