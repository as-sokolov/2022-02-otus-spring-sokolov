package ru.otus.spring.models.rdbms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "books")
@NamedEntityGraph(name = "book-with-relations",
        attributeNodes = {
                @NamedAttributeNode("authorList"),
                @NamedAttributeNode("genre")
        })
public class Book {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="books_seq")
    @SequenceGenerator(name="books_seq", sequenceName="books_seq", allocationSize = 1)
    @Column(name="id")
    private long id;

    @Column(name = "name")
    private String name;

    // Список авторов обычно не слишком большой. Это наиболее часто используемая информация о книге, поэтому
    // читаем при каждой выборке
    @ManyToMany(targetEntity = Author.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name = "books_authors", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    @Fetch(FetchMode.JOIN)
    private List<Author> authorList;

    // Жанр, как и список авторов, важная информация о книге. Читаем сразу
    @ManyToOne(targetEntity = Genre.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "genre_id")
    @Fetch(FetchMode.JOIN)
    private Genre genre;

    @OneToMany(orphanRemoval = true, targetEntity = Comment.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    @Fetch(FetchMode.SUBSELECT)
    private List<Comment> commentList;

    public Book() {
        authorList = new ArrayList<>();
        commentList = new ArrayList<>();
    }
}
