package com.asr.project.controllers;

import com.asr.project.dtos.OrderDto;
import com.asr.project.models.OrderRequest;
import com.asr.project.payloads.ApiResponseMessage;
import com.asr.project.payloads.PageableResponse;
import com.asr.project.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    ResponseEntity<OrderDto> createOrder(@RequestBody @Valid OrderRequest orderRequest) {

        OrderDto orderDto = orderService.createOrder(orderRequest);
        return new ResponseEntity<>(orderDto, HttpStatus.CREATED);
    }

    @PutMapping("/{orderID}")
    ResponseEntity<OrderDto> updateOrder(@RequestBody @Valid OrderRequest orderRequest, @PathVariable String orderID) {

        OrderDto orderDto = orderService.updateOrder(orderRequest, orderID);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable String orderId) {

        orderService.removeOrder(orderId);
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message("Order deleted successfully").success(true).
                status(HttpStatus.OK).build();
        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping("/{orderId}")
    ResponseEntity<OrderDto> getOrder(@PathVariable String orderId) {

        OrderDto orderDto = orderService.getOrder(orderId);
        return ResponseEntity.ok(orderDto);
    }

    @GetMapping("users/{userId}")
    ResponseEntity<PageableResponse<OrderDto>> getAllOrdersOfUser(@PathVariable String userId,
            @RequestParam( value = "pageNumber", defaultValue = "1") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "orderedDate") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir
    ) {

        PageableResponse<OrderDto> pageableResponse = orderService.getAllOrdersOfUser(userId, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(pageableResponse);
    }

    @GetMapping
    ResponseEntity<PageableResponse<OrderDto>> getAllOrders(
            @RequestParam( value = "pageNumber", defaultValue = "1") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "orderedDate") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir
    ) {

            PageableResponse<OrderDto> pageableResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
            return ResponseEntity.ok(pageableResponse);
    }
}
