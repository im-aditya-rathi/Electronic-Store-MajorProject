package com.asr.project.models;

import com.asr.project.dtos.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddItemCartRequest {

    private String productId;
    private int quantity;
}
