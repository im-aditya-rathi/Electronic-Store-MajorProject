package com.asr.project.models;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class OrderRequest {

    @NotBlank(message = "User Id is required !!")
    private String userId;

    private String orderStatus = "PENDING";

    private String paymentStatus = "NOTPAID";

    @NotBlank(message = "Billing Name is required !!")
    private String billingName;

    @NotBlank(message = "Billing Phone is required !!")
    private String billingPhone;

    @NotBlank(message = "Billing Address is required !!")
    private String billingAddress;
}
