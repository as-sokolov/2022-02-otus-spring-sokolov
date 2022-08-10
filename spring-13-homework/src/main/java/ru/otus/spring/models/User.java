package ru.otus.spring.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="users_seq")
    @SequenceGenerator(name="users_seq", sequenceName="users_seq", allocationSize = 1)
    @Column(name="id")
    private long id;

    @Column(name = "login")
    private String login;

    @Column(name = "hash")
    private String password;

//    Не нужно для BCrypt, хранится внутри хэша
//    @Column(name = "salt")
//    private String salt;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "accountnonexpired")
    private boolean accountNonExpired;

    @Column(name = "credentialsnonexpired")
    private boolean credentialsNonExpired;

    @Column(name = "nonlocked")
    private boolean accountNonLocked;

    @Column(name = "role")
    private String role;
}
