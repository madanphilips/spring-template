package com.example.springtemplate.billing.service;


import com.example.springtemplate.billing.dto.BillingDTO;
import com.example.springtemplate.billing.entity.Billing;
import com.example.springtemplate.billing.exception.InvalidInvoiceException;
import com.example.springtemplate.billing.repository.BillingRepository;
import com.example.springtemplate.billing.util.BillingValidationUtil;
import com.example.springtemplate.common.exception.ResourceNotFoundException;
import com.example.springtemplate.common.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillingService {

    private final BillingRepository billingRepository;

    @Autowired
    private BillingValidationUtil billingValidationUtil;

    public BillingService(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    public BillingDTO create(BillingDTO dto) {

        BillingValidationUtil.validateInvoiceNumberFormat(dto.getInvoiceNumber());
        BillingValidationUtil.customerNameCheck(dto.getCustomerName());


        /*-------------------Validate Amount---------------------------------*/
        billingValidationUtil.validateAmount(dto.getAmount());
//        BillingValidationUtil.validateAmount(dto.getAmount());


        /*-------------------Invoice number already exists---------------------*/
        boolean invoiceExists = billingRepository.existsByInvoiceNumber(dto.getInvoiceNumber());
        BillingValidationUtil.checkInvoiceNumberExists(invoiceExists);

//if (billingRepository.existsByInvoiceNumber(dto.getInvoiceNumber())){
//    throw new InvalidInvoiceException("Invoice number already exists");
//}
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

    public BillingDTO update(String invoiceNumber, BillingDTO dto) {
        BillingValidationUtil.validateInvoiceNumberFormat(invoiceNumber);
        BillingValidationUtil.customerNameCheck(dto.getCustomerName());

        /*-------------------Invoice number already exists---------------------*/
        boolean invoiceExists = billingRepository.existsByInvoiceNumber(dto.getInvoiceNumber());
        BillingValidationUtil.checkInvoiceNumberExists(invoiceExists);

        /*-------------------Validate Amount---------------------------------*/
        billingValidationUtil.validateAmount(dto.getAmount());
//        BillingValidationUtil.validateAmount(dto.getAmount());


        Billing billing = billingRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice number not found: " + invoiceNumber));

        billing.setCustomerName(dto.getCustomerName());
        billing.setAmount(dto.getAmount());

        Billing updatedBilling = billingRepository.save(billing);

        return BillingDTO.builder()
                .invoiceNumber(updatedBilling.getInvoiceNumber())
                .customerName(updatedBilling.getCustomerName())
                .amount(updatedBilling.getAmount())
                .durationSinceCreated(DateTimeUtil.calculateDuration(updatedBilling.getCreatedAt()))
                .build();
    }

    public String delete(String invoiceNumber) {


        Billing billing = billingRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice number not found: " + invoiceNumber));

        billingRepository.delete(billing);
        return  "Deleted Successfully : " + invoiceNumber;
    }



}