package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @Schema(example = "1", description = "Id of a book", accessMode = Schema.AccessMode.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Integer id;


    @NotEmpty(message = "Please provide a title")
    @Schema(example= "Harry potterom", description = "Ttitle of book")
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @NotEmpty(message = "Please provide an author")
    @Schema(example= "J.K Rowling", description = "Author of book")
    @Column(name = "author", nullable = false)
    private String author;

    @NotEmpty(message = "Please provide a description")
    @Schema(example= "Book about magic", description = "Description of book")
    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @Schema(description = "Shopping cart in which book could be stored", accessMode = Schema.AccessMode.READ_ONLY)
    @ManyToMany(mappedBy = "books")
    @JsonIgnore
    @ToString.Exclude
    private Set<Cart> carts;

    @Schema(description = "Orders in which book could be stored", accessMode = Schema.AccessMode.READ_ONLY)
    @ManyToMany(mappedBy = "books")
    @JsonIgnore
    @ToString.Exclude
    private Set<Orders> orders;
}
