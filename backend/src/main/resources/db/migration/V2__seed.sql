-- V2: Seed data for testing and demo

-- Default Client user
--  invoice_data_id null.
INSERT INTO users (id, user_type, first_name, last_name, email, phone, login, password_hash, invoice_data_id) 
VALUES (1, 'CLIENT', 'Jan', 'Kowalski', 'jan.kowalski@example.com', '+48123456789', 'jan.kowalski', 
        '$2a$10$dummyhash', NULL);

-- Admin user
-- Login: admin, Password: admin
INSERT INTO users (id, user_type, first_name, last_name, email, phone, login, password_hash, invoice_data_id) 
VALUES (2, 'ADMIN', 'Admin', 'Global', 'admin@example.com', '+48987654321', 'admin', 
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', NULL);

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
('LO3925', 'WAW', 'FCO', '2026-02-11 10:00:00', '2026-02-11 12:10:00', 130, 1499.99, 'PLN', 30, 'BUSINESS', 'Bagaż podręczny: 12kg, Bagaż rejestrowany: 32kg', 'Zmiana biletu: bezpłatna, Anulowanie: 80% zwrotu do 24h przed lotem'),

-- Additional routes - WAW -> BCN
('LO351', 'WAW', 'BCN', '2026-02-13 08:00:00', '2026-02-13 10:45:00', 165, 599.99, 'PLN', 155, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 150 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),
('FR1234', 'WAW', 'BCN', '2026-02-13 19:30:00', '2026-02-13 22:15:00', 165, 549.99, 'PLN', 140, 'ECONOMY', 'Bagaż podręczny: 10kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 180 PLN, Anulowanie: 40% zwrotu do 10 dni przed lotem'),

-- WAW -> MUC
('LO355', 'WAW', 'MUC', '2026-02-15 09:30:00', '2026-02-15 11:00:00', 90, 429.99, 'PLN', 160, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 150 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),
('LH1523', 'WAW', 'MUC', '2026-02-15 17:00:00', '2026-02-15 18:30:00', 90, 449.99, 'PLN', 175, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 150 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),

-- WAW -> BUD
('LO371', 'WAW', 'BUD', '2026-02-17 12:00:00', '2026-02-17 13:15:00', 75, 349.99, 'PLN', 125, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 120 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),

-- Return flights - FCO -> WAW
('LO3924', 'FCO', 'WAW', '2026-02-16 10:00:00', '2026-02-16 12:10:00', 130, 519.99, 'PLN', 135, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 150 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),
('LO3926', 'FCO', 'WAW', '2026-02-16 18:00:00', '2026-02-16 20:10:00', 130, 499.99, 'PLN', 145, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 150 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),

-- LHR -> WAW
('BA851', 'LHR', 'WAW', '2026-02-14 10:00:00', '2026-02-14 13:20:00', 200, 679.99, 'PLN', 170, 'ECONOMY', 'Bagaż podręczny: 10kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 200 PLN, Anulowanie: 30% zwrotu do 14 dni przed lotem'),
('LO282', 'LHR', 'WAW', '2026-02-14 16:30:00', '2026-02-14 19:50:00', 200, 649.99, 'PLN', 155, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 150 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),

-- CDG -> WAW
('LO332', 'CDG', 'WAW', '2026-02-16 11:00:00', '2026-02-16 13:20:00', 140, 559.99, 'PLN', 165, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 150 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),
('AF1048', 'CDG', 'WAW', '2026-02-16 20:00:00', '2026-02-16 22:20:00', 140, 589.99, 'PLN', 180, 'ECONOMY', 'Bagaż podręczny: 12kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 180 PLN, Anulowanie: 40% zwrotu do 10 dni przed lotem'),

-- FRA -> WAW
('LH1349', 'FRA', 'WAW', '2026-02-18 10:00:00', '2026-02-18 11:40:00', 100, 469.99, 'PLN', 190, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 150 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),
('LO348', 'FRA', 'WAW', '2026-02-18 19:00:00', '2026-02-18 20:40:00', 100, 489.99, 'PLN', 170, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 150 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),

-- AMS -> WAW
('KL1360', 'AMS', 'WAW', '2026-02-20 14:00:00', '2026-02-20 16:05:00', 125, 539.99, 'PLN', 150, 'ECONOMY', 'Bagaż podręczny: 12kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 170 PLN, Anulowanie: 45% zwrotu do 7 dni przed lotem'),

-- MAD -> WAW
('IB3155', 'MAD', 'WAW', '2026-02-22 11:00:00', '2026-02-22 14:30:00', 210, 719.99, 'PLN', 140, 'ECONOMY', 'Bagaż podręczny: 10kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 200 PLN, Anulowanie: 30% zwrotu do 14 dni przed lotem'),

-- VIE -> WAW
('OS612', 'VIE', 'WAW', '2026-02-24 16:00:00', '2026-02-24 17:15:00', 75, 399.99, 'PLN', 120, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 120 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),

-- PRG -> WAW
('LO532', 'PRG', 'WAW', '2026-02-26 11:00:00', '2026-02-26 12:05:00', 65, 339.99, 'PLN', 105, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 100 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),

-- BCN -> WAW
('LO352', 'BCN', 'WAW', '2026-02-19 12:00:00', '2026-02-19 14:45:00', 165, 619.99, 'PLN', 145, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 150 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),

-- MUC -> WAW
('LO356', 'MUC', 'WAW', '2026-02-21 11:00:00', '2026-02-21 12:30:00', 90, 439.99, 'PLN', 150, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 150 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),

-- BUD -> WAW
('LO372', 'BUD', 'WAW', '2026-02-23 14:00:00', '2026-02-23 15:15:00', 75, 359.99, 'PLN', 115, 'ECONOMY', 'Bagaż podręczny: 8kg, Bagaż rejestrowany: 23kg', 'Zmiana biletu: 120 PLN, Anulowanie: 50% zwrotu do 7 dni przed lotem'),

-- Additional Business class flights
('LO3927', 'WAW', 'FCO', '2026-02-12 14:00:00', '2026-02-12 16:10:00', 130, 1599.99, 'PLN', 25, 'BUSINESS', 'Bagaż podręczny: 12kg, Bagaż rejestrowany: 32kg', 'Zmiana biletu: bezpłatna, Anulowanie: 80% zwrotu do 24h przed lotem'),
('BA851B', 'LHR', 'WAW', '2026-02-15 14:00:00', '2026-02-15 17:20:00', 200, 1899.99, 'PLN', 20, 'BUSINESS', 'Bagaż podręczny: 12kg, Bagaż rejestrowany: 32kg', 'Zmiana biletu: bezpłatna, Anulowanie: 80% zwrotu do 24h przed lotem'),

-- Additional routes - Cross connections
('AF1049', 'CDG', 'FCO', '2026-02-17 08:00:00', '2026-02-17 10:15:00', 135, 450.00, 'EUR', 160, 'ECONOMY', 'Hand luggage: 12kg, Checked: 23kg', 'Change fee: 50 EUR, Cancellation: 50% refund up to 7 days'),
('LH1530', 'FRA', 'LHR', '2026-02-19 09:00:00', '2026-02-19 10:15:00', 75, 420.00, 'EUR', 170, 'ECONOMY', 'Hand luggage: 8kg, Checked: 23kg', 'Change fee: 50 EUR, Cancellation: 50% refund up to 7 days'),
('KL1370', 'AMS', 'CDG', '2026-02-21 10:00:00', '2026-02-21 11:30:00', 90, 380.00, 'EUR', 155, 'ECONOMY', 'Hand luggage: 12kg, Checked: 23kg', 'Change fee: 50 EUR, Cancellation: 50% refund up to 7 days');

-- Reset sequence for users table to continue from id > 2
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
