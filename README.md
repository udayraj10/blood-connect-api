# BloodConnect API

A real-time blood donation platform built with Spring Boot that connects blood donors with patients through automatic donor matching. The application streamlines the blood donation process by notifying eligible donors when a compatible blood request is created, reducing the time required to find available donors.

---

# Key Features

* JWT-based authentication and authorization
* Automatic donor matching by blood group and city
* Blood request lifecycle management
* Donation offer management
* Personal dashboard with donation statistics
* Role-based access control (USER & ADMIN)
* Interactive Swagger API documentation
* Docker-based deployment
* PostgreSQL database hosted on Neon

---

# Tech Stack

* Java 21
* Spring Boot 3
* Spring Security
* Spring Data JPA (Hibernate)
* PostgreSQL (Neon)
* JWT
* Maven
* Docker & Docker Compose
* SpringDoc OpenAPI (Swagger)

---

# Getting Started

## Prerequisites

Before running the project, install:

* Git
* Docker
* Docker Compose
* Postman for testing the API

---

## Installation

Clone the repository:

```bash
git clone https://github.com/udayraj10/blood-connect-api.git

cd blood-connect-api
```

Create a `.env` file in the project root:

```env
JWT_SECRET=your-secret-key
DB_SOURCE=your-postgresql-url
DB_USERNAME=your-username
DB_PASSWORD=your-password
```

Start the application:

```bash
docker compose up --build
```

Run in detached mode:

```bash
docker compose up -d
```

Stop the application:

```bash
docker compose down
```

---

# Usage

Once the containers are running, the application will be available at:

```
http://localhost:8080
```

---

## API Overview

| Resource | Description |
|----------|-------------|
| Authentication | User registration and login |
| Users | Profile management, availability, and statistics |
| Blood Requests | Create, view, and manage blood requests |
| Donation Offers | Accept, decline, and complete donation offers |
| Admin | User management, request moderation, and platform statistics |

For detailed request and response schemas, visit the Swagger UI after starting the application:

---


## API Documentation

Interactive API documentation is available via Swagger UI:

```
http://localhost:8080/docs
```

OpenAPI Specification:

```
http://localhost:8080/api-json
```
---

# Configuration

The application requires the following environment variables:

| Variable      | Description                                |
| ------------- | ------------------------------------------ |
| `JWT_SECRET`  | Secret key used for JWT generation and validation |
| `DB_SOURCE`   | PostgreSQL JDBC connection URL             |
| `DB_USERNAME` | PostgreSQL username                        |
| `DB_PASSWORD` | PostgreSQL password                        |

---

## Troubleshooting

| Issue                     | Solution                                        |
| ------------------------- | ----------------------------------------------- |
| Docker build fails        | Ensure Docker Desktop is running                |
| Database connection fails | Verify your Neon database credentials in `.env` |
| JWT authentication fails  | Verify `JWT_SECRET` is configured correctly     |
| Port 8080 already in use  | Change the exposed port in `docker-compose.yml` |

---

# License

MIT
