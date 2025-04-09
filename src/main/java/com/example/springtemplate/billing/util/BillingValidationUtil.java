package com.example.springtemplate.billing.util;


import com.example.springtemplate.billing.exception.InvalidInvoiceException;

import java.util.regex.Pattern;

public class BillingValidationUtil {

    private static final Pattern INVOICE_PATTERN = Pattern.compile("^INV-\\d{4}-\\d{4}$");


    public static void validateInvoiceNumberFormat(String invoiceNumber) {
        if (!INVOICE_PATTERN.matcher(invoiceNumber).matches()) {
            throw new InvalidInvoiceException("Invalid invoice number format. Expected format: INV-YYYY-SEQ");
        }
    }


    public static void validateCustomerName(String customerName) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new InvalidInvoiceException("Customer name cannot be null or empty");
        }
    }

    public static void validateAmount(Double amount) {
        if (amount == null || amount <= 0) {
            throw new InvalidInvoiceException("Amount must be greater than zero");
        }
    }
}