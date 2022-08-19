package ru.otus.spring.models;

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
@Table(name = "books")
@EqualsAndHashCode
public class Book implements StephenKingArt {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="books_seq")
    @SequenceGenerator(name="books_seq", sequenceName="books_seq", allocationSize = 1)
    @Column(name="id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "drawings")
    private String drawings;

    @Column(name = "decoration")
    private String decoration;

    @Column(name = "pagecount")
    private int pageCount;

}