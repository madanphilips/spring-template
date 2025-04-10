package com.example.springtemplate.billing.service.ImpServices;


import com.example.springtemplate.billing.dto.BillingDTO;
import com.example.springtemplate.billing.entity.Billing;
import com.example.springtemplate.billing.exception.InvalidInvoiceException;
import com.example.springtemplate.billing.repository.BillingRepository;
import com.example.springtemplate.billing.service.Iservices;
import com.example.springtemplate.billing.util.BillingValidationUtil;
import com.example.springtemplate.common.exception.ResourceNotFoundException;
import com.example.springtemplate.common.util.DateTimeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillingService implements Iservices {

    private final BillingRepository billingRepository;

    private static final Logger log = LogManager.getLogger(BillingService.class);
    @Autowired
    private BillingValidationUtil billingValidationUtil;

    public BillingService(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    @Override
    public BillingDTO create(BillingDTO dto) {

        BillingValidationUtil.validateInvoiceNumberFormat(dto.getInvoiceNumber());
        billingValidationUtil.validateAmount(dto.getAmount());
        billingValidationUtil.validateUsername(dto.getCustomerName());
        billingValidationUtil.checkInvoiceExistOrNot(dto.getInvoiceNumber());




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

    @Override
    public List<BillingDTO> findAll(String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        return billingRepository.findAll(sort)
                .stream()
                .map(b -> BillingDTO.builder()
                        .invoiceNumber(b.getInvoiceNumber())
                        .customerName(b.getCustomerName())
                        .amount(b.getAmount())
                        .durationSinceCreated(DateTimeUtil.calculateDuration(b.getCreatedAt()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
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

    @Override
    public BillingDTO update(String invoiceNumber, BillingDTO dto) {

        billingValidationUtil.validateAmount(dto.getAmount());
        billingValidationUtil.validateUsername(dto.getCustomerName());
        Billing billing = billingRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new InvalidInvoiceException("Billing record not found for invoice number: " + invoiceNumber));

        billing = Billing.builder()
                .id(billing.getId())
                .invoiceNumber(billing.getInvoiceNumber())
                .customerName(dto.getCustomerName())
                .amount(dto.getAmount())
                .createdAt(billing.getCreatedAt())
                .build();

        billingRepository.save(billing);

        return BillingDTO.builder()
                .invoiceNumber(billing.getInvoiceNumber())
                .customerName(billing.getCustomerName())
                .amount(billing.getAmount())
                .durationSinceCreated(DateTimeUtil.calculateDuration(LocalDateTime.now()))
                .build();
    }

    @Override
    public String delete(String invoiceNumber) {
        // First, find the billing record
        Billing billing = billingRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new InvalidInvoiceException("Billing record not found for invoice number: " + invoiceNumber));

        // Delete the found record
        billingRepository.delete(billing);

        // Return the deleted record's details as confirmation
        return "Billing Data with InvoiceNumber "+billing.getInvoiceNumber()+" is Deleted Successfully ";
    }

}