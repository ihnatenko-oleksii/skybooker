# Kontrakt API (MVP) – System wyszukiwania i zakupu biletów lotniczych

Data: 2025-12-26

## Konwencje
- Base URL: `/api`
- JSON, UTF-8
- Daty: `YYYY-MM-DD`, daty+czas: ISO-8601 (np. `2026-01-10T12:30:00Z`)
- BookingStatus: `PENDING_PAYMENT`, `PAID`, `PAYMENT_FAILED`, `CANCELLED`
- PaymentStatus: `NEW`, `CONFIRMED`, `REJECTED`

---

## 1) Flights

### 1.1 Wyszukiwanie lotów
`GET /api/flights/search?from={IATA}&to={IATA}&date={YYYY-MM-DD}&passengers={n}&travelClass={ECONOMY|BUSINESS}`

**200**
```json
[
  {
    "id": 123,
    "flightNumber": "LO3921",
    "fromAirport": "WAW",
    "toAirport": "FCO",
    "departureAt": "2026-01-10T09:00:00Z",
    "arrivalAt": "2026-01-10T11:10:00Z",
    "durationMinutes": 130,
    "price": 499.99,
    "currency": "PLN",
    "availableSeats": 12
  }
]
```

### 1.2 Szczegóły lotu
`GET /api/flights/{id}`

---

## 2) Bookings

### 2.1 Utwórz rezerwację
`POST /api/bookings`

**Request**
```json
{
  "flightId": 123,
  "passengers": [
    {
      "firstName": "Jan",
      "lastName": "Kowalski",
      "birthDate": "2000-01-01",
      "documentNumber": "ABC123456"
    }
  ],
  "extras": {
    "extraBaggage": false,
    "insurance": false
  }
}
```

**201**
```json
{
  "id": 9001,
  "status": "PENDING_PAYMENT",
  "totalPrice": 499.99,
  "currency": "PLN"
}
```

### 2.2 Moje rezerwacje (lista)
`GET /api/bookings/me`

### 2.3 Szczegóły rezerwacji
`GET /api/bookings/{id}`

### 2.4 Anuluj rezerwację
`POST /api/bookings/{id}/cancel`

---

## 3) Payments (mock)

### 3.1 Płatność mock
`POST /api/payments/mock`

**Request**
```json
{ "bookingId": 9001, "method": "CARD", "outcome": "SUCCESS" }
```

**200**
```json
{ "paymentId": 5001, "status": "CONFIRMED", "bookingStatus": "PAID" }
```
