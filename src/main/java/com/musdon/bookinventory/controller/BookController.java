package com.musdon.bookinventory.controller;

import com.musdon.bookinventory.model.Book;
import com.musdon.bookinventory.model.Genre;
import com.musdon.bookinventory.model.request.CreateBookRequest;
import com.musdon.bookinventory.model.response.BookInventoryResponse;
import com.musdon.bookinventory.model.response.Response;
import com.musdon.bookinventory.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.musdon.bookinventory.util.AppConstants.BOOKS;

@RestController
@RequiredArgsConstructor
@RequestMapping(BOOKS)
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Response> addBook(@RequestBody @Valid CreateBookRequest createBookRequest) {
        return ResponseEntity.ok(bookService.addBook(createBookRequest));
    }

    @GetMapping("/search")
    public ResponseEntity<Response> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Integer yearOfPublication,
            @RequestParam(required = false) Genre genre) {
        Response books = bookService.searchBooks(title, author, yearOfPublication, genre);
        return ResponseEntity.ok(books);
    }


}
