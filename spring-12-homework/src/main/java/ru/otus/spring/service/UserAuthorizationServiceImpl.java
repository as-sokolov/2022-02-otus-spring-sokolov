package ru.otus.spring.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.spring.models.User;
import java.util.Arrays;
import java.util.Collections;

@Service
@Slf4j
public class UserAuthorizationServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserAuthorizationServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userService.getUser(login);

        if (user == null) {
            log.warn("UserAuthorizationServiceImpl.loadUserByUsername User was not found {}", login);
            throw new RuntimeException("Пользователь с таким именем не найден");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}
