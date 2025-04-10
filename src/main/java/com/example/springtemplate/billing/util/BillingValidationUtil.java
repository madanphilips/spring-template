package com.example.springtemplate.billing.util;


import com.example.springtemplate.billing.exception.AmountException;
import com.example.springtemplate.billing.exception.InvalidInvoiceException;
import com.example.springtemplate.billing.exception.NameException;

import java.util.regex.Pattern;

public class BillingValidationUtil
{
    private static final Pattern INVOICE_PATTERN = Pattern.compile("^INV-\\d{4}-\\d{4}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z\\-']+$");


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
            throw new AmountException("Amount cannot be empty");
        }

        try {
            double value = Double.parseDouble(amount);
            if (value < 0) {
                throw new AmountException("Amount cannot be negative");
            }
        } catch (NumberFormatException e) {
            throw new AmountException("Invalid amount format");
        }
    }

    public static void validateCustomerName(String customerName)
    {
        if (customerName == null || customerName.isEmpty())
        {
            throw new NameException("Name cannot be Empty");
        }
        if (!NAME_PATTERN.matcher(customerName).matches())
        {
            throw new NameException("Invalid customer name format. Only letters, hyphens, and apostrophes are allowed.");
        }
    }

}