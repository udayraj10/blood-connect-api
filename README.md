# Blood Connect API

A RESTful API connecting blood donors with seekers in real-time, enabling efficient blood donation matching and
management.

---

## Key Features

- **Donor/Seeker Registration & Matching**: User registration with blood group and location-based matching for optimal
  donor-seeker connections
- **JWT Authentication**: Secure token-based authentication with configurable expiry and refresh capabilities
- **Role-Based Access Control (RBAC)**: Two-tier role system:
    - **USER**: Can request blood, post donations, and manage offers
    - **ADMIN**: Full platform oversight and user management capabilities
- **Blood Request Management**: Create, track, and fulfill blood requests with urgency levels and status updates
- **Donation Offer Management**: Users can offer blood donations with availability and compatibility tracking
- **Centralized Error Handling**: Standardized API response format with custom exception handling
- **Swagger UI Documentation**: Interactive API documentation accessible via `/docs`
- **H2 Database Support**: In-memory database for development and testing with automatic schema initialization

---

## Tech Stack

### Backend Framework

- **Spring Boot 3.5.14**: Modern Java web framework
- **Spring Security 6.x**: Authentication and authorization
- **Spring Data JPA**: Object-relational mapping with Hibernate

### Database

- **H2 Database**: In-memory database for development (can be swapped with PostgreSQL/MySQL for production)

### API Documentation & Validation

- **SpringDoc OpenAPI 2.8.5**: OpenAPI 3.0 documentation generator
- **Swagger UI**: Interactive API documentation
- **Jakarta Validation**: Bean validation with custom constraints

### Security

- **JWT (JSON Web Tokens) 0.12.6**: JJWT library for token generation and validation
- **JJWT API, Implementation & Jackson**: Complete JWT support stack

### Development Tools

- **Lombok**: Reduce boilerplate code
- **Maven**: Build and dependency management

---

## Prerequisites

Before you begin, ensure you have the following installed:

