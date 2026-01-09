package com.flightbooking.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal; // Assuming 'należność: Płatność' means the amount or link to payment. Usually amount.

@Entity
@Table(name = "invoice_data")
public class InvoiceData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "nip")
    private String nip; // Optional (int?) in diagram, usually String

    @Column(name = "address")
    private String address;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "sale_date")
    private LocalDate saleDate;

    // "produkty: List<Rezerwacja>"
    @OneToMany
    @JoinTable(name = "invoice_reservations", joinColumns = @JoinColumn(name = "invoice_id"), inverseJoinColumns = @JoinColumn(name = "booking_id"))
    private List<Booking> products;

    // "należność: Płatność" - implies association with Payment or Amount.
    // Given Invoice usually has a total amount, and Payment is separate.
    // However, diagram says 'należność' type is 'Płatność'.
    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    /**
     * Generuje fakturę.
     * UML: DaneDoFaktury.WygenerujFakture()
     * Zwraca fakturę jako sformatowany tekst (format HTML/tekstowy dla MVP).
     */
    public String generateInvoice() {
        StringBuilder invoice = new StringBuilder();
        invoice.append("=== FAKTURA ===\n\n");
        
        if (companyName != null) {
            invoice.append("Firma: ").append(companyName).append("\n");
        }
        if (nip != null) {
            invoice.append("NIP: ").append(nip).append("\n");
        }
        if (address != null) {
            invoice.append("Adres: ").append(address).append("\n");
        }
        if (issueDate != null) {
            invoice.append("Data wystawienia: ").append(issueDate).append("\n");
        }
        if (saleDate != null) {
            invoice.append("Data sprzedaży: ").append(saleDate).append("\n");
        }
        
        invoice.append("\n=== PRODUKTY ===\n");
        if (products != null && !products.isEmpty()) {
            for (Booking booking : products) {
                invoice.append("Rezerwacja #").append(booking.getId())
                       .append(" - ").append(booking.getTotalPrice())
                       .append(" ").append(booking.getCurrency()).append("\n");
            }
        }
        
        if (payment != null) {
            invoice.append("\n=== PŁATNOŚĆ ===\n");
            invoice.append("Kwota: ").append(payment.getAmount())
                   .append(" ").append(payment.getCurrency()).append("\n");
            invoice.append("Status: ").append(payment.getStatus()).append("\n");
        }
        
        return invoice.toString();
    }

    // Gettery i Settery
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public List<Booking> getProducts() {
        return products;
    }

    public void setProducts(List<Booking> products) {
        this.products = products;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
