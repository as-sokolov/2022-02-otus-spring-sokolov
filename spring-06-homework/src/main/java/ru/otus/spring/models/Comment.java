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
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="comments_seq")
    @SequenceGenerator(name="comments_seq", sequenceName="comments_seq", allocationSize = 1)
    @Column(name="id")
    private long id;

    @Column(name = "text", nullable = false)
    private String text;
}
