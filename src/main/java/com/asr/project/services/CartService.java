package com.asr.project.services;

import com.asr.project.dtos.CartDto;
import com.asr.project.models.AddItemCartRequest;

public interface CartService {

    CartDto getCartByUser(String userId);

    CartDto addItemToCart(String userId, AddItemCartRequest addItemCartRequest);

    void removeItemFromCart(String userId, String itemId);

    void clearCart(String userId);
}
