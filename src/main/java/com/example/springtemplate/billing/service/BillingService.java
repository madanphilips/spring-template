package com.example.springtemplate.billing.service;


import com.example.springtemplate.billing.dto.BillingDTO;
import com.example.springtemplate.billing.entity.Billing;
import com.example.springtemplate.billing.repository.BillingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillingService {

    private final BillingRepository billingRepository;

    public BillingService(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    public BillingDTO create(BillingDTO dto) {
        Billing billing = Billing.builder()
                .invoiceNumber(dto.getInvoiceNumber())
                .customerName(dto.getCustomerName())
                .amount(dto.getAmount())
                .createdAt(LocalDateTime.now())
                .build();

        billingRepository.save(billing);
        return dto;
    }

    public List<BillingDTO> findAll() {
        return billingRepository.findAll()
                .stream()
                .map(b -> BillingDTO.builder()
                        .invoiceNumber(b.getInvoiceNumber())
                        .customerName(b.getCustomerName())
                        .amount(b.getAmount())
                        .build())
                .collect(Collectors.toList());
    }
}