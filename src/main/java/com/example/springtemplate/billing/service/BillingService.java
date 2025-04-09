package com.example.springtemplate.billing.service;


import com.example.springtemplate.billing.dto.BillingDTO;
import com.example.springtemplate.billing.entity.Billing;
import com.example.springtemplate.billing.exception.InvalidInvoiceException;
import com.example.springtemplate.billing.repository.BillingRepository;
import com.example.springtemplate.billing.util.BillingValidationUtil;
import com.example.springtemplate.common.exception.ResourceNotFoundException;
import com.example.springtemplate.common.util.DateTimeUtil;

import org.springframework.data.domain.Sort;
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
        
        BillingValidationUtil.validateInvoiceNumberFormat(dto.getInvoiceNumber());
        BillingValidationUtil.validateCustomerName(dto.getCustomerName());
        BillingValidationUtil.validateAmount(dto.getAmount());
    
        if (billingRepository.findByInvoiceNumber(dto.getInvoiceNumber()).isPresent()) {
            throw new InvalidInvoiceException("Invoice number already exists: " + dto.getInvoiceNumber());
        }
        
        Billing billing = Billing.builder()
                .invoiceNumber(dto.getInvoiceNumber())
                .customerName(dto.getCustomerName())
                .amount(dto.getAmount())
                .createdAt(LocalDateTime.now())
                .build();

        Billing savedBilling = billingRepository.save(billing);
        dto.setDurationSinceCreated(DateTimeUtil.calculateDuration(savedBilling.getCreatedAt()));

        return dto;
    }

    public List<BillingDTO> findAll(String sortBy, String order) {
        Sort sort = order.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
    
        List<Billing> billings = billingRepository.findAll(sort);
    
        return billings.stream()
                .map(b -> BillingDTO.builder()
                        .invoiceNumber(b.getInvoiceNumber())
                        .customerName(b.getCustomerName())
                        .amount(b.getAmount())
                        .durationSinceCreated(DateTimeUtil.calculateDuration(b.getCreatedAt()))
                        .build())
                .collect(Collectors.toList());
    }
    

    public BillingDTO getBillingByInvoiceNumber(String invoiceNumber) {
        BillingValidationUtil.validateInvoiceNumberFormat(invoiceNumber);

        Billing billing = billingRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice number not found: " + invoiceNumber));


        BillingDTO dto = new BillingDTO();
        dto.setInvoiceNumber(billing.getInvoiceNumber());
        dto.setCustomerName(billing.getCustomerName());
        dto.setAmount(billing.getAmount());
        dto.setDurationSinceCreated(DateTimeUtil.calculateDuration(billing.getCreatedAt()));
        return dto;

    }

    public BillingDTO updateBillingByInvoiceNumber(String invoiceNumber, BillingDTO updateData) {
        BillingValidationUtil.validateInvoiceNumberFormat(invoiceNumber);

        Billing billing = billingRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice number not found: " + invoiceNumber));
    
        if (updateData.getCustomerName() != null)
            billing.setCustomerName(updateData.getCustomerName());
    
        if (updateData.getAmount() != null)
            billing.setAmount(updateData.getAmount());
    
        Billing updated = billingRepository.save(billing);
    
        BillingDTO dto = new BillingDTO();
        dto.setInvoiceNumber(updated.getInvoiceNumber());
        dto.setCustomerName(updated.getCustomerName());
        dto.setAmount(updated.getAmount());
        dto.setDurationSinceCreated(DateTimeUtil.calculateDuration(updated.getCreatedAt()));
        return dto;
    }

    public void deleteByInvoiceNumber(String invoiceNumber) {
        Billing billing = billingRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice number not found: " + invoiceNumber));
        billingRepository.delete(billing);
    }
    
    

}