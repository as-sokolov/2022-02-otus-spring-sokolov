package ru.otus.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.models.User;

public interface UserRepository  extends JpaRepository<User, Long> {
    User getUserByLogin(String id);
}