- **JDK 17** or higher ([Download](https://www.oracle.com/java/technologies/downloads/#java17))
- **Maven 3.6+** (comes with the project as `mvnw`)
- **Git** (for cloning the repository)
- **Postman or cURL** (for testing API endpoints)

### Optional

- **PostgreSQL 13+** or **MySQL 8.0+** (for production deployment)

---

## Quick Start / Installation

Follow these steps to set up and run the application locally:

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/blood-connect.git
cd blood-connect
```

### 2. Configure the Database (Optional)

By default, the application uses an in-memory H2 database. To use PostgreSQL or MySQL,
update `src/main/resources/application.properties`:

```properties
# For PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/blood_connect
spring.datasource.username=postgres
spring.datasource.password=yourpassword
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL10Dialect
# For MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/blood_connect
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

### 3. Build the Application

```bash
./mvnw clean package
```

### 4. Run the Application

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

---

## API Documentation

### Swagger UI

Once the application is running, access the interactive API documentation at:

**[http://localhost:8080/docs](http://localhost:8080/docs)**

### OpenAPI JSON Definition

The raw OpenAPI specification is available at:

**[http://localhost:8080/api-json](http://localhost:8080/api-json)**

### H2 Database Console (Development Only)

Access the embedded H2 database console at:

**[http://localhost:8080/h2-console](http://localhost:8080/h2-console)**

- JDBC URL: `jdbc:h2:mem:testdb`
- User: `sa`
- Password: (leave blank)

---

## API Endpoints Overview

### Authentication (Public)

| Method | Endpoint             | Description                         |
|--------|----------------------|-------------------------------------|
| POST   | `/api/auth/register` | Register a new user (User role)     |
| POST   | `/api/auth/login`    | Authenticate and retrieve JWT token |

### Users (Protected - USER role)

| Method | Endpoint                     | Description                             |
|--------|------------------------------|-----------------------------------------|
| GET    | `/api/users/me`              | Get current user profile details        |
| PATCH  | `/api/users/me`              | Update current user profile information |
| PATCH  | `/api/users/me/availability` | Update user blood donation availability |
| PATCH  | `/api/users/me/password`     | Change user password                    |
| PATCH  | `/api/users/me/deactivate`   | Deactivate current user account         |
| GET    | `/api/users/me/stats`        | Get current user statistics             |

### Blood Requests (Protected - USER role)

| Method | Endpoint                                 | Description                                        |
|--------|------------------------------------------|----------------------------------------------------|
| POST   | `/api/blood-requests/`                   | Create a new blood request                         |
| GET    | `/api/blood-requests/`                   | Get all blood requests (paginated)                 |
| GET    | `/api/blood-requests/{requestId}`        | Get specific blood request details                 |
| GET    | `/api/blood-requests/{requestId}/donors` | Get matched donors for a blood request (paginated) |
| PATCH  | `/api/blood-requests/{requestId}/cancel` | Cancel a blood request                             |

### Donation Offers (Protected - USER role)

| Method | Endpoint                                   | Description                               |
|--------|--------------------------------------------|-------------------------------------------|
| GET    | `/api/donations/offers`                    | Get available donation offers (paginated) |
| PATCH  | `/api/donations/offers/{offerId}/accept`   | Accept a donation offer                   |
| PATCH  | `/api/donations/offers/{offerId}/decline`  | Decline a donation offer                  |
| PATCH  | `/api/donations/offers/{offerId}/complete` | Complete a donation offer                 |

### Admin (Protected - ADMIN role)

| Method | Endpoint                                       | Description                             |
|--------|------------------------------------------------|-----------------------------------------|
| GET    | `/api/admin/users`                             | Get all users with pagination           |
| GET    | `/api/admin/users/{userId}`                    | Get specific user details by ID         |
| PATCH  | `/api/admin/users/{userId}/deactivate`         | Deactivate a user account               |
| PATCH  | `/api/admin/users/{userId}/activate`           | Activate a deactivated user account     |
| DELETE | `/api/admin/users/{userId}`                    | Permanently delete a user               |
| GET    | `/api/admin/blood-requests`                    | Get all blood requests with pagination  |
| GET    | `/api/admin/blood-requests/{requestId}`        | Get specific blood request details      |
| PATCH  | `/api/admin/blood-requests/{requestId}/cancel` | Cancel any blood request                |
| GET    | `/api/admin/offers`                            | Get all donation offers with pagination |
| GET    | `/api/admin/offers/{offerId}`                  | Get specific donation offer details     |
| GET    | `/api/admin/stats`                             | Get platform statistics and analytics   |

---

## Authentication & Authorization

### JWT Token Structure

Tokens are valid for **24 hours** from generation and include user information and role for authorization checks.

### Role-Based Access Control (RBAC)

The platform implements two-tier role-based access control:

- **USER**:
    - Can register as a blood donor or seeker
    - Can create blood requests and post donation availability
    - Can search for matched donors/recipients
    - Can accept, decline, and complete donation offers
    - Can manage own profile and view personal statistics
    - Can change password and deactivate account

- **ADMIN**:
    - Full access to all user management operations
    - Can view, activate, or deactivate any user account
    - Can delete user accounts permanently
    - Can manage and cancel any blood requests
    - Can view and manage donation offers platform-wide
    - Can access platform statistics and analytics
    - Can monitor all system activities

### Including JWT in Requests

All protected endpoints require the JWT token in the `Authorization` header:

```bash
curl -H "Authorization: Bearer YOUR_JWT_TOKEN" http://localhost:8080/api/users/me
```

---

## Troubleshooting

### Common Issues

**Issue:** Application fails to start

- **Solution:** Ensure JDK 17+ is installed. Run `java -version` to verify.

**Issue:** Port 8080 is already in use

- **Solution:** Change the port in `application.properties`:
  ```properties
  server.port=8081
  ```

**Issue:** Database connection errors

- **Solution:** Verify database credentials in `application.properties` and ensure the database server is running.

**Issue:** JWT token expired

- **Solution:** Generate a new token by logging in again with `/api/auth/login`.

---

## Development Guidelines

### Code Structure

- `/controller` - REST API endpoints
- `/service` - Business logic layer
- `/repository` - Database access layer
- `/entity` - JPA entities
- `/dto` - Data Transfer Objects (requests/responses)
- `/security` - JWT and authentication logic
- `/exception` - Custom exception handling
- `/enums` - Application enumerations
- `/config` - Spring configuration beans

### Database Schema

The application uses SQL scripts to initialize the database:

- `schema.sql` - Table creation and structure
- `import.sql` - Sample data for testing

---

## Changelog

### Version 0.0.1 (Initial Release)

- User registration and authentication
- Blood request creation and management
- Donation offer management
- Donor search and matching
- Admin dashboard and statistics
- JWT-based security

---

**Happy Coding! 🩸❤️**
