# Model danych (DB) – System wyszukiwania i zakupu biletów lotniczych

Data: 2025-12-26

## Tabele
**users**(id PK, role, first_name, last_name, email unique, phone, login unique, password_hash, invoice_data)

**airports**(code PK, name, city, country)

**flights**(id PK, flight_number, from_airport_code FK, to_airport_code FK, departure_at, arrival_at, duration_minutes, base_price, currency, baggage_info, change_cancel_rules, available_seats)

**bookings**(id PK, user_id FK, flight_id FK, created_at, status, total_price, currency, extra_baggage, insurance)

**passengers**(id PK, booking_id FK, first_name, last_name, birth_date, document_number)

**payments**(id PK, booking_id FK unique, amount, currency, payment_date, method, status, external_tx_id)

## Relacje
- users 1..* bookings
- flights 1..* bookings
- bookings 1..* passengers
- bookings 0..1 payments
- airports 1..* flights (from/to)
