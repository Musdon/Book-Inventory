package com.musdon.bookinventory.controller;

import com.musdon.bookinventory.model.CheckoutOptions;
import com.musdon.bookinventory.model.response.Response;
import com.musdon.bookinventory.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.musdon.bookinventory.util.AppConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(CARTS)
public class CartController {
    private final CartService cartService;

    @PostMapping("add/"+BOOK_ID)
    public ResponseEntity<Response> addBookToCart(@RequestParam String userEmail, @PathVariable Long bookId) {
        Response response = cartService.addToCart(userEmail, bookId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("purchase-history")
    public ResponseEntity<Response> viewUserPurchaseHistory(@RequestParam String userEmail) {
        Response response = cartService.viewUserPurchaseHistory(userEmail);
        return ResponseEntity.ok(response);
    }

    @GetMapping(CART_ID + "/view-cart")
    public ResponseEntity<Response> viewCart(@PathVariable Long cartId) {
        Response response = cartService.viewCart(cartId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("check-out/"+CART_ID)
    public ResponseEntity<String> checkOut(@PathVariable Long cartId, @RequestParam CheckoutOptions paymentChannel) {
        cartService.checkout(cartId, paymentChannel);
        return ResponseEntity.ok("Payment successful");
    }
}
