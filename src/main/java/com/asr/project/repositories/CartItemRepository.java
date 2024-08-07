package com.asr.project.repositories;

import com.asr.project.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, String> {

    // fetches all cart items from the given cart id
    List<CartItem> findByCart_CartId(String cartId);
}
