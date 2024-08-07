package com.asr.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartItemDto {

    private String cartItemId;
    private int quantity;
    private Double totalPrice;
    private ProductDto product;
//    private CartDto cart;
}
