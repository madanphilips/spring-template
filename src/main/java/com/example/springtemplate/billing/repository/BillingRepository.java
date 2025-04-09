package com.example.springtemplate.billing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springtemplate.billing.entity.Billing;

public interface BillingRepository extends JpaRepository<Billing, Long> {

    boolean existsByInvoiceNumber(String invoiceNumber);

    Optional<Billing> findByInvoiceNumber(String invoiceNumber);  

}
