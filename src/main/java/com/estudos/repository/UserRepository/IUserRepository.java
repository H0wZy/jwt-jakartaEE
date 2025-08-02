package com.estudos.repository.UserRepository;

import com.estudos.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String usernameOrEmail);

    List<User> findAll();

    void delete(User user);

    void deleteById(Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByCargoAndIsUserDisabled(String cargo, boolean disabled);
}
