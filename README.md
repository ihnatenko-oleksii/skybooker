# Flight Ticket Search and Purchase System

Flight ticket reservation system built with Spring Boot (backend) + React (frontend).

## 📋 Prerequisites

- **Java 21+**
- **Maven 3.8+**
- **Node.js 18+** and npm
- **Docker** and Docker Compose

## 🚀 Quick start

### 1. Start the database

```bash
docker-compose up -d
```

Check that PostgreSQL is running:
```bash
docker ps
```

### 2. Start the backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Backend will be available at: `http://localhost:8080`

### 3. Start the frontend

In a new terminal:

```bash
cd frontend
npm install
npm run dev
```

Frontend will be available at: `http://localhost:5173`

## 📁 Project structure

```
.
├── backend/                 # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/flightbooking/
│   │   │   │   ├── config/      # Configuration (CORS)
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
├── docs/                   # Documentation
└── docker-compose.yml      # PostgreSQL
```

## 🔑 Authentication (JWT)

The system uses JWT-based authentication.

- `POST /api/auth/register` to create an account
- `POST /api/auth/login` to get a token
- Send `Authorization: Bearer <token>` for protected endpoints (bookings, payments, etc.)

## 🌐 API endpoints

### Auth
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and receive JWT

### Flights
- `GET /api/flights/search?from={IATA}&to={IATA}&date={YYYY-MM-DD}&passengers={n}&travelClass={ECONOMY|BUSINESS}` - Search flights
- `GET /api/flights/{id}` - Flight details

### Bookings
- `POST /api/bookings` - Create a booking (requires auth)
- `GET /api/bookings/me` - My bookings (requires auth)
- `GET /api/bookings/{id}` - Booking details
- `POST /api/bookings/{id}/cancel` - Cancel a booking

### Payments
- `POST /api/payments/mock` - Mock payment (SUCCESS/FAIL, requires auth)

Detailed API contract: [docs/api.md](docs/api.md)

## 🧪 Testing

### Backend tests

```bash
cd backend
mvn test
```

### Demo scenario (TODO - to be filled after implementation)

1. Search flights from WAW to FCO
2. Select a flight and view details
3. Create a booking for 2 passengers
4. Make a payment (SUCCESS)
5. View the list of bookings
6. Cancel the booking

## 📊 Database

**PostgreSQL** (port 5432)
- Database: `flightbooking`
- User: `postgres`
- Password: `postgres`

Migrations managed by **Flyway**.

Data model: [docs/db.md](docs/db.md)

## 🛠️ Development

### Flyway migrations

New migration in `backend/src/main/resources/db/migration/`:
- `V{version}__{description}.sql`



## 📚 Documentation

- [Requirements](docs/requirements.md)
- [API contract](docs/api.md)
- [Data model](docs/db.md)
- [Traceability](docs/traceability.md)
- [Implementation progress](docs/progress.md)

## 📄 License

Educational project - Software Engineering II
