package com.flightbooking.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "extra_baggage")
    private Boolean extraBaggage;

    @Column
    private Boolean insurance;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Passenger> passengers = new ArrayList<>();

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = BookingStatus.PENDING_PAYMENT;
        }
    }

    public Booking() {
    }

    public Booking(Long id, User user, Flight flight, LocalDateTime createdAt, BookingStatus status,
            BigDecimal totalPrice, String currency, Boolean extraBaggage, Boolean insurance, Payment payment) {
        this.id = id;
        this.user = user;
        this.flight = flight;
        this.createdAt = createdAt;
        this.status = status;
        this.totalPrice = totalPrice;
        this.currency = currency;
        this.extraBaggage = extraBaggage;
        this.insurance = insurance;
        this.payment = payment;
    }

    // Metody biznesowe z diagramu

    public void addPassenger(Passenger passenger) {
        passenger.setBooking(this);
        this.passengers.add(passenger);
    }

    /**
     * Dodaje wielu pasażerów do rezerwacji.
     * UML: Rezerwacja.dodajPasazerow(List<Pasazer>): bool
     */
    public boolean addPassengers(List<Passenger> passengers) {
        if (passengers == null || passengers.isEmpty()) {
            return false;
        }
        for (Passenger passenger : passengers) {
            addPassenger(passenger);
        }
        return true;
    }

    public void removePassenger(Passenger passenger) {
        this.passengers.remove(passenger);
        passenger.setBooking(null);
    }

    /**
     * Usuwa pasażera po ID.
     * UML: Rezerwacja.usunPasazera(int idPasazera): bool
     */
    public boolean removePassengerById(Long passengerId) {
        Passenger passenger = this.passengers.stream()
                .filter(p -> p.getId().equals(passengerId))
                .findFirst()
                .orElse(null);
        
        if (passenger != null) {
            removePassenger(passenger);
            return true;
        }
        return false;
    }

    public BigDecimal calculatePrice() {
        if (flight != null) {
            BigDecimal pricePerPerson = flight.calculatePrice(flight.getTravelClass(),
                    Boolean.TRUE.equals(extraBaggage), Boolean.TRUE.equals(insurance));
            BigDecimal total = pricePerPerson.multiply(new BigDecimal(passengers.size()));
            this.totalPrice = total;
            return total;
        }
        return BigDecimal.ZERO;
    }

    /**
     * Pobiera całkowitą cenę.
     * UML: Rezerwacja.dajCene(): float
     */
    public BigDecimal getPrice() {
        return this.totalPrice;
    }

    public void addExtraBaggage() {
        this.extraBaggage = true;
        calculatePrice();
    }

    public void addInsurance() {
        this.insurance = true;
        calculatePrice();
    }

    /**
     * Anuluje rezerwację.
     * UML: Rezerwacja.anulujRezerwacje(): bool
     */
    public boolean cancelReservation() {
        if (this.status == BookingStatus.CANCELLED) {
            return false; // Już anulowana
        }
        this.status = BookingStatus.CANCELLED;
        // Logika przywracania miejsc w locie mogłaby być tutaj lub w serwisie
        return true;
    }

    public void cancel() {
        cancelReservation();
    }

    // Gettery i Settery

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BookingStatus getStatus() {
        return status;
    }

    /**
     * Ustawia status rezerwacji z walidacją.
     * UML: Rezerwacja.ustawStatus(status: StatusRezerwacji): bool
     */
    public boolean setStatus(BookingStatus status) {
        if (status == null) {
            return false;
        }
        // Walidacja: nie można zmienić z CANCELLED na inny status
        if (this.status == BookingStatus.CANCELLED && status != BookingStatus.CANCELLED) {
            return false;
        }
        this.status = status;
        return true;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getExtraBaggage() {
        return extraBaggage;
    }

    public void setExtraBaggage(Boolean extraBaggage) {
        this.extraBaggage = extraBaggage;
    }

    public Boolean getInsurance() {
        return insurance;
    }

    public void setInsurance(Boolean insurance) {
        this.insurance = insurance;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }
}
