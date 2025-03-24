package com.musdon.bookinventory.repository;

import com.musdon.bookinventory.model.Cart;
import com.musdon.bookinventory.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserEmail(String userEmail);
    @Query("SELECT c FROM Cart c WHERE c.userEmail = :userEmail AND c.status = :status")
    Optional<Cart> findActiveCartByUserEmail(@Param("userEmail") String userEmail, @Param("status") Status status);
}
