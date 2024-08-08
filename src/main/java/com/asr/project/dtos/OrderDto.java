package com.asr.project.dtos;

import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class OrderDto {

    private String orderID;

    //PENDING, DISPATCHED, DELIVERED
    private String orderStatus;

    //PAID, NOTPAID
    private String paymentStatus;

    private String billingName;
    private String billingPhone;
    private String billingAddress;
    private Double orderAmount;
    private Date orderedDate;
    private Date deliveredDate;
    private UserDto user;
    private List<OrderItemDto> orderItems = new ArrayList<>();
}
