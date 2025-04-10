package com.example.springtemplate.billing.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BillingDTO {
    private String invoiceNumber;
    private String customerName;
    private Double amount;
    private String durationSinceCreated;
}