package com.asr.project.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class OrderItemDto {

    private String orderItemId;
    private int quantity;
    private int totalPrice;
    private ProductDto product;

/*  removed since recursive call happening  */
//    private OrderDto order;
}
