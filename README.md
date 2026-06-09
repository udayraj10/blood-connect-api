# BloodConnect API

A real-time blood donation platform connecting donors with patients and hospitals in need.

---

## Key Features

- **User Registration & Authentication**: Any user can register with their basic details,
  blood group, and city. Secure JWT-based login with role-based token claims.

- **Flexible Donor & Requester System**: No fixed roles — every registered user can request
  blood when they need it and donate when they are available. A donor today can be a
  patient tomorrow.

- **Automatic Donor Matching**: When a blood request is created, the system automatically
  finds all available users with the matching blood group in the same city and sends them
  a donation offer — no manual search needed.

- **Donation Offer Lifecycle**: Matched donors receive offers they can accept, decline, or
  complete. Completing an offer automatically marks the blood request as fulfilled — one
  action updates both records in a single transaction.

- **Personal Stats Dashboard**: Every user can view their full activity summary — total
  donations, pending and accepted offers, total requests made, fulfilled and cancelled
  requests, current eligibility status, and next eligible donation date.

- **Role-Based Access Control (RBAC)**: Two-tier role system enforced at both the URL and
  method level:
    - **USER**: Register, request blood, respond to offers, view personal stats and history
    - **ADMIN**: Full platform oversight — manage users, monitor all requests and offers,
      view platform-wide stats, force cancel requests, activate or deactivate accounts

- **Blood Request Management**: Create requests with urgency levels (NORMAL, URGENT,
  CRITICAL), track status in real time (OPEN, FULFILLED, CANCELLED), and view full
  request history.

- **Centralized Exception Handling**: All errors return a consistent JSON structure with
  timestamp, status code, error type, and message. Custom exceptions cover all domain
  scenarios — resource not found, unauthorized access, duplicate registration, ineligible
  donor, and already fulfilled requests.

- **Database Flexibility**: Runs on H2 in-memory database by default — no setup needed.
  Switch to MySQL or PostgreSQL by changing one environment variable.

- **Swagger UI Documentation**: Interactive API documentation available at `/docs`.
  All endpoints documented with request bodies, response schemas, and status codes.

- **Docker Support**: Fully containerized with a single `docker-compose up` command. Starts
  with H2 and pre-loaded dummy data out of the box. Real database credentials passed via
  environment variables — nothing sensitive committed to the repository.

---

## Tech Stack

### Backend Framework

- **Spring Boot 3.x**: Modern Java web framework
- **Spring Security 6.x**: Authentication and authorization
- **Spring Data JPA**: Object-relational mapping with Hibernate

### Database

- **H2 Database**: In-memory database for development (default)
- **MySQL / PostgreSQL**: Supported via profile switch for production

### API Documentation & Validation

- **SpringDoc OpenAPI**: OpenAPI 3.0 documentation generator
- **Swagger UI**: Interactive API documentation at `/docs`
- **Jakarta Validation**: Bean validation with custom constraints

### Security

- **JWT (JJWT 0.12.x)**: Token generation, validation, and role-based claims

### Development Tools

- **Lombok**: Reduce boilerplate code
- **Maven**: Build and dependency management

---

## Prerequisites

Before you begin, ensure you have the following installed:

