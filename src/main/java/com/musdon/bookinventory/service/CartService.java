package com.musdon.bookinventory.service;

import com.musdon.bookinventory.exception.BookNotFoundException;
import com.musdon.bookinventory.model.*;
import com.musdon.bookinventory.model.response.CartResponse;
import com.musdon.bookinventory.model.response.Response;
import com.musdon.bookinventory.repository.BookRepository;
import com.musdon.bookinventory.repository.CartRepository;
import com.musdon.bookinventory.repository.TransactionRepository;
import com.musdon.bookinventory.util.Mapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.musdon.bookinventory.util.Mapper.mapCartToCartResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final TransactionRepository transactionRepository;

    public Response addToCart(String userEmail, Long bookId) {
        Optional<Cart> existingCartOptional = cartRepository.findActiveCartByUserEmail(userEmail, Status.ACTIVE);
        Cart cart = existingCartOptional.orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserEmail(userEmail);
            newCart.setStatus(Status.ACTIVE);
            return newCart;
        });

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        if (cart.getBooks() == null) {
            cart.setBooks(new ArrayList<>());
        }
        cart.getBooks().add(book);

        cartRepository.save(cart);

        return Response.builder()
                .responseMessage("Successfully added item to the cart")
                .cartItem(mapCartToCartResponse(cart))
                .build();
    }

    public Response viewUserPurchaseHistory(String userEmail) {
        List<Cart> cartList = cartRepository.findByUserEmail(userEmail);

        if (cartList.isEmpty()) {
            return Response.builder()
                    .responseMessage("No purchase history found for this user")
                    .cartItemList(Collections.emptyList())
                    .build();
        }

        List<CartResponse> cartResponses = cartList.stream()
                .map(Mapper::mapCartToCartResponse)
                .toList();

        return Response.builder()
                .responseMessage("Success")
                .cartItemList(cartResponses)
                .build();
    }

    public Response viewCart(Long cardId){
        Optional<Cart> cartOptional = cartRepository.findById(cardId);
        if (cartOptional.isEmpty()){
            return Response.builder()
                    .responseMessage("No cart found")
                    .build();
        }
        Cart cart = cartOptional.get();
        return Response.builder()
                .responseMessage("Success")
                .cartItem(mapCartToCartResponse(cart))
                .build();
    }

    @Transactional
    public void checkout(Long cartId, CheckoutOptions paymentChannel) {
        try{
            log.info("Starting checkout process for cart ID: {}", cartId);

            if (!EnumSet.of(CheckoutOptions.WEB, CheckoutOptions.USSD, CheckoutOptions.TRANSFER).contains(paymentChannel)) {
                log.error("Invalid payment channel {} for cart ID {}", paymentChannel, cartId);
                throw new RuntimeException("Invalid payment channel " + paymentChannel);
            }

            // Fetch cart by ID
            Optional<Cart> cartOptional = cartRepository.findById(cartId);
            if (cartOptional.isEmpty()) {
                log.error("Cart with ID {} not found", cartId);
                throw new RuntimeException("Cart with ID " + cartId + " not found");
            }

            Cart cart = cartOptional.get();

            // Check if cart is already paid
            if (cart.getStatus() == Status.PAID) {
                log.warn("Cart ID {} is already marked as paid", cartId);
                throw new RuntimeException("Cart ID " + cartId + " is already marked as paid");
            }

            if (cart.getBooks() == null || cart.getBooks().isEmpty()) {
                log.error("Cart ID {} has no books", cartId);
                throw new RuntimeException("Cart ID " + cartId + " has no books");
            }

            Double totalAmount = cart.getBooks().stream()
                    .map(Book::getPrice)
                    .reduce(0.0, Double::sum);

            Transactions transactions = Transactions.builder()
                    .books(cart.getBooks())
                    .cart(cart)
                    .transactionAmount(totalAmount)
                    .transactionReference(UUID.randomUUID().toString().substring(0, 10))
                    .transactionStatus("PAID")
                    .userEmail(cart.getUserEmail())
                    .paymentChannel(paymentChannel)
                    .build();
            try {
                transactionRepository.save(transactions);
            } catch (Exception e) {
                log.error("Error saving transaction", e);
                throw new RuntimeException("Transaction save failed: " + e.getMessage());
            }
            log.info("Transaction saved successfully: {}", transactions.getTransactionReference());

            cart.setStatus(Status.PAID);
            cartRepository.save(cart);
            log.info("Cart ID {} marked as PAID", cartId);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
