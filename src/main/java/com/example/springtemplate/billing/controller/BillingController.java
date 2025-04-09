package com.example.springtemplate.billing.controller;

import com.example.springtemplate.billing.dto.BillingDTO;
import com.example.springtemplate.billing.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
public class BillingController
{
    private final BillingService billingService;
    @Autowired
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
    public void delete(@PathVariable String invoiceNumber)
    {
        billingService.delete(invoiceNumber);
    }

}
