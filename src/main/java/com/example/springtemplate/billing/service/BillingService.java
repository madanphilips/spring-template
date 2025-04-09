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

    // Create a new billing record and return it as a DTO
    public BillingDTO create(BillingDTO dto) {
        // Validate invoice number format
        BillingValidationUtil.validateInvoiceNumberFormat(dto.getInvoiceNumber());

        // Check if the invoice number already exists
        if (billingRepository.existsByInvoiceNumber(dto.getInvoiceNumber())) {
            throw new DuplicateInvoiceException("Invoice number " + dto.getInvoiceNumber() + " already exists.");
        }

        // Check if customer name is empty or null
        if (dto.getCustomerName() == null || dto.getCustomerName().isEmpty()) {
            throw new CustomerNameEmptyException("Please enter a customer name.");
        }

        // Convert DTO to Billing entity
        Billing billing = convertToEntity(dto);

        // Save the new billing entity
        Billing savedBilling = billingRepository.save(billing);

        // Return the saved billing as a DTO
        return convertToDTO(savedBilling);
    }

    // Update a billing record by invoice number
    public BillingDTO update(String invoiceNumber, BillingDTO dto) {
        Billing existingBilling = billingRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Billing record not found for invoice number: " + invoiceNumber));

        // Check if customer name is empty or null during update
        if (dto.getCustomerName() == null || dto.getCustomerName().isEmpty()) {
            throw new CustomerNameEmptyException("Please enter a customer name.");
        }

        // Update the billing details
        if (dto.getCustomerName() != null) {
            existingBilling.setCustomerName(dto.getCustomerName());
        }
        if (dto.getAmount() != null) {
            existingBilling.setAmount(dto.getAmount());
        }

        Billing updatedBilling = billingRepository.save(existingBilling);

        return convertToDTO(updatedBilling);
    }

    // Delete a billing record by invoice number
    public void delete(String invoiceNumber) {
        Billing existingBilling = billingRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Billing record not found for invoice number: " + invoiceNumber));

        billingRepository.delete(existingBilling);
    }

    // Helper method to convert Billing entity to BillingDTO
    private BillingDTO convertToDTO(Billing billing) {
        // Calculate the time since creation
        String durationSinceCreated = calculateDuration(billing.getCreatedAt());

        // Build and return DTO
        return BillingDTO.builder()
                .invoiceNumber(billing.getInvoiceNumber())
                .customerName(billing.getCustomerName())
                .amount(billing.getAmount())
                .durationSinceCreated(durationSinceCreated)
                .build();
    }

    // Helper method to convert BillingDTO to Billing entity
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

    // Helper method to calculate the duration since the billing was created
    private String calculateDuration(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        // Calculate the time difference in days, hours, or minutes
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
