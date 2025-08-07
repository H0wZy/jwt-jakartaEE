package com.estudos.repository.UserRepository;

import com.estudos.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class UserRepository implements IUserRepository {

    @PersistenceContext(unitName = "jwtPU")
    private EntityManager entityManager;

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            entityManager.persist(user);
            return user;
        } else {
            user.setUpdatedAt(new Date());
            return entityManager.merge(user);
        }
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u ORDER BY u.createdAt DESC", User.class).getResultList();
    }

    @Override
    public void delete(User user) {
        if (entityManager.contains(user)) {
            entityManager.remove(user);
        } else {
            entityManager.remove(entityManager.merge(user));
        }
    }

    @Override
    public void deleteById(Long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        try {
            User user = entityManager.createQuery("select u from User u where  u.username = :input or u.email = :input", User.class).setParameter("input", usernameOrEmail).getSingleResult();
            return Optional.of(user);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            User user = entityManager.createQuery("SELECT u FROM User u where u.username = :username", User.class).setParameter("username", username).getSingleResult();
            return Optional.of(user);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class).setParameter("email", email).getSingleResult();
            return Optional.of(user);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        Long count = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class).setParameter("username", username).getSingleResult();
        return count > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        Long count = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class).setParameter("email", email).getSingleResult();
        return count > 0;
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findByCargoAndIsUserDisabled(String cargo, boolean disabled) {
        return List.of();
    }

    @Override
    @Transactional
    public void updateLastLogin(Long userId) {
        entityManager.createQuery("UPDATE User u SET u.lastLoginAt = :now WHERE u.id = :id")
                .setParameter("now", new Date())
                .setParameter("id", userId)
                .executeUpdate();
    }
}
