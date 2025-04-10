package com.example.springtemplate.billing.repository;

import com.example.springtemplate.billing.entity.Billing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BillingRepository extends JpaRepository<Billing, Long> {

    Optional<Billing> findByInvoiceNumber(String invoiceNumber);


    /*------------------Invoice number already exists------------------------*/
    boolean existsByInvoiceNumber(String invoiceNumber);



}