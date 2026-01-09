package com.flightbooking.controller;

import com.flightbooking.service.InvoiceService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    /**
     * Generuje fakturę dla rezerwacji.
     * UML: DaneDoFaktury.WygenerujFakture()
     */
    @GetMapping("/booking/{bookingId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> generateInvoiceForBooking(@PathVariable Long bookingId) {
        String invoice = invoiceService.generateInvoiceForBooking(bookingId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "invoice_" + bookingId + ".txt");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(invoice);
    }

    /**
     * Generuje fakturę dla wszystkich rezerwacji klienta.
     * UML: DaneDoFaktury.WygenerujFakture()
     */
    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> generateInvoiceForClient(@PathVariable Long clientId) {
        String invoice = invoiceService.generateInvoiceForClient(clientId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "invoice_client_" + clientId + ".txt");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(invoice);
    }
}
