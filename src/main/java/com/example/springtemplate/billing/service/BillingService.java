package com.example.springtemplate.billing.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.springtemplate.billing.dto.BillingDTO;
import com.example.springtemplate.billing.entity.Billing;
import com.example.springtemplate.billing.repository.BillingRepository;
import com.example.springtemplate.billing.util.BillingValidationUtil;
import com.example.springtemplate.common.exception.CustomerNameEmptyException;
import com.example.springtemplate.common.exception.DuplicateInvoiceException;
import com.example.springtemplate.common.exception.ResourceNotFoundException;

@Service
public class BillingService {

    private final BillingRepository billingRepository;

    public BillingService(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    public BillingDTO create(BillingDTO dto) {
        BillingValidationUtil.validateInvoiceNumberFormat(dto.getInvoiceNumber());

        if (billingRepository.existsByInvoiceNumber(dto.getInvoiceNumber())) {
            throw new DuplicateInvoiceException("Invoice number " + dto.getInvoiceNumber() + " already exists.");
        }

        if (dto.getCustomerName() == null || dto.getCustomerName().isEmpty()) {
            throw new CustomerNameEmptyException("Please enter a customer name.");
        }

        Billing billing = convertToEntity(dto);

        Billing savedBilling = billingRepository.save(billing);

        return convertToDTO(savedBilling);
    }

    public BillingDTO update(String invoiceNumber, BillingDTO dto) {
        Billing existingBilling = billingRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Billing record not found for invoice number: " + invoiceNumber));

        if (dto.getCustomerName() == null || dto.getCustomerName().isEmpty()) {
            throw new CustomerNameEmptyException("Please enter a customer name.");
        }

        if (dto.getCustomerName() != null) {
            existingBilling.setCustomerName(dto.getCustomerName());
        }
        if (dto.getAmount() != null) {
            existingBilling.setAmount(dto.getAmount());
        }

        Billing updatedBilling = billingRepository.save(existingBilling);

        return convertToDTO(updatedBilling);
    }

    public void delete(String invoiceNumber) {
        Billing existingBilling = billingRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Billing record not found for invoice number: " + invoiceNumber));

        billingRepository.delete(existingBilling);
    }

    private BillingDTO convertToDTO(Billing billing) {
        String durationSinceCreated = calculateDuration(billing.getCreatedAt());

        return BillingDTO.builder()
                .invoiceNumber(billing.getInvoiceNumber())
                .customerName(billing.getCustomerName())
                .amount(billing.getAmount())
                .durationSinceCreated(durationSinceCreated)
                .build();
    }

    private Billing convertToEntity(BillingDTO dto) {
        Billing billing = new Billing();
        billing.setInvoiceNumber(dto.getInvoiceNumber());
        billing.setCustomerName(dto.getCustomerName());
        billing.setAmount(dto.getAmount());
        billing.setCreatedAt(LocalDateTime.now());  
        return billing;
    }

    public List<BillingDTO> findAll() {
        List<Billing> billings = billingRepository.findAll(Sort.by("customerName").ascending());
        return billings.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public BillingDTO getBillingByInvoiceNumber(String invoiceNumber) {
        Billing billing = billingRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Billing record not found for invoice number: " + invoiceNumber));
        return convertToDTO(billing);
    }

    private String calculateDuration(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        if (duration.toDays() > 0) {
            return duration.toDays() + " days ago";
        } else if (duration.toHours() > 0) {
            return duration.toHours() + " hours ago";
        } else if (duration.toMinutes() > 0) {
            return duration.toMinutes() + " minutes ago";
        } else {
            return "Just now";
        }
    }
}
