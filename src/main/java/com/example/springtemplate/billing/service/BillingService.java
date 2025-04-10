package com.example.springtemplate.billing.service;

import com.example.springtemplate.billing.dto.BillingDTO;
import com.example.springtemplate.billing.entity.Billing;
import com.example.springtemplate.billing.exception.InvalidInvoiceException;
import com.example.springtemplate.billing.repository.BillingRepository;
import com.example.springtemplate.billing.util.BillingValidationUtil;
import com.example.springtemplate.common.exception.ResourceNotFoundException;
import com.example.springtemplate.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillingService
{
    private final BillingRepository billingRepository;

    @Autowired
    public BillingService(BillingRepository billingRepository)
    {
        this.billingRepository = billingRepository;
    }

    public BillingDTO create(BillingDTO dto)
    {
        BillingValidationUtil.validateInvoiceNumberFormat(dto.getInvoiceNumber());
        BillingValidationUtil.validateAmount(String.valueOf(dto.getAmount()));
        BillingValidationUtil.validateCustomerName(dto.getCustomerName());

        if (billingRepository.existsByInvoiceNumber(dto.getInvoiceNumber())) {
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

    public List<BillingDTO> findAll()
    {
        return billingRepository.findAll(Sort.by("customerName").ascending())
                .stream()
                .map(b -> BillingDTO.builder()
                        .invoiceNumber(b.getInvoiceNumber())
                        .customerName(b.getCustomerName())
                        .amount(b.getAmount())
                        .durationSinceCreated(DateTimeUtil.calculateDuration(b.getCreatedAt()))
                        .build())
                .collect(Collectors.toList());
    }


    public BillingDTO getBillingByInvoiceNumber(String invoiceNumber)
    {
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

    public BillingDTO update(String invoiceNumber, BillingDTO dto)
    {
        BillingValidationUtil.validateInvoiceNumberFormat(invoiceNumber);
        BillingValidationUtil.validateAmount(String.valueOf(dto.getAmount()));
        BillingValidationUtil.validateCustomerName(dto.getCustomerName());

        Billing billing = billingRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice number not found: " + invoiceNumber));

        billing.setCustomerName(dto.getCustomerName());
        billing.setAmount(dto.getAmount());
        billing.setCreatedAt(LocalDateTime.now());

        Billing updatedBilling = billingRepository.save(billing);

        dto.setInvoiceNumber(updatedBilling.getInvoiceNumber());
        dto.setCustomerName(updatedBilling.getCustomerName());
        dto.setAmount(updatedBilling.getAmount());
        dto.setDurationSinceCreated(DateTimeUtil.calculateDuration(updatedBilling.getCreatedAt()));

        return dto;
    }

    public String delete(String invoiceNumber)
    {
        BillingValidationUtil.validateInvoiceNumberFormat(invoiceNumber);

        Billing billing = billingRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice number not found: " + invoiceNumber));

        billingRepository.delete(billing);
        System.out.println(invoiceNumber);
        return ("Record with invoice number " + invoiceNumber + " has been deleted successfully.");
    }

}
