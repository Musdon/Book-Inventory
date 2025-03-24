package com.musdon.bookinventory.service;

import com.musdon.bookinventory.model.*;
import com.musdon.bookinventory.model.response.Response;
import com.musdon.bookinventory.repository.BookRepository;
import com.musdon.bookinventory.repository.CartRepository;
import com.musdon.bookinventory.repository.TransactionRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CartServiceTest {
    private final CartRepository cartRepository = Mockito.mock(CartRepository.class);
    private final BookRepository bookRepository = Mockito.mock(BookRepository.class);
    private final TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);

    private final CartService cartService = new CartService(cartRepository, bookRepository, transactionRepository);

    @Test
    void testAddToCart() {
        // Arrange
        String userEmail = "testuser@example.com";
        Long bookId = 1L;

        Cart cart = new Cart();
        cart.setUserEmail(userEmail);
        cart.setStatus(Status.ACTIVE);

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Test Book");

        when(cartRepository.findActiveCartByUserEmail(userEmail, Status.ACTIVE)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // Act
        Response response = cartService.addToCart(userEmail, bookId);

        // Assert
        assertEquals("Successfully added item to the cart", response.getResponseMessage());
        assertNotNull(response.getCartItem());
        assertEquals(userEmail, response.getCartItem().getUserEmail());
        verify(cartRepository, times(1)).save(cart);
    }
    @Test
    void testViewUserPurchaseHistory() {
        // Arrange
        String userEmail = "testuser@example.com";

        Cart cart1 = new Cart();
        cart1.setId(1L);
        cart1.setUserEmail(userEmail);

        Cart cart2 = new Cart();
        cart2.setId(2L);
        cart2.setUserEmail(userEmail);

        when(cartRepository.findByUserEmail(userEmail)).thenReturn(List.of(cart1, cart2));

        // Act
        Response response = cartService.viewUserPurchaseHistory(userEmail);

        // Assert
        assertEquals("Success", response.getResponseMessage());
        assertNotNull(response.getCartItemList());
        assertEquals(2, response.getCartItemList().size());
        verify(cartRepository, times(1)).findByUserEmail(userEmail);
    }

    @Test
    void testCheckout() {
        // Arrange
        Long cartId = 1L;
        CheckoutOptions paymentChannel = CheckoutOptions.WEB;

        // Create sample books
        Book book1 = Book.builder()
                .id(1L)
                .title("Book One")
                .genre(Genre.FICTION)
                .author("Author One")
                .isbnCode("ISBN001")
                .yearOfPublication(2021)
                .price(100.0)
                .build();

        Book book2 = Book.builder()
                .id(2L)
                .title("Book Two")
                .genre(Genre.HORROR)
                .author("Author Two")
                .isbnCode("ISBN002")
                .yearOfPublication(2020)
                .price(150.0)
                .build();

        // Create a sample cart
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setUserEmail("testuser@example.com");
        cart.setStatus(Status.ACTIVE);
        cart.setBooks(List.of(book1, book2));

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        cartService.checkout(cartId, paymentChannel);

        assertEquals(Status.PAID, cart.getStatus());
        verify(transactionRepository, times(1)).save(any(Transactions.class));
        verify(cartRepository, times(1)).save(cart);
    }
}


