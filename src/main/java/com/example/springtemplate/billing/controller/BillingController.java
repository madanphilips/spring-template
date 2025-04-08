package com.example.springtemplate.billing.controller;

import com.example.springtemplate.billing.dto.BillingDTO;
import com.example.springtemplate.billing.service.BillingService;
import com.example.springtemplate.billing.util.BillingValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    private final BillingService billingService;


    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping
    public List<BillingDTO> findAll() {
        return billingService.findAll();
    }

    @PostMapping
    public BillingDTO create(@RequestBody BillingDTO dto) {
        return billingService.create(dto);
    }

    @GetMapping("/{invoiceNumber}")
    public BillingDTO getBillingByInvoiceNumber(@PathVariable String invoiceNumber) {
        return billingService.getBillingByInvoiceNumber(invoiceNumber);
    }


    @PutMapping("/{invoiceNumber}")
    public BillingDTO update(@PathVariable String invoiceNumber, @RequestBody BillingDTO dto) {
        return billingService.update(invoiceNumber, dto);
    }

    @DeleteMapping("/{invoiceNumber}")
    public String delete(@PathVariable String invoiceNumber) {
        return billingService.delete(invoiceNumber);

    }

//    @DeleteMapping("/{invoiceNumber}")
//    public String deleteInvoice(@PathVariable String invoiceNumber) {
//        String result = billingService.delete(invoiceNumber);
//        return result;  // Return success message
//    }



}