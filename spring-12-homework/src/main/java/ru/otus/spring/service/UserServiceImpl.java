package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.models.User;
import ru.otus.spring.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(String login) {
        return userRepository.getUserByLogin(login);
    }
}
