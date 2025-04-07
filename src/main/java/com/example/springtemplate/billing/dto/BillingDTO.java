package com.example.springtemplate.billing.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingDTO {
    private String invoiceNumber;
    private String customerName;
    private Double amount;
    private String durationSinceCreated;
}