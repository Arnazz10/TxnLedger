# TxnLedger — Concurrent Banking Transaction Engine

TxnLedger is a high-performance financial transaction ledger backend built with **Java 17**, **Spring Boot**, and **PostgreSQL**. It is designed to handle high-concurrency transfers while maintaining strict data integrity using advanced locking mechanisms.

## Key Features

- **Concurrent Transfer Engine**: Uses `ReentrantLock` with a consistent locking order to prevent race conditions and deadlocks during multi-account transfers.
- **Optimistic Locking**: Leverages Hibernate's `@Version` to handle concurrent balance queries and updates efficiently.
- **Async Batch Processing**: Configurable `ThreadPoolTaskExecutor` for handling high volumes of transactions.
- **PostgreSQL Optimization**: Indexed columns and query optimization for fast ledger lookups.
- **Scheduled Snapshots**: Automated end-of-day balance snapshots using Spring's `@Scheduled` tasks.
- **RESTful API**: Comprehensive endpoints for account management and fund transfers.

## Tech Stack

- **Core**: Java 17, Spring Boot 3.2.5
- **Persistence**: Spring Data JPA, Hibernate, PostgreSQL
- **Concurrency**: `ReentrantLock`, `ConcurrentHashMap`, `ThreadPoolTaskExecutor`
- **Infrastructure**: Docker, Maven
- **Lombok**: For boilerplate reduction

## Getting Started

### Prerequisites

- Docker and Docker Compose
- JDK 17
- Maven 3.8+

### Setup

1. **Start the Database**:
   ```bash
   docker-compose up -d
   ```

2. **Build the Application**:
   ```bash
   mvn clean install
   ```

3. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

## API Endpoints

### Accounts
- `POST /api/accounts`: Create a new account.
  ```json
  {
    "ownerName": "Arnab Mal",
    "initialBalance": 1000.00
  }
  ```
- `GET /api/accounts/{accountNumber}`: Get account details.
- `GET /api/accounts`: List all accounts.

### Transactions
- `POST /api/transactions/transfer`: Transfer funds between accounts.
  ```json
  {
    "fromAccountNumber": "ACC123",
    "toAccountNumber": "ACC456",
    "amount": 250.00
  }
  ```

## Concurrency Strategy

TxnLedger employs a dual-locking strategy:
1. **Application-level Locking**: `ReentrantLock` is used per-account to synchronize transfer operations. A consistent locking order (based on account number) prevents circular wait deadlocks.
2. **Database-level Locking**: Optimistic locking via `@Version` ensures that even if application-level locks are bypassed, the database remains consistent by detecting concurrent modifications.

## Development

- **Repo**: [https://github.com/Arnazz10/TxnLedger](https://github.com/Arnazz10/TxnLedger)
- **Author**: Arnazz10 (arnabmal665@gmail.com)
