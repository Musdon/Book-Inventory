package com.musdon.bookinventory.service;

import com.musdon.bookinventory.exception.BookNotFoundException;
import com.musdon.bookinventory.exception.InvalidInputException;
import com.musdon.bookinventory.model.Book;
import com.musdon.bookinventory.model.Genre;
import com.musdon.bookinventory.model.request.CreateBookRequest;
import com.musdon.bookinventory.model.response.BookInventoryResponse;
import com.musdon.bookinventory.model.response.Response;
import com.musdon.bookinventory.repository.BookRepository;
import com.musdon.bookinventory.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Response addBook(CreateBookRequest createBookRequest) {
        Optional<Book> checkBookExists = bookRepository.findByIsbnCode(createBookRequest.getIsbnCode());
        if (checkBookExists.isPresent()) {
            throw new InvalidInputException("Book already exists");
        }
        Book newBook = Book.builder()
                .title(createBookRequest.getTitle())
                .genre(createBookRequest.getGenre())
                .author(createBookRequest.getAuthor())
                .yearOfPublication(createBookRequest.getYearOfPublication())
                .isbnCode(createBookRequest.getIsbnCode())
                .price(createBookRequest.getPrice())
                .build();
        Book savedBook = bookRepository.save(newBook);
        return Response.builder()
                .responseMessage("Book added successfully")
                .bookInventory(Mapper.convertBookToBookInventoryResponse(savedBook))
                .build();
    }

    public Response searchBooks(String title, String author, Integer yearOfPublication, Genre genre) {

        List<Book> books;
        if (title == null && author == null && yearOfPublication == null && genre == null) {
            books = bookRepository.findAll();
        } else {
            books = bookRepository.searchBooks(title, author, yearOfPublication, genre);
        }
        if (books.isEmpty()) {
            return Response.builder()
                    .responseMessage("No books found")
                    .build();
        }
        List<BookInventoryResponse> bookInventoryResponseList = books.stream()
                .map(Mapper::convertBookToBookInventoryResponse).toList();

        return Response.builder()
                .responseMessage("Books found")
                .bookInventoryList(bookInventoryResponseList)
                .totalBooks(books.size())
                .build();
    }
}
