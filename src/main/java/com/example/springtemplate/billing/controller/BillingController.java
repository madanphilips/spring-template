package com.example.springtemplate.billing.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springtemplate.billing.dto.BillingDTO;
import com.example.springtemplate.billing.service.BillingService;

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

    @GetMapping("/{invoiceNumber}")
    public BillingDTO getBillingByInvoiceNumber(@PathVariable String invoiceNumber) {
        return billingService.getBillingByInvoiceNumber(invoiceNumber);
    }

    @PostMapping
    public BillingDTO create(@RequestBody BillingDTO dto) {
        return billingService.create(dto);
    }


    @PutMapping("/{invoiceNumber}")
    public BillingDTO update(@PathVariable String invoiceNumber, @RequestBody BillingDTO dto) {
        return billingService.update(invoiceNumber, dto);
    }

    @DeleteMapping("/{invoiceNumber}")
    public void delete(@PathVariable String invoiceNumber) {
        billingService.delete(invoiceNumber);
    }

}
