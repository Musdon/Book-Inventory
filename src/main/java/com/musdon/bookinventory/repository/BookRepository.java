package com.musdon.bookinventory.repository;

import com.musdon.bookinventory.model.Book;
import com.musdon.bookinventory.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbnCode(String isbnCode);

    @Query("SELECT b FROM Book b WHERE " +
            "(:title IS NULL OR b.title LIKE %:title%) AND " +
            "(:author IS NULL OR b.author LIKE %:author%) AND " +
            "(:yearOfPublication IS NULL OR b.yearOfPublication = :yearOfPublication) AND " +
            "(:genre IS NULL OR b.genre = :genre)")
    List<Book> searchBooks(@Param("title") String title,
                           @Param("author") String author,
                           @Param("yearOfPublication") Integer yearOfPublication,
                           @Param("genre") Genre genre);
}
