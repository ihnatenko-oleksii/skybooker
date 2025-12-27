-- V2: Seed data for testing and demo

-- Default Client user
-- Note: invoice_data_id is null for now, or create invoice_data first.
-- For simplicity, let's leave invoice_data_id null.
INSERT INTO users (id, user_type, first_name, last_name, email, phone, login, password_hash, invoice_data_id) 
VALUES (1, 'CLIENT', 'Jan', 'Kowalski', 'jan.kowalski@example.com', '+48123456789', 'jan.kowalski', 
        '$2a$10$dummyhash', NULL);

-- Admin user
INSERT INTO users (id, user_type, first_name, last_name, email, phone, login, password_hash, invoice_data_id) 
VALUES (2, 'ADMIN', 'Admin', 'Global', 'admin@example.com', '+48987654321', 'admin', 
        '$2a$10$dummyhash', NULL);

-- Airports
INSERT INTO airports (code, name, city, country) VALUES
('WAW', 'Warsaw Chopin Airport', 'Warsaw', 'Poland'),
('FCO', 'Leonardo da Vinci-Fiumicino Airport', 'Rome', 'Italy'),
('LHR', 'London Heathrow Airport', 'London', 'United Kingdom'),
('CDG', 'Charles de Gaulle Airport', 'Paris', 'France'),
('FRA', 'Frankfurt Airport', 'Frankfurt', 'Germany'),
('AMS', 'Amsterdam Airport Schiphol', 'Amsterdam', 'Netherlands'),
('MAD', 'Adolfo Suárez Madrid-Barajas Airport', 'Madrid', 'Spain'),
('BCN', 'Barcelona-El Prat Airport', 'Barcelona', 'Spain'),
('MUC', 'Munich Airport', 'Munich', 'Germany'),
('VIE', 'Vienna International Airport', 'Vienna', 'Austria'),
('PRG', 'Václav Havel Airport Prague', 'Prague', 'Czech Republic'),
('BUD', 'Budapest Ferenc Liszt International Airport', 'Budapest', 'Hungary');

-- Flights (future dates for testing, using dates relative to 2026)
-- WAW -> FCO
INSERT INTO flights (flight_number, from_airport_code, to_airport_code, departure_at, arrival_at, duration_minutes, base_price, currency, available_seats, travel_class, baggage_info, change_cancel_rules) 
VALUES 
('LO3921', 'WAW', 'FCO', '2026-02-10 09:00:00', '2026-02-10 11:10:00', 130, 499.99, 'PLN', 150, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 150 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),
('LO3923', 'WAW', 'FCO', '2026-02-10 15:30:00', '2026-02-10 17:40:00', 130, 549.99, 'PLN', 120, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 150 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),

-- FCO -> WAW
('LO3922', 'FCO', 'WAW', '2026-02-15 12:00:00', '2026-02-15 14:10:00', 130, 489.99, 'PLN', 140, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 150 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),

-- WAW -> LHR
('BA850', 'WAW', 'LHR', '2026-02-12 07:00:00', '2026-02-12 08:50:00', 170, 649.99, 'PLN', 180, 'ECONOMY', 'Bagaż podręczny: 10kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 200 PLN, Anulowanie: 30% zwrotu do 14 dni przed lotem'),
('LO281', 'WAW', 'LHR', '2026-02-12 13:45:00', '2026-02-12 15:35:00', 170, 599.99, 'PLN', 160, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 150 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),

-- WAW -> CDG
('LO331', 'WAW', 'CDG', '2026-02-14 10:30:00', '2026-02-14 12:50:00', 140, 529.99, 'PLN', 170, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 150 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),
('AF1047', 'WAW', 'CDG', '2026-02-14 18:00:00', '2026-02-14 20:20:00', 140, 579.99, 'PLN', 190, 'ECONOMY', 'Bagaż podręczny: 12kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 180 PLN, Anulowanie: 40% zwrotu do 10 dni przed lotem'),

-- WAW -> FRA
('LH1348', 'WAW', 'FRA', '2026-02-16 08:15:00', '2026-02-16 09:55:00', 100, 459.99, 'PLN', 200, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 150 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),
('LO347', 'WAW', 'FRA', '2026-02-16 16:40:00', '2026-02-16 18:20:00', 100, 479.99, 'PLN', 180, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 150 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),

-- WAW -> AMS
('KL1359', 'WAW', 'AMS', '2026-02-18 11:00:00', '2026-02-18 13:05:00', 125, 519.99, 'PLN', 165, 'ECONOMY', 'Bagaż podręczny: 12kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 170 PLN, Anulowanie: 45% zwrotu do 7 dni przed lotem'),

-- WAW -> MAD
('IB3154', 'WAW', 'MAD', '2026-02-20 06:30:00', '2026-02-20 10:00:00', 210, 699.99, 'PLN', 145, 'ECONOMY', 'Bagaż podręczny: 10kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 200 PLN, Anulowanie: 30% zwrotu do 14 dni przed lotem'),

-- WAW -> VIE
('OS611', 'WAW', 'VIE', '2026-02-22 14:20:00', '2026-02-22 15:35:00', 75, 389.99, 'PLN', 130, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 120 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),

-- WAW -> PRG
('LO531', 'WAW', 'PRG', '2026-02-24 09:15:00', '2026-02-24 10:20:00', 65, 329.99, 'PLN', 110, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 100 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),

-- LHR -> CDG (cross-route)
('BA303', 'LHR', 'CDG', '2026-02-25 07:45:00', '2026-02-25 10:00:00', 75, 450.00, 'EUR', 175, 'ECONOMY', 'Hand luggage: 10kg, Checked: 23kg', 'Change fee: 50 EUR, Cancellation: 50% refund up to 7 days'),

-- Business class flights
('LO3925', 'WAW', 'FCO', '2026-02-11 10:00:00', '2026-02-11 12:10:00', 130, 1499.99, 'PLN', 30, 'BUSINESS', 'Bagaż podręczny: 12kg, Bagaż rejestrowany: 32kg', 'Zmiana biletu: bezpłatna, Anulowanie: 80% zwrotu do 24h przed lotem');

-- Reset sequence for users table to continue from id > 2
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
