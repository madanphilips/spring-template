package com.example.springtemplate.billing.service;

import com.example.springtemplate.billing.dto.BillingDTO;
import org.springframework.stereotype.Component;

import java.util.List;


public interface Iservices {

     BillingDTO create(BillingDTO dto);
     List<BillingDTO> findAll(String sortBy, String direction);
     BillingDTO getBillingByInvoiceNumber(String invoiceNumber);
     BillingDTO update(String invoiceNumber, BillingDTO dto);
     String delete(String invoiceNumber);
}
