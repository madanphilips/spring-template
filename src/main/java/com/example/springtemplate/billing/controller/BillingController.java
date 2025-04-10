package com.example.springtemplate.billing.controller;

import com.example.springtemplate.billing.dto.BillingDTO;
import com.example.springtemplate.billing.service.ImpServices.BillingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
public class BillingController {
    private static final Logger log = LogManager.getLogger(BillingController.class);
    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping
    public List<BillingDTO> findAll(@RequestParam(defaultValue = "customerName") String sortBy, @RequestParam(defaultValue = "asc") String direction) {

        return billingService.findAll( sortBy, direction);
    }

    @GetMapping("/{invoiceNumber}")
    public BillingDTO getBillingByInvoiceNumber(@PathVariable String invoiceNumber) {
        return billingService.getBillingByInvoiceNumber(invoiceNumber);
    }

    @PostMapping
    public BillingDTO create(@RequestBody BillingDTO dto) {
        return billingService.create(dto);
    }

    @DeleteMapping("/{invoiceNumber}")
    public String deleteByInvoice(@PathVariable String invoiceNumber){
         return  billingService.delete(invoiceNumber);
    }

    @PutMapping("/{invoiceNumber}")
    public BillingDTO updateByInvoice(@PathVariable String invoiceNumber, @RequestBody BillingDTO dto){

        return billingService.update(invoiceNumber,dto);
    }
}