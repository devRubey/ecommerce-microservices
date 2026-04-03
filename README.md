# 🛒 E-Commerce Microservices Backend

A production-style microservices e-commerce backend built with **Java, Spring Boot, PostgreSQL, and Spring Cloud Gateway**. Features 4 independent services — each with its own database — communicating via REST.

---

## 🏗️ Architecture

```
[ API Gateway :8080 ]
         |
  ┌──────┼──────┐
  │      │      │
8081   8082   8083
product  user  order
  DB     DB     DB
```

---

## 🛠️ Tech Stack

- **Java 17** + Spring Boot 4.x
- **Spring Cloud Gateway** — API routing
- **Spring Security** + **JWT** — authentication
- **PostgreSQL** — one database per service
- **Lombok** — reduced boilerplate
- **Maven** — dependency management

---

## 📦 Services

| Service | Port | Description |
|---|---|---|
| api-gateway | 8080 | Routes all traffic to correct service |
| product-service | 8081 | Product catalog — CRUD, search, pagination |
| user-service | 8082 | Registration, login, JWT authentication |
| order-service | 8083 | Place orders, inter-service communication |

---

## 🚀 Getting Started

### 1. Create the databases
```sql
CREATE DATABASE products_db;
CREATE DATABASE users_db;
CREATE DATABASE orders_db;
```

### 2. Configure each service
Update `application.properties` in each service with your PostgreSQL password:
```properties
spring.datasource.password=your_password_here
```

### 3. Run the services
Start each service in IntelliJ or run:
```bash
./mvnw spring-boot:run
```
Start in this order: `product-service` → `user-service` → `order-service` → `api-gateway`

---

## 📋 API Endpoints

All requests go through the gateway at `http://localhost:8080`

### Products
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/products` | Get all products (paginated) |
| GET | `/api/products/{id}` | Get product by ID |
| POST | `/api/products` | Create a product |
| PUT | `/api/products/{id}` | Update a product |
| DELETE | `/api/products/{id}` | Delete a product |
| GET | `/api/products/search/category?q=` | Search by category |
| GET | `/api/products/search/name?q=` | Search by name |

### Auth
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and get JWT token |

### Orders
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/orders` | Place an order |
| GET | `/api/orders` | Get all orders |
| GET | `/api/orders/{id}` | Get order by ID |
| GET | `/api/orders/user/{userId}` | Get orders by user |
| POST | `/api/orders/{id}/confirm` | Confirm an order |
| POST | `/api/orders/{id}/cancel` | Cancel an order |

---

## 🧪 Testing

```bash
./mvnw test
```

---

*Built by [devRubey](https://github.com/devRubey)*
