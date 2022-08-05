package ru.otus.spring.service;

import ru.otus.spring.models.User;

public interface UserService {
    User getUser(String login);
}
