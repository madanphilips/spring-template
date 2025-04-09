package com.example.springtemplate.billing.util;


import com.example.springtemplate.billing.exception.InvalidInvoiceException;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class BillingValidationUtil {

    private static final Pattern INVOICE_PATTERN = Pattern.compile("^INV-\\d{4}-\\d{4}$");


    public static void validateInvoiceNumberFormat(String invoiceNumber) {
        if (!INVOICE_PATTERN.matcher(invoiceNumber).matches()) {
            throw new InvalidInvoiceException("Invalid invoice number format. Expected format: INV-YYYY-SEQ");
        } 
        else if (invoiceNumber == null || invoiceNumber.isBlank()) {
            throw new InvalidInvoiceException("Invoice number cannot be blank");
        }
    }

    public static void validAmount(Double amount) {
        if (amount < 0) {
            throw new InvalidInvoiceException("Amount cannot be negative");
        } else if (amount == null || amount == 0 || Double.isInfinite(amount)) {
            throw new InvalidInvoiceException("Amount cannot be zero");
        }
        BigDecimal bd = BigDecimal.valueOf(amount);
        if (bd.precision() - bd.scale() > 9 || bd.scale() > 2) {
            throw new InvalidInvoiceException("Amount must have max 7 digits before and 2 after decimal");
        }
    }

    public static void validCustomerName(String customerName) {
        if (customerName == null || customerName == "" ||customerName.isBlank()) {
            throw new InvalidInvoiceException("Customer name cannot be blank");
        } else if(customerName.matches(".*\\d.*")) {
            throw new InvalidInvoiceException("Customer name cannot contain numbers");
        }
    }

}