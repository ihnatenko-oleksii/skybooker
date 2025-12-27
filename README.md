# System wyszukiwania i zakupu biletów lotniczych

System rezerwacji biletów lotniczych zbudowany w architekturze Spring Boot (backend) + React (frontend).

## 📋 Wymagania wstępne

- **Java 17+** (zalecane: Java 17 lub 21)
- **Maven 3.8+**
- **Node.js 18+** i npm
- **Docker** i Docker Compose

## 🚀 Szybki start

### 1. Uruchom bazę danych

```bash
docker-compose up -d
```

Sprawdź, czy PostgreSQL działa:
```bash
docker ps
```

### 2. Uruchom backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Backend będzie dostępny pod adresem: `http://localhost:8080`

### 3. Uruchom frontend

W nowym terminalu:

```bash
cd frontend
npm install
npm run dev
```

Frontend będzie dostępny pod adresem: `http://localhost:5173`

## 📁 Struktura projektu

```
.
├── backend/                 # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/flightbooking/
│   │   │   │   ├── config/      # Konfiguracja (CORS)
│   │   │   │   ├── controller/  # REST controllers
│   │   │   │   ├── dto/         # DTOs
│   │   │   │   ├── entity/      # JPA entities
│   │   │   │   ├── exception/   # Exception handling
│   │   │   │   ├── repository/  # JPA repositories
│   │   │   │   └── service/     # Business logic
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── db/migration/  # Flyway migrations
│   │   └── test/
│   └── pom.xml
├── frontend/                # React frontend
│   ├── src/
│   │   ├── api/            # API client
│   │   ├── components/     # React components
│   │   ├── pages/          # Page components
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── package.json
│   └── vite.config.js
├── docs/                   # Dokumentacja
└── docker-compose.yml      # PostgreSQL
```

## 🔑 Uwierzytelnianie (MVP)

Dla uproszczenia MVP, system używa nagłówka `X-User-Id` do identyfikacji użytkownika.

**Domyślny użytkownik:** ID = 1 (tworzony automatycznie w seed danych)

W środowisku produkcyjnym należy zaimplementować pełne uwierzytelnianie (JWT/OAuth2).

## 🌐 Endpointy API

### Flights
- `GET /api/flights/search?from={IATA}&to={IATA}&date={YYYY-MM-DD}&passengers={n}&travelClass={ECONOMY|BUSINESS}` - Wyszukaj loty
- `GET /api/flights/{id}` - Szczegóły lotu

### Bookings
- `POST /api/bookings` - Utwórz rezerwację
- `GET /api/bookings/me` - Moje rezerwacje
- `GET /api/bookings/{id}` - Szczegóły rezerwacji
- `POST /api/bookings/{id}/cancel` - Anuluj rezerwację

### Payments
- `POST /api/payments/mock` - Płatność mock (SUCCESS/FAIL)

Szczegółowy kontrakt API: [docs/api.md](docs/api.md)

## 🧪 Testowanie

### Testy backend

```bash
cd backend
mvn test
```

### Scenariusz demo (TODO - uzupełnione po implementacji)

1. Wyszukaj loty z WAW do FCO
2. Wybierz lot i zobacz szczegóły
3. Utwórz rezerwację dla 2 pasażerów
4. Wykonaj płatność (SUCCESS)
5. Zobacz listę rezerwacji
6. Anuluj rezerwację

## 📊 Baza danych

**PostgreSQL** (port 5432)
- Database: `flightbooking`
- User: `postgres`
- Password: `postgres`

Migracje zarządzane przez **Flyway**.

Model danych: [docs/db.md](docs/db.md)

## 🛠️ Rozwój

### Flyway migrations

Nowa migracja w `backend/src/main/resources/db/migration/`:
- `V{version}__{description}.sql`

### Hot reload
- Backend: Spring Boot DevTools (automatyczny restart)
- Frontend: Vite HMR (automatyczne odświeżanie)

## ⚠️ Znane ograniczenia

- Brak pełnego systemu uwierzytelniania (tylko X-User-Id header)
- Płatności jako mock (brak integracji z prawdziwym dostawcą)
- Brak panelu administratora w MVP
- Brak walidacji dostępności miejsc przy równoczesnych rezerwacjach
- Brak obsługi dodatków (bagaż, ubezpieczenie) w pełnym zakresie

## 📝 TODO / Roadmap

- [ ] Implementacja JWT authentication
- [ ] Panel administratora (CRUD lotów)
- [ ] Obsługa dodatków: bagaż, ubezpieczenie
- [ ] Zmiana rezerwacji
- [ ] Integracja z zewnętrznym API lotów
- [ ] Testy E2E
- [ ] CI/CD pipeline

## 📚 Dokumentacja

- [Wymagania](docs/requirements.md)
- [Kontrakt API](docs/api.md)
- [Model danych](docs/db.md)
- [Traceability](docs/traceability.md)
- [Postęp implementacji](docs/progress.md)

## 📄 Licencja

Projekt edukacyjny - Inżynieria Oprogramowania II
