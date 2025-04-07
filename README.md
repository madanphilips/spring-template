# Spring Boot Billing API

A minimal Spring Boot REST API to manage Invoices in a Billing System.

---

## Features

- Create Invoice
- Get Invoice by `invoiceNumber`
- Get All Invoices
- Duration since Invoice created (e.g., "2 days ago", "5 hours ago")
- Input Validation & Exception Handling
- HikariCP Connection Pooling
- MySQL Database
- DTO-based API response mapping
- Clean Architecture (Entity → DTO → Controller → Service → Repository)

---

## Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Data JPA
- MySQL 8.x
- HikariCP
- Lombok
- Maven
- REST API (JSON)

---

## SQL Scripts
Execute the scripts from the `src/main/resources/sql` (in the defined order)

---
## API Endpoints

| Method | Endpoint               | Description                        |
|--------|------------------------|-----------------------------------|
| POST   | `/api/invoices`        | Create a new Invoice              |
| GET    | `/api/invoices`        | Get all Invoices                  |
| GET    | `/api/invoices/{id}`   | Get Invoice by Invoice Number     |

---

## Sample API Response

```json
{
  "invoiceNumber": "INV-2025-0001",
  "amount": 500.00,
  "customerName": "John Doe",
  "durationSinceCreated": "2 days ago"
}
