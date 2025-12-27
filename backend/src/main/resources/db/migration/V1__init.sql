-- V1: Initial schema for flight booking system

-- Invoice Data table (DaneDoFaktury)
CREATE TABLE invoice_data (
    id BIGSERIAL PRIMARY KEY,
    company_name VARCHAR(200),
    nip VARCHAR(20),
    address VARCHAR(255),
    issue_date DATE,
    sale_date DATE
    -- payment_id added later or nullable
);

-- Users table (Single Table Inheritance)
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    user_type VARCHAR(31) NOT NULL, -- Discriminator: CLIENT, ADMIN
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(200) NOT NULL UNIQUE,
    phone VARCHAR(20),
    login VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    invoice_data_id BIGINT REFERENCES invoice_data(id) -- For Client
);

-- Airports table
CREATE TABLE airports (
    code VARCHAR(3) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    city VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL
);

-- Flights table
CREATE TABLE flights (
    id BIGSERIAL PRIMARY KEY,
    flight_number VARCHAR(10) NOT NULL,
    from_airport_code VARCHAR(3) NOT NULL REFERENCES airports(code),
    to_airport_code VARCHAR(3) NOT NULL REFERENCES airports(code),
    departure_at TIMESTAMP NOT NULL,
    arrival_at TIMESTAMP NOT NULL,
    duration_minutes INTEGER NOT NULL,
    base_price DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    baggage_info TEXT,
    change_cancel_rules TEXT,
    available_seats INTEGER NOT NULL,
    travel_class VARCHAR(20)
);

-- Bookings table
CREATE TABLE bookings (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    flight_id BIGINT NOT NULL REFERENCES flights(id),
    created_at TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    extra_baggage BOOLEAN,
    insurance BOOLEAN
);

-- Invoice Reservations Join Table (OneToMany from InvoiceData to Booking)
CREATE TABLE invoice_reservations (
    invoice_id BIGINT NOT NULL REFERENCES invoice_data(id),
    booking_id BIGINT NOT NULL REFERENCES bookings(id),
    PRIMARY KEY (invoice_id, booking_id)
);

-- Passengers table
CREATE TABLE passengers (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL REFERENCES bookings(id),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    birth_date DATE NOT NULL,
    document_number VARCHAR(50) NOT NULL
);

-- Payments table
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL UNIQUE REFERENCES bookings(id),
    amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    payment_date TIMESTAMP,
    method VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    external_tx_id VARCHAR(100)
);

-- Add payment link to invoice_data if needed
ALTER TABLE invoice_data ADD COLUMN payment_id BIGINT REFERENCES payments(id);

-- Indexes for better query performance
CREATE INDEX idx_flights_from_airport ON flights(from_airport_code);
CREATE INDEX idx_flights_to_airport ON flights(to_airport_code);
CREATE INDEX idx_flights_departure ON flights(departure_at);
CREATE INDEX idx_bookings_user ON bookings(user_id);
CREATE INDEX idx_bookings_flight ON bookings(flight_id);
CREATE INDEX idx_bookings_status ON bookings(status);
CREATE INDEX idx_passengers_booking ON passengers(booking_id);
