package com.estudos.service.UserService;

import com.estudos.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    User createUser(User user, String password) throws Exception;

    User authenticateUser(String usernameOrEmail, String password) throws Exception;

    Optional<User> findById(Long id);

    List<User> findAllUsers();

    User updateUser(User user) throws Exception;

    void deleteUser(Long id);

    boolean isUsernameAvailable(String username);

    boolean isEmailAvailable(String email);

    void updateLastLogin(Long userId);
}
