# BloodConnect API

BloodConnect is a REST API that manages blood donation requests, donor offers, and user accounts. Any registered user can both create blood requests and donate blood to other users. The system automatically identifies eligible users when a new request is created and generates donation offers based on matching criteria

The project demonstrates backend engineering concepts including **JWT Authentication**, **Spring Security**, **Role-Based Access Control (RBAC)**, **Regex-powered search**, **Global Exception Handling**, **REST API design**, **data aggregation**, and **Dockerized deployment**.

---

# Live Demo

| Resource     | URL                                                  |
| ------------ | ---------------------------------------------------- |
| API          | https://blood-connect-api-dvlk.onrender.com          |
| Swagger UI   | https://blood-connect-api-dvlk.onrender.com/docs     |
| OpenAPI JSON | https://blood-connect-api-dvlk.onrender.com/api-json |

---

# Architecture

Instead of manually searching for donors, the system automatically identifies eligible donors when a request is created, generates donation offers, tracks every offer through its lifecycle, and provides administrators with complete visibility into platform activity.

The project was designed around clean REST principles, layered architecture, centralized exception handling, and stateless JWT authentication.

---

## System Architecture

```
                Client
                  │
                  ▼
       Spring Security (JWT)
                  │
                  ▼
          REST Controllers
                  │
                  ▼
          Business Services
                  │
                  ▼
      Spring Data JPA (Hibernate)
                  │
                  ▼
             PostgreSQL
```

The application follows a layered architecture where each layer has a single responsibility.

| Layer      | Responsibility                        |
| ---------- | ------------------------------------- |
| Controller | Exposes REST APIs                     |
| Service    | Implements business rules             |
| Repository | Database access using Spring Data JPA |
| Security   | JWT authentication and authorization  |
| Database   | PostgreSQL persistence                |

---

# Technical Implementation

---

## Authentication & Authorization

The application uses stateless authentication with JSON Web Tokens (JWT).

### Authentication Flow

```
Login
   │
   ▼
Validate Credentials
   │
   ▼
Generate JWT
   │
   ▼
Client Stores Token
   │
   ▼
Authorization Header
Bearer <token>
```

Each incoming request passes through a JWT authentication filter before reaching protected endpoints.

The authenticated user is stored inside the Spring Security Context for the remainder of the request.

---

# Role-Based Access Control (RBAC)

Spring Security enforces authorization at the endpoint level.

Supported roles:

* USER
* ADMIN

Examples:

| Endpoint             | USER | ADMIN |
| -------------------- | ---- | ----- |
| Register/Login       | Yes  | Yes   |
| Create Blood Request | Yes  | Yes   |
| Update Own Profile   | Yes  | Yes   |
| View Dashboard       | Yes  | Yes   |
| Manage Users         | No   | Yes   |
| Platform Analytics   | No   | Yes   |
| Delete Users         | No   | Yes   |

Administrative endpoints are protected using role-based authorization.

Unauthorized users attempting to access privileged APIs receive appropriate HTTP responses:

```
403 Forbidden
```

This prevents privilege escalation while maintaining a clear separation between standard users and administrators.

---

## Request Matching

One of the core backend components is the automated donor matching system.

Whenever a patient creates a blood request, the application immediately begins identifying potential donors.

### Matching Criteria

The matching algorithm evaluates:

* Blood Group compatibility
* Donor city
* Donor availability status
* Active account status

Eligible donors are automatically associated with the request by creating donation offers.

### Workflow

```
Blood Request Created
          │
          ▼
Validate Request
          │
          ▼
Query Eligible Donors
(Blood Group + City + Availability)
          │
          ▼
Generate Donation Offers
          │
          ▼
Store Offers
          │
          ▼
Request Becomes Visible
```

### Engineering Decisions

* Business logic is isolated inside the service layer.
* Matching executes automatically during request creation.
* Database queries retrieve only eligible donors, avoiding unnecessary in-memory filtering.
* The design keeps controllers lightweight while maintaining clear separation of concerns.

---

# User Search

The application supports flexible searching instead of requiring exact text matches.

Users can search using:

* Username
* City
* Blood Group
* Partial text
* Mixed keywords

Examples:

```
mee
Meena
hyderabad
A+
O-
```

Regex and pattern matching enable case-insensitive partial searches without requiring users to enter exact values.

This greatly improves usability compared to exact SQL equality matching.

### Technical Benefits

* Partial string matching
* Case-insensitive search
* Flexible query patterns
* Improved search experience

---

## Graceful Handling of Empty Search Results

Initially, searching by city or blood group could result in a `404 Resource Not Found` exception when no matching users existed.

Instead of treating an empty result set as an application error, the search implementation was redesigned.

### Previous Behavior

```
GET /users/search?query=Delhi

404 Not Found
```

### Improved Behavior

```
GET /users/search?query=Delhi

200 OK

[]
```

This approach aligns with REST best practices:

* Empty search results are valid responses.
* Clients no longer need exception handling for normal search scenarios.
* APIs become predictable and easier to consume.

---

# Administrative Statistics

The administrator dashboard aggregates application-wide metrics directly from the database.

Example response:

```json
{
  "totalUsers": 152,
  "activeUsers": 141,
  "inactiveUsers": 11,
  "availableDonors": 64,
  "openRequests": 12,
  "fulfilledRequests": 38,
  "cancelledRequests": 9,
  "normalRequests": 15,
  "urgentRequests": 8,
  "criticalRequests": 5,
  "groupByAge": {
    "22": 80,
    "18": 45
  },
  "groupByBlood": {
    "O+": 120,
    "A+": 200,
    "AB-": 180
  }
}
```

The analytics layer uses optimized aggregation queries to minimize database round trips while providing administrators with a real-time overview of platform activity.

---

# Error Handling

The project implements centralized exception handling using `@ControllerAdvice`.

Instead of exposing stack traces or framework-generated error pages, all exceptions are converted into consistent JSON responses.

### Handled Exceptions

* Resource Not Found
* Validation Errors
* Authentication Failure
* Authorization Failure
* Duplicate Resources
* Invalid Requests
* Internal Server Errors

Example response:

```json
{
  "status": "error",
  "code": "404",
  "message": "Blood request not found.",
  "timestamp": "2026-07-31T12:45:10"
}
```

Benefits:

* Consistent API responses
* Cleaner client-side error handling
* No stack trace leakage
* Proper HTTP status codes
* Improved debugging

--- 

# REST API Modules

| Module          | Description                       |
| --------------- | --------------------------------- |
| Authentication  | Registration and Login            |
| Users           | Profile, Availability, Statistics |
| Blood Requests  | Request Creation and Management   |
| Donation Offers | Accept, Decline, Complete         |
| Admin           | User Management and Analytics     |

---

# Running the Project

## Clone the Project

```
git clone https://github.com/udayraj10/blood-connect-api.git
cd blood-connect-api
```

## Environment Variables

```env
JWT_SECRET=your-secret-key
DB_SOURCE=your-postgresql-url
DB_USERNAME=your-username
DB_PASSWORD=your-password
```

## Start with Docker

```bash
docker compose up --build
```

Application:

```
http://localhost:8080
```

Swagger:

```
http://localhost:8080/docs
```

---

# License

MIT License
