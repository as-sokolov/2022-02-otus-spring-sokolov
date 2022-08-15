package ru.otus.spring.models.rdbms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
@Table(name = "authors")
@EqualsAndHashCode
public class Author {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="authors_seq")
    @SequenceGenerator(name="authors_seq", sequenceName="authors_seq", allocationSize = 1)
    @Column(name="id")
    private long id;

    @Column(name = "name")
    private String name;
}