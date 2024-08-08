package com.asr.project.services;

import com.asr.project.dtos.OrderDto;
import com.asr.project.models.OrderRequest;
import com.asr.project.payloads.PageableResponse;

public interface OrderService {

    OrderDto createOrder(OrderRequest orderDto);

    void removeOrder(String orderId);

    OrderDto updateOrder(OrderRequest orderRequest, String orderId);

    OrderDto getOrder(String orderId);

    PageableResponse<OrderDto> getAllOrdersOfUser(String userId, int pageNumber, int pageSize, String sortBy, String sortDir);

    PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

}
