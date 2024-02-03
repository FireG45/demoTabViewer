package ru.fireg45.demotabviewer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fireg45.demotabviewer.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByUsername(String username);
}
