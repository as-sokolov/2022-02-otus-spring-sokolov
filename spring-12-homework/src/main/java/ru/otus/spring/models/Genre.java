package ru.otus.spring.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="genres_seq")
    @SequenceGenerator(name="genres_seq", sequenceName="genres_seq", allocationSize = 1)
    @Column(name="id")
    private long id;

    @Column(name = "name")
    private String name;
}
