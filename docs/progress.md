# Postęp implementacji – System rezerwacji biletów lotniczych

Data aktualizacji: 2025-12-26

## ✅ Co zostało zrobione

### Checkpoint A: Szkielet projektu (✓ Zakończono)
- Utworzono strukturę projektu z katalogami `backend/`, `frontend/`, `docker-compose.yml`
- Skonfigurowano PostgreSQL w Docker Compose
- Zainicjalizowano projekt Spring Boot z Maven
- Skonfigurowano CORS dla frontendu
- Zainicjalizowano projekt React z Vite
- Utworzono główny README.md z instrukcjami

### Checkpoint B: Model danych i migracje (✓ Zakończono)
- Utworzono encje JPA:
  - `User` - użytkownicy systemu
  - `Airport` - lotniska (kod IATA jako PK)
  - `Flight` - loty z cenami i dostępnością
  - `Booking` - rezerwacje ze statusem
  - `Passenger` - pasażerowie przypisani do rezerwacji
  - `Payment` - płatności powiązane z rezerwacjami
- Zaimplementowano relacje zgodnie z `docs/db.md`
- Dodano migracje Flyway:
  - `V1__init.sql` - schemat bazy danych
  - `V2__seed.sql` - dane testowe (1 użytkownik, 12 lotnisk, 16 lotów)
- Utworzono repozytoria JPA dla wszystkich encji

### Checkpoint C: Backend API (✓ Zakończono)
- Zaimplementowano wszystkie endpointy zgodnie z `docs/api.md`:
  - **Flights**: `GET /api/flights/search`, `GET /api/flights/{id}`
  - **Bookings**: `POST /api/bookings`, `GET /api/bookings/me`, `GET /api/bookings/{id}`, `POST /api/bookings/{id}/cancel`
  - **Payments**: `POST /api/payments/mock`
- Utworzono warstwy aplikacji:
  - **DTO**: Request/Response z walidacją Bean Validation
  - **Service**: Logika biznesowa (FlightService, BookingService, PaymentService)
  - **Controller**: REST endpoints
  - **Exception Handling**: GlobalExceptionHandler z obsługą 404, 400, validation errors
- Napisano testy integracyjne:
  - `FlightControllerIntegrationTest` - testowanie wyszukiwania lotów
  - `BookingFlowIntegrationTest` - testowanie pełnego procesu rezerwacji i płatności

### Checkpoint D: Frontend UI (✓ Zakończono)
- Skonfigurowano React Router
- Utworzono API client (axios) z konfiguracją baseURL i nagłówkiem X-User-Id
- Zaimplementowano strony:
  - **SearchPage** (`/`) - formularz wyszukiwania + lista wyników
  - **FlightDetailsPage** (`/flights/:id`) - szczegóły lotu + przycisk rezerwacji
  - **BookingFormPage** (`/bookings/new`) - formularz danych pasażerów
  - **PaymentPage** (`/payment`) - symulacja płatności (SUCCESS/FAIL)
  - **BookingsListPage** (`/bookings`) - lista rezerwacji użytkownika
- Dodano obsługę stanów loading/error we wszystkich komponentach
- Zaimplementowano nawigację między stronami

### Checkpoint E: Integracja (⏳ W trakcie)
- Backend gotowy do uruchomienia
- Frontend gotowy do uruchomienia
- PostgreSQL w trakcie pobierania obrazu Docker

## 🔄 Co pozostało do zrobienia

### Checkpoint E: Integracja i demo (do dokończenia)
- [ ] Uruchomić PostgreSQL (docker-compose up -d)
- [ ] Uruchomić backend (mvn spring-boot:run)
- [ ] Uruchomić frontend (npm run dev)
- [ ] Przetestować pełny flow użytkownika
- [ ] Dodać szczegółowy scenariusz demo do README

## 🚀 Jak uruchomić

### 1. Baza danych
```bash
docker-compose up -d
```

### 2. Backend
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Backend będzie dostępny pod: `http://localhost:8080`

### 3. Frontend
```bash
cd frontend
npm install
npm run dev
```

Frontend będzie dostępny pod: `http://localhost:5173`

## 📋 Endpointy API (zaimplementowane)

### Flights
- `GET /api/flights/search?from=WAW&to=FCO&date=2026-02-10&passengers=1`
- `GET /api/flights/{id}`

### Bookings
- `POST /api/bookings` (wymaga X-User-Id header, domyślnie: 1)
- `GET /api/bookings/me` (wymaga X-User-Id header)
- `GET /api/bookings/{id}`
- `POST /api/bookings/{id}/cancel`

### Payments
- `POST /api/payments/mock` (body: `{"bookingId": 1, "method": "CARD", "outcome": "SUCCESS"}`)

## ⚠️ Znane ograniczenia i TODO

### MVP - uproszczenia
- **Uwierzytelnianie**: Używamy nagłówka `X-User-Id` zamiast pełnego systemu JWT/OAuth
- **Płatności**: Tylko mock endpoint z parametrem outcome (SUCCESS/FAIL)
- **Walidacja dostępności**: Brak zabezpieczeń przed równoczesnymi rezerwacjami tego samego miejsca
- **Dodatki**: Bagaż i ubezpieczenie są flagami booleańskimi bez logiki cenowej

### Poza MVP (do przyszłej implementacji)
- [ ] Panel administratora (CRUD lotów)
- [ ] Pełna rejestracja i logowanie użytkowników
- [ ] Zmiana rezerwacji (edycja danych pasażerów)
- [ ] Obsługa dodatków z cenami (bagaż, ubezpieczenie)
- [ ] Walidacja konkurencyjna dostępności miejsc
- [ ] Integracja z prawdziwym systemem płatności
- [ ] Testy E2E dla frontendu
- [ ] Dokumentacja API (Swagger/OpenAPI)
- [ ] CI/CD pipeline

## 📊 Statystyki implementacji

- **Pliki backend**: ~30 plików Java (entities, DTOs, services, controllers, repositories, tests)
- **Pliki frontend**: 7 plików (1 API client, 5 stron, 1 App.jsx)
- **Migracje DB**: 2 pliki SQL
- **Dane seed**: 1 użytkownik, 12 lotnisk, 16 lotów
- **Endpointy API**: 8 endpointów REST
- **Testy integracyjne**: 5 testów

## 🎯 Następne kroki

1. Sprawdzić czy PostgreSQL jest uruchomiony
2. Uruchomić backend i sprawdzić logi Flyway (migracje powinny się wykonać automatycznie)
3. Uruchomić testy backend: `mvn test`
4. Uruchomić frontend
5. Przetestować flow:
   - Wyszukanie lotu WAW → FCO
   - Wyświetlenie szczegółów lotu
   - Utworzenie rezerwacji dla 2 pasażerów
   - Płatność SUCCESS
   - Wyświetlenie listy rezerwacji
   - Anulowanie rezerwacji
6. Zaktualizować README z dokładnym scenariuszem demo
7. Utworzenie dokumentu walkthrough.md z screenshotami