- **JDK 21** ([Download](https://www.oracle.com/java/technologies/downloads/#java21))
- **Maven 3.6+** (comes with the project as `mvnw`)
- **Git**
- **Docker & Docker Compose** ([Download](https://www.docker.com/products/docker-desktop))
- **Postman or cURL** (for testing API endpoints)

### Optional (for real database)

- **PostgreSQL 13+** or **MySQL 8.0+**

---

## Environment Setup

The application reads secrets and configuration from environment variables.
**Never hardcode credentials in property files.**

### Step 1 — Create your .env file

Copy the provided template:

```bash
cp .env.example .env
```

### Step 2 — Fill in your .env file

```bash
# ==========================================
# REQUIRED — set these before running
# ==========================================

# Active profile: h2 | mysql | postgres
SPRING_PROFILES_ACTIVE=h2

# JWT secret key — use a long random string in production
JWT_SECRET=your-secret-key-here-make-it-long-and-random
```

### Option A - Clone the repository

```bash
git clone [https://github.com/yourusername/bloodconnect.git](https://github.com/yourusername/bloodconnect.git)
cd bloodconnect

# Set up environment
cp .env.example .env
# Edit .env with your JWT_SECRET

# Start the application
docker-compose up
```

The application starts at `http://localhost:8080` with H2 database
and pre-loaded dummy data.

To run in the background:

```bash
docker-compose up -d
```

To stop:

```bash
docker-compose down
```

---

### Option B - Run Locally with Maven

```bash
# Clone the repository
git clone https://github.com/yourusername/bloodconnect.git
cd bloodconnect

# Set up environment
cp .env.example .env
# Edit .env with your JWT_SECRET

# Build the project
./mvnw clean package -DskipTests

# Run the application
./mvnw spring-boot:run
```

---

## Database Profiles

The application supports three database profiles.
Switch between them by changing `SPRING_PROFILES_ACTIVE` in your `.env` file.

| Profile    | Database       | Use Case                          |
|------------|----------------|-----------------------------------|
| `h2`       | H2 In-Memory   | Default — development and testing |
| `mysql`    | MySQL 8+       | Production with MySQL             |
| `postgres` | PostgreSQL 13+ | Production with PostgreSQL        |

### H2 (Default)

```bash
SPRING_PROFILES_ACTIVE=h2
```

No additional setup needed. Schema and dummy data load automatically on startup.

---

## API Documentation

Once the application is running, access the interactive API documentation at:

**[http://localhost:8080/docs](http://localhost:8080/docs)**

Raw OpenAPI JSON:

**[http://localhost:8080/api-json](http://localhost:8080/api-json)**

### H2 Console (H2 profile only)

**[http://localhost:8080/h2-console](http://localhost:8080/h2-console)**

```
JDBC URL  : jdbc:h2:mem:bloodconnect
Username  : sa
Password  : (leave blank)
```

---

## API Endpoints Overview

### Authentication (Public)

| Method | Endpoint             | Description                 |
|--------|----------------------|-----------------------------|
| POST   | `/api/auth/register` | Register a new user         |
| POST   | `/api/auth/login`    | Login and receive JWT token |

### Users (USER role)

| Method | Endpoint                     | Description                  |
|--------|------------------------------|------------------------------|
| GET    | `/api/users/me`              | Get own profile              |
| PATCH  | `/api/users/me`              | Update profile               |
| PATCH  | `/api/users/me/availability` | Toggle donation availability |
| PATCH  | `/api/users/me/password`     | Change password              |
| PATCH  | `/api/users/me/deactivate`   | Deactivate account           |
| GET    | `/api/users/me/stats`        | Get personal stats           |
| GET    | `/api/users/search`          | Search donors                |

### Blood Requests (USER role)

| Method | Endpoint                                 | Description                  |
|--------|------------------------------------------|------------------------------|
| POST   | `/api/blood-requests`                    | Create a blood request       |
| GET    | `/api/blood-requests`                    | Get own requests (paginated) |
| GET    | `/api/blood-requests/{requestId}`        | Get request details          |
| GET    | `/api/blood-requests/{requestId}/donors` | Get matched donors           |
| PATCH  | `/api/blood-requests/{requestId}/cancel` | Cancel a request             |

### Donation Offers (USER role)

| Method | Endpoint                                   | Description                |
|--------|--------------------------------------------|----------------------------|
| GET    | `/api/donations/offers`                    | Get own offers (paginated) |
| PATCH  | `/api/donations/offers/{offerId}/accept`   | Accept an offer            |
| PATCH  | `/api/donations/offers/{offerId}/decline`  | Decline an offer           |
| PATCH  | `/api/donations/offers/{offerId}/complete` | Complete a donation        |

### Admin (ADMIN role)

| Method | Endpoint                                       | Description                  |
|--------|------------------------------------------------|------------------------------|
| GET    | `/api/admin/users`                             | Get all users (paginated)    |
| GET    | `/api/admin/users/{userId}`                    | Get user by ID               |
| PATCH  | `/api/admin/users/{userId}/deactivate`         | Deactivate a user            |
| PATCH  | `/api/admin/users/{userId}/activate`           | Activate a user              |
| DELETE | `/api/admin/users/{userId}`                    | Delete a user                |
| GET    | `/api/admin/blood-requests`                    | Get all requests (paginated) |
| GET    | `/api/admin/blood-requests/{requestId}`        | Get request by ID            |
| PATCH  | `/api/admin/blood-requests/{requestId}/cancel` | Force cancel a request       |
| GET    | `/api/admin/offers`                            | Get all offers (paginated)   |
| GET    | `/api/admin/offers/{offerId}`                  | Get offer by ID              |
| GET    | `/api/admin/stats`                             | Get platform statistics      |

---

## Authentication & Authorization

All protected endpoints require a JWT token in the `Authorization` header:

```bash
Authorization: Bearer YOUR_JWT_TOKEN
```

Tokens are valid for **24 hours**. Generate a new one via `/api/auth/login`.

### Role Permissions

| Action                     | USER | ADMIN |
|----------------------------|------|-------|
| Register and login         | ✅    | ✅     |
| Create blood request       | ✅    | ✅     |
| Respond to offers          | ✅    | ✅     |
| View own profile and stats | ✅    | ✅     |
| View all users             | ❌    | ✅     |
| Manage any request         | ❌    | ✅     |
| View platform stats        | ❌    | ✅     |
| Delete users               | ❌    | ✅     |

---

## Default Admin Account

A default admin account is pre-loaded with the dummy data on startup:

```
Email    : admin1@bloodconnect.com
Password : 000000
```

---

## Code Structure

```
src/main/java/com/yourname/bloodconnect/
├── controller/       REST API endpoints
├── service/          Business logic
├── repository/       Database access
├── domain/           JPA entities
├── dto/
│   ├── request/      Incoming payloads
│   └── response/     Outgoing responses
├── enums/            Application enumerations
├── exception/        Custom exceptions and global handler
├── security/         JWT filter, service, and security config
└── config/           Swagger and audit configuration

src/main/resources/
├── schema.sql                     Table creation
├── import.sql                     Dummy seed data
├── application.properties         Common config and active profile
├── application-h2.properties      H2 specific config
├── application-mysql.properties   MySQL specific config
└── application-postgres.properties PostgreSQL specific config
```

---

## Troubleshooting

| Issue                    | Solution                                                    |
|--------------------------|-------------------------------------------------------------|
| App fails to start       | Ensure JDK 21 is installed — run `java -version`            |
| Port 8080 in use         | Add `server.port=8081` to `application.properties`          |
| JWT error on requests    | Token expired — login again at `/api/auth/login`            |
| H2 console not loading   | Confirm `SPRING_PROFILES_ACTIVE=h2` in your `.env`          |
| Docker build fails       | Ensure Docker Desktop is running before `docker-compose up` |
| Real DB connection error | Verify DB is running and credentials in `.env` are correct  |

---

## Changelog

### Version 0.0.1

- User registration and JWT authentication
- Automatic donor matching by blood group and city
- Donation offer system with accept, decline, and complete flow
- Personal stats dashboard
- Admin panel with user and platform management
- Role-based access control (USER and ADMIN)
- Multi-database support via Spring profiles
- Docker and docker-compose support

---

**Happy Coding! 🩸**