package com.example.springtemplate.billing.service;


import com.example.springtemplate.billing.dto.BillingDTO;
import com.example.springtemplate.billing.entity.Billing;
import com.example.springtemplate.billing.exception.InvalidInvoiceException;
import com.example.springtemplate.billing.repository.BillingRepository;
import com.example.springtemplate.billing.util.BillingValidationUtil;
import com.example.springtemplate.common.exception.ResourceNotFoundException;
import com.example.springtemplate.common.util.DateTimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillingService {

    private final BillingRepository billingRepository;


    public BillingService(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    public BillingDTO create(BillingDTO dto, String invoiceNumber) {
        Billing billing;
        if(invoiceNumber == "") {
            if(billingRepository.findByInvoiceNumber(dto.getInvoiceNumber()) != null) {
                throw new InvalidInvoiceException("Invoice number "+dto.getInvoiceNumber()+" already exists");
            }

            BillingValidationUtil.validateInvoiceNumberFormat(dto.getInvoiceNumber());

            BillingValidationUtil.validAmount(dto.getAmount());

            BillingValidationUtil.validCustomerName(dto.getCustomerName());


            billing = Billing.builder()
                    .invoiceNumber(dto.getInvoiceNumber())
                    .customerName(dto.getCustomerName())
                    .amount(Math.round(dto.getAmount() * 100.0) / 100.0)
                    .createdAt(LocalDateTime.now())
                    .build();
        } else {
            billing = billingRepository.findByInvoiceNumber(invoiceNumber)
                    .orElseThrow(() -> new ResourceNotFoundException("Invoice number not found: " + invoiceNumber));

            BillingValidationUtil.validAmount(dto.getAmount());

            BillingValidationUtil.validCustomerName(dto.getCustomerName());

            billing.setAmount(Math.round(dto.getAmount() * 100.0) / 100.0);
            billing.setCustomerName(dto.getCustomerName());
        }

        Billing savedBilling = billingRepository.save(billing);
        dto.setDurationSinceCreated(DateTimeUtil.calculateDuration(savedBilling.getCreatedAt()));

        return dto;
    }

    public List<BillingDTO> findAll() {
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

    public ResponseEntity<?> Delete(String invoiceNumber) {
        BillingValidationUtil.validateInvoiceNumberFormat(invoiceNumber);

        Billing billing = billingRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice number not found: " + invoiceNumber));

        billingRepository.deleteById(billing.getId());
        return ResponseEntity.ok("Billing record deleted successfully");        
    }

}