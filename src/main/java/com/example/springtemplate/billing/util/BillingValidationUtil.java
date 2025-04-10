package com.example.springtemplate.billing.util;


import com.example.springtemplate.billing.exception.InvalidInvoiceException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class BillingValidationUtil {

    private static final Pattern INVOICE_PATTERN = Pattern.compile("^INV-\\d{4}-\\d{4}$");

    /*-------------------Validate Invoice Number---------------------------------*/
    public static void validateInvoiceNumberFormat(String invoiceNumber) {
        if (!INVOICE_PATTERN.matcher(invoiceNumber).matches()) {
            throw new InvalidInvoiceException("Invalid invoice number format. Expected format: INV-YYYY-SEQ");
        }
    }

    /*-------------------Validate Amount----------------------------------------*/
    public void validateAmount(Double amount) {
        if (amount == null || amount <= 0) {
            throw new InvalidInvoiceException("Invalid amount. Amount must be greater than 0.");
        }
    }

    /*-------------------Invoice number already exists---------------------------*/
    public static void checkInvoiceNumberExists(boolean exists) {
        if (exists) {
            throw new InvalidInvoiceException("Invoice number already exists");
        }
    }

    public static void customerNameCheck(String customerName) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new InvalidInvoiceException("Customer name must not be empty");
        }
    }

}