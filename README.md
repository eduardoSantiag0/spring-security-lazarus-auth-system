The aim of this project is to study Spring Security, JWT-based authentication, and role-based access control.

# 🛰️ Lazarus Auth System
**The Lazarus Mission** is a fictional scenario inspired by the film Interstellar. Its goal is to identify potentially habitable planets beyond our solar system. Onboard the Endurance spacecraft are both scientists and engineers, each with distinct roles and access levels.

For security reasons, only scientists have full access to confidential mission data. Engineers can assist and execute tasks, but they do not receive the complete picture.

This system was built to reflect that structure — using role-based authorization and JWT to ensure that only users with the correct role (e.g., ``SCIENTIST``) can access sensitive endpoints.

---

## ✨ Features

- ✅ User registration and authentication (`/signup`, `/login`)
- ✅ JWT and Refresh Token generation with custom claims (`role`, `mission_code`)
- ✅ Secure login/password update and token refresh
- ✅ Token blacklist on logout (using Redis TTL)
- ✅ Login attempt limiter (by IP) via Redis
- ✅ Swagger/OpenAPI documentation
- ✅ Role-based access control (e.g., `SCIENTIST`)
- ✅ Mission management endpoints (GET, PATCH, Confidential access)

---

## 📦 Tech Stack

- Java 17+
- Spring Boot
- Spring Security
- Spring Data JPA
- JWT (`java-jwt`)
- Redis (via `StringRedisTemplate`)
- Flyway (for DB migrations)
- Swagger/OpenAPI (`springdoc-openapi`)
- PostgreSQL

---

## 🔐 Authentication Endpoints

| Method | Path                  | Description                                | Auth Required |
|--------|------------------------|--------------------------------------------|---------------|
| POST   | `/auth/signup`         | Create a new user                          | ❌             |
| POST   | `/auth/login`          | Authenticate and receive JWT + refresh     | ❌             |
| POST   | `/auth/logout`         | Logout and blacklist the JWT               | ✅             |
| POST   | `/auth/update-token`   | Update access token using refresh token    | ❌             |
| POST   | `/auth/update-login`   | Change username/password                   | ✅             |

> Logout invalidates the current token by blacklisting it in Redis with a time-to-live (TTL) based on its expiration.

---

## 🛰️ Lazarus Endpoints (Authenticated)

| Method | Path                            | Description                                     | Role Required |
|--------|----------------------------------|-------------------------------------------------|---------------|
| GET    | `/lazarus/users/me`             | Get current user's login                        | Any           |
| GET    | `/lazarus/mission`              | Get mission information                         | Any           |
| GET    | `/lazarus/mission/confidential` | View confidential mission data (SCIENTIST only) | `SCIENTIST`   |
| PATCH  | `/lazarus/mission`              | Update mission status (SCIENTIST only)          | `SCIENTIST`   |

---

## 🔐 Security Overview

- JWT tokens signed with HMAC256 (`iss: auth-lazarus`)
- Custom JWT claims: `mission_code`, `role`
- Tokens can be blacklisted and stored in Redis until expiration
- Login attempt limiter: 5 attempts per IP within 5 minutes
- Role-based access for sensitive endpoints

---

## Run Redis local with: 

```bash
docker-compose up
```

## 📚 Swagger Docs
Once the application is running, access the documentation at:

```bash
http://localhost:8080/swagger-ui.html
```