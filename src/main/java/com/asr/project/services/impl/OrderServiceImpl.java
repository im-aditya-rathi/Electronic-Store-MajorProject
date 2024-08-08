package com.asr.project.services.impl;

import com.asr.project.dtos.OrderDto;
import com.asr.project.entities.*;
import com.asr.project.exceptions.BadApiResponseException;
import com.asr.project.exceptions.ResourceNotFoundException;
import com.asr.project.models.OrderRequest;
import com.asr.project.payloads.PageableResponse;
import com.asr.project.repositories.CartRepository;
import com.asr.project.repositories.OrderItemRepository;
import com.asr.project.repositories.OrderRepository;
import com.asr.project.repositories.UserRepository;
import com.asr.project.services.OrderService;
import com.asr.project.utils.Helper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public OrderDto createOrder(OrderRequest orderRequest) {

        String userId = orderRequest.getUserId();

        User user = userRepository.findById(userId).
                orElseThrow(()->new ResourceNotFoundException("User ID not found !!"));
        Cart cart = cartRepository.findByUser(user).
                orElseThrow(()->new ResourceNotFoundException("Cart not found for given user !!"));
        List<CartItem> cartItems = cart.getCartItems();
        if(cartItems.isEmpty()) {
            throw new BadApiResponseException("Order can't be created with empty cart !!");
        }

        Order order = Order.builder()
                .orderID(UUID.randomUUID().toString())
                .orderStatus(orderRequest.getOrderStatus())
                .paymentStatus(orderRequest.getPaymentStatus())
                .billingName(orderRequest.getBillingName())
                .billingAddress(orderRequest.getBillingAddress())
                .billingPhone(orderRequest.getBillingPhone())
                .orderedDate(new Date())
                .deliveredDate(null)
                .user(user)
                .build();

        AtomicReference<Double> totalOrderAmount = new AtomicReference<>(0.0);
        List<OrderItem> orderItemList = cartItems.stream().map(item -> {
            OrderItem orderItem = OrderItem.builder()
                    .orderItemId(UUID.randomUUID().toString())
                    .quantity(item.getQuantity())
                    .totalPrice(item.getTotalPrice())
                    .product(item.getProduct())
                    .order(order)
                    .build();
            totalOrderAmount.set(totalOrderAmount.get() + item.getTotalPrice());
            return orderItem;
        }).toList();

        //save order
        order.setOrderItems(orderItemList);
        order.setOrderAmount(totalOrderAmount.get());
        Order savedOrder = orderRepository.save(order);

        //save cart
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return entityToDto(savedOrder);
    }

    @Override
    public void removeOrder(String orderId) {

        Order order = orderRepository.findById(orderId).
                orElseThrow(()->new ResourceNotFoundException("Order ID not found !!"));
        orderRepository.delete(order);
    }

    @Override
    public OrderDto updateOrder(OrderRequest orderRequest, String orderId) {

        Order order = orderRepository.findById(orderId).
                orElseThrow(()->new ResourceNotFoundException("Order ID not found !!"));

        Order updatedOrder = Order.builder()
/*        just copy non-updatable fields      */
                .orderID(order.getOrderID())
                .orderedDate(order.getOrderedDate())
                .orderItems(order.getOrderItems())
                .orderAmount(order.getOrderAmount())
                .deliveredDate(order.getDeliveredDate())
                .user(order.getUser())

/*        update only updatable fields      */
                .billingName(orderRequest.getBillingName())
                .billingAddress(orderRequest.getBillingAddress())
                .billingPhone(orderRequest.getBillingPhone())
                .paymentStatus(orderRequest.getPaymentStatus())
                .orderStatus(orderRequest.getOrderStatus())
                .build();

        Order updatedOrderRes = orderRepository.save(updatedOrder);
        return entityToDto(updatedOrderRes);
    }

    @Override
    public OrderDto getOrder(String orderId) {

        Order order = orderRepository.findById(orderId).
                orElseThrow(()->new ResourceNotFoundException("Order ID not found !!"));
        return entityToDto(order);
    }

    @Override
    public PageableResponse<OrderDto> getAllOrdersOfUser(String userId, int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
        Page<Order> orders = orderRepository.findByUser_UserId(userId, pageable);
        return Helper.getPageableResponse(orders, OrderDto.class);
    }

    @Override
    public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize, sort);
        Page<Order> orders = orderRepository.findAll(pageable);
        return Helper.getPageableResponse(orders, OrderDto.class);
    }

    private OrderDto entityToDto(Order order) {

        return modelMapper.map(order, OrderDto.class);
    }

    private Order dtoToEntity(OrderDto orderDto) {

        return modelMapper.map(orderDto, Order.class);
    }
}
