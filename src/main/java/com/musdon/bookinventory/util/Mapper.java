package com.musdon.bookinventory.util;

import com.musdon.bookinventory.model.Book;
import com.musdon.bookinventory.model.Cart;
import com.musdon.bookinventory.model.response.BookInventoryResponse;
import com.musdon.bookinventory.model.response.CartResponse;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper {
    public static BookInventoryResponse convertBookToBookInventoryResponse(Book book) {
        return BookInventoryResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .genre(book.getGenre())
                .author(book.getAuthor())
                .yearOfPublication(book.getYearOfPublication())
                .isbnCode(book.getIsbnCode())
                .price(book.getPrice())
                .build();
    }

    public static CartResponse mapCartToCartResponse(Cart cart) {
        List<Book> book = cart.getBooks();
        return CartResponse.builder()
                .id(cart.getId())
                .userEmail(cart.getUserEmail())
                .bookInventoryResponse(book.stream().map(Mapper::convertBookToBookInventoryResponse).toList())
                .build();
    }
}
