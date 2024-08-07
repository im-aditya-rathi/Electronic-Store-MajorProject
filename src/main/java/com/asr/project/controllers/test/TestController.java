package com.asr.project.controllers.test;

import com.asr.project.entities.CartItem;
import com.asr.project.repositories.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("tests/")
public class TestController {

    @Autowired
    CartItemRepository cartItemRepository;

    @GetMapping("/carts/{cartId}/items")
    ResponseEntity<List<CartItem>> getCartItemsFromCartId(@PathVariable String cartId) {

        List<CartItem> cartItems = cartItemRepository.findByCart_CartId(cartId);
        return ResponseEntity.ok(cartItems);
    }

}
