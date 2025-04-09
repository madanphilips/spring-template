package com.example.springtemplate.billing.util;


import com.example.springtemplate.billing.dto.BillingDTO;
import com.example.springtemplate.billing.exception.InvalidAmountException;
import com.example.springtemplate.billing.exception.InvalidInvoiceException;
import com.example.springtemplate.billing.exception.InvalidNameException;
import com.example.springtemplate.billing.repository.BillingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.regex.Pattern;

public class BillingValidationUtil {


    private static final Pattern INVOICE_PATTERN = Pattern.compile("^INV-\\d{4}-\\d{4}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z\\s\\-']+$");


    public static void validateInvoiceNumberFormat(String invoiceNumber)
    {
        if (!INVOICE_PATTERN.matcher(invoiceNumber).matches())
        {
            throw new InvalidInvoiceException("Invalid invoice number format. Expected format: INV-YYYY-SEQ");
        }

    }

    public static void validateAmount(String amount)
    {
        if (amount == null || amount.isEmpty()) {
            throw new InvalidAmountException("Amount cannot be empty");
        }

        try {
            double value = Double.parseDouble(amount);
            if (value < 0) {
                throw new InvalidAmountException("Amount cannot be negative");
            }
        } catch (NumberFormatException e) {
            throw new InvalidAmountException("Invalid amount format");
        }
    }

    public static void validateCustomerName(String customerName)
    {
        if (customerName == null || customerName.isEmpty())
        {
            throw new InvalidNameException("Name cannot be null");
        }
        if (!NAME_PATTERN.matcher(customerName).matches())
        {
            throw new InvalidNameException("Invalid customer name format. Only letters, spaces, hyphens, and apostrophes are allowed.");
        }
    }




}