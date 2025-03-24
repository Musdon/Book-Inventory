package com.musdon.bookinventory.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double transactionAmount;
    private String transactionStatus;
    @Enumerated(EnumType.STRING)
    private CheckoutOptions paymentChannel;
    @Column(unique = true)
    private String transactionReference;
    private String userEmail;
    @OneToOne
    private Cart cart;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "transaction_books", // Join table to map the relationship
            joinColumns = @JoinColumn(name = "transaction_id"), // Foreign key to Transactions
            inverseJoinColumns = @JoinColumn(name = "book_id")// Foreign key to Books
    )
    private List<Book> books;
}
