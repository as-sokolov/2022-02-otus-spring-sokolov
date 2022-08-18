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
@Table(name = "pictures")
public class Picture implements StephenKingArt {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pictures_seq")
    @SequenceGenerator(name = "pictures_seq", sequenceName = "pictures_seq", allocationSize = 1)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "framematerial")
    private String frameMaterial;
}
