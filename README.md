# backend-developer-as-final-64203-vivek
Final Project Assignment - This repository contains the complete final project code and documentation.

# Resource Booking System

A RESTful Resource Booking System built using **Java 17, Spring Boot, Spring Security, JWT, Spring Data JPA, and H2/MySQL/PostgreSQL**.

## Features

* JWT-based authentication with BCrypt password encryption
* Role-Based Access Control (ADMIN and USER)
* Multiple roles supported per user
* ADMIN can manage resources and reservations
* USER can view resources and create reservations
* USER can view only their own reservations
* Reservation identity is derived from JWT
* Reservation statuses: `PENDING`, `CONFIRMED`, `CANCELLED`
* Filtering by status, minimum price, maximum price, and resource
* Pagination and sorting support
* Request validation and global exception handling
* Database seed data loaded automatically on startup

## Tech Stack

* Java 17+
* Spring Boot
* Spring Security
* JWT
* Spring Data JPA / Hibernate
* H2 Database
* MySQL / PostgreSQL supported
* Maven

## Setup

### Prerequisites

* Java 17+
* Maven

### Run the Application

```bash
mvn spring-boot:run
```

The application starts at:

```text
http://localhost:8080
```

## Environment Variables

```text
JWT_SECRET=your_secure_jwt_secret_key_at_least_32_characters
JWT_EXPIRATION=86400000
```

## Database Configuration

The project currently uses an H2 in-memory database.

```properties
spring.datasource.url=jdbc:h2:mem:resource_booking_db
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always

jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION:86400000}
```

H2 Console:

```text
http://localhost:8080/h2-console
```

## Seed Data

Initial data is automatically loaded from:

```text
src/main/resources/data.sql
```

The following data is seeded:

* Roles
* Users
* User Roles
* Resources
* Reservations

Test users:

```text
ADMIN
Username: admin
Password: password

USER
Username: vivek
Password: 1234
```

## Authentication

### Login

```http
POST /auth/login
```

```json
{
  "username": "admin",
  "password": "password"
}
```

Use the returned JWT for protected APIs:

```http
Authorization: Bearer <JWT_TOKEN>
```

## Main APIs

### Resources

| Method | Endpoint                    | Access      |
| ------ | --------------------------- | ----------- |
| POST   | `/resources/createResource` | ADMIN       |
| GET    | `/resources/fetchResource`  | ADMIN, USER |
| PUT    | `/resources/updateResource` | ADMIN       |
| DELETE | `/resources/deleteResource` | ADMIN       |

### Reservations

| Method | Endpoint                          | Access      |
| ------ | --------------------------------- | ----------- |
| POST   | `/reservations/createReservation` | USER, ADMIN |
| GET    | `/reservations/fetchReservations` | USER, ADMIN |
| PUT    | `/reservations/updateReservation` | ADMIN       |
| DELETE | `/reservations/deleteReservation` | ADMIN       |

### Reservation Filtering

```http
GET /reservations/fetchReservations?status=CONFIRMED&minPrice=1000&maxPrice=10000&page=0&size=10&sort=price,desc
```

Supported filters:

* `status`
* `minPrice`
* `maxPrice`
* `resourceId`
* `page`
* `size`
* `sort`

## Authorization Rules

* **ADMIN** can access and manage all resources and reservations.
* **USER** can view resources, create reservations, and view only their own reservations.
* Reservation ownership is determined from the authenticated JWT user.

## API Testing

The APIs can be tested using Postman. The recommended flow is:

```text
Login → Copy JWT → Add Bearer Token → Test Protected APIs
```
