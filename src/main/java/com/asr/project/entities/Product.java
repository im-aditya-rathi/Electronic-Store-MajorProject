package com.asr.project.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "products")
public class Product {

    @Id
    private String productId;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    private Double price;
    private Double discountedPrice;
    private Integer quantity;
    private Boolean live;
    private Boolean stock;
    private Date addedDate;
    private String coverImage;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
