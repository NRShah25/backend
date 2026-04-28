# 💰 FinanceTracker — Backend

A personal finance tracking REST API built with Spring Boot, designed to mirror 
real-world fintech backend architecture.

## 🛠 Tech Stack

- **Java 21** + **Spring Boot 3.5**
- **Spring Security** + **JWT** authentication
- **Spring Data JPA** + **MySQL**
- **AWS SDK** (SQS, SNS, S3)
- **Maven**
- **Lombok**

## ✨ Features

- JWT-based authentication with role-based access (User, Financial Advisor)
- Transaction management (income & expenses by category)
- Monthly budget tracking with real-time spending calculation
- Automated daily budget alert engine (WARNING / EXCEEDED)
- Event-driven alert pipeline via AWS SQS → Lambda → SNS
- BCrypt password hashing
- Stateless REST API

## 📡 API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register new user |
| POST | /api/auth/login | Login and get JWT token |

### Transactions
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/transactions | Get all transactions |
| POST | /api/transactions | Create transaction |
| DELETE | /api/transactions/{id} | Delete transaction |
| GET | /api/transactions/summary | Get monthly total |

### Budgets
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/budgets | Get monthly budgets |
| POST | /api/budgets | Create or update budget |
| DELETE | /api/budgets/{id} | Delete budget |
| GET | /api/budgets/alerts | Get WARNING/EXCEEDED budgets |

## 🚀 Running Locally

### Prerequisites
- Java 21
- Maven 3.9+
- MySQL 9+

### Steps
1. Clone the repository
2. Create a MySQL database called `financetracker`
3. Update `src/main/resources/application.properties` with your MySQL credentials
4. Run the application:
mvn spring-boot:run
The API will be available at `http://localhost:8080`
## 🔗 Related
- [FinanceTracker Frontend](https://github.com/NRShah25/frontend)
