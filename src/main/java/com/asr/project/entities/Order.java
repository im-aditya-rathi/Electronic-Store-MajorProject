package com.asr.project.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    private String orderID;

    //PENDING, DISPATCHED, DELIVERED
    private String orderStatus;

    //PAID, NOTPAID
    private String paymentStatus;

    private Double orderAmount;
    private String billingName;
    private String billingPhone;
    private String billingAddress;
    private Date orderedDate;
    private Date deliveredDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
}
