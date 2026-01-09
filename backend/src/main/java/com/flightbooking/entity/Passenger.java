package com.flightbooking.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "passengers")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "document_number", nullable = false, length = 50)
    private String documentNumber;


    public Passenger() {
    }

    public Passenger(Long id, Booking booking, String firstName, String lastName, LocalDate birthDate, String documentNumber) {
        this.id = id;
        this.booking = booking;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.documentNumber = documentNumber;
    }

    /**
     * Aktualizuje dane pasażera.
     * UML: Pasazer.ZakutalizujDane(Pasazer): bool
     */
    public boolean updateData(Passenger newData) {
        if (newData == null) {
            return false;
        }
        this.firstName = newData.getFirstName();
        this.lastName = newData.getLastName();
        this.birthDate = newData.getBirthDate();
        this.documentNumber = newData.getDocumentNumber();
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
}
