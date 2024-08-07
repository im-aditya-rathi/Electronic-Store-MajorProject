package com.asr.project.controllers;

import com.asr.project.dtos.CartDto;
import com.asr.project.models.AddItemCartRequest;
import com.asr.project.payloads.ApiResponseMessage;
import com.asr.project.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}")
    ResponseEntity<CartDto> addItemToCart(@PathVariable String userId,
            @RequestBody AddItemCartRequest addItemCartRequest) {

        return ResponseEntity.status(HttpStatus.CREATED).
                body(cartService.addItemToCart(userId, addItemCartRequest));
    }

    @GetMapping("/{userId}")
    ResponseEntity<CartDto> getCart(@PathVariable String userId) {

        CartDto cartDto = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/items/{itemId}")
    ResponseEntity<ApiResponseMessage> removeItemToCart(@PathVariable String userId, @PathVariable String itemId) {

        cartService.removeItemFromCart(userId, itemId);
        ApiResponseMessage response = ApiResponseMessage.builder().
                message("Item is removed from cart successfully !!").success(true).
                status(HttpStatus.OK).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId) {

        cartService.clearCart(userId);
        ApiResponseMessage response = ApiResponseMessage.builder().
                message("Now cart is empty !!").success(true).
                status(HttpStatus.OK).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
