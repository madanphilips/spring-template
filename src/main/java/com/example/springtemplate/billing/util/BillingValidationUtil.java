package com.example.springtemplate.billing.util;


import com.example.springtemplate.billing.exception.InvalidInvoiceException;
import com.example.springtemplate.billing.repository.BillingRepository;
import com.example.springtemplate.billing.service.ImpServices.BillingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class BillingValidationUtil {

    private static final Pattern INVOICE_PATTERN = Pattern.compile("^INV-\\d{4}-\\d{4}$");

    private final BillingRepository billingRepository;

    private static final Logger log = LogManager.getLogger(BillingService.class);


    public BillingValidationUtil(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }


    public static void validateInvoiceNumberFormat(String invoiceNumber) {
        if (!INVOICE_PATTERN.matcher(invoiceNumber).matches()) {
            throw new InvalidInvoiceException("Invalid invoice number format. Expected format: INV-YYYY-SEQ");
        }
    }

    public  void validateAmount(Double amount) {

        if(amount == null || amount<=0)
        {
            throw new InvalidInvoiceException("Inavil amount please enter valid amount");
        }
    }

    public void validateUsername(String customerName) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new InvalidInvoiceException("Customer name cannot be null or empty");
        }

        if (!customerName.matches("^[a-zA-Z\\s]{3,50}$")) {
            throw new InvalidInvoiceException("Customer name must be 3-50 characters long and contain only letters and spaces");
        }

    }

    public void checkInvoiceExistOrNot(String invoiceNumber)
    {
        if(billingRepository.existsByInvoiceNumber(invoiceNumber)) {
            throw new InvalidInvoiceException("Invoice number already exists: " + invoiceNumber);
        }
    }
}