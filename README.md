# Real-Time Inventory & Supply Chain Tracker

A high-performance, event-driven inventory tracking system built with Spring Boot, Apache Kafka, and Redis.

## Features

- **Real-Time Inventory Updates:** Event-driven architecture using Kafka for fast, decoupled stock updates.
- **Cache-Aside Pattern:** Optimized reads using Redis caching to reduce database load.
- **CSV Import Pipeline:** Batch processing of inventory data with tracking and rollback mechanisms.
- **Audit Logging & Stock Alerts:** Asynchronous logging and alert generation.
- **Robust REST API:** Full CRUD operations for Products, Warehouses, Inventory, and Transactions.

## Tech Stack

- Java 17 / 21
- Spring Boot 3.4.x
- Apache Kafka (Event Bus)
- Redis (Caching)
- MySQL (Primary Storage)
- Lombok

## Getting Started

### Prerequisites

- JDK 17 or 21 (If you're on a Mac using Java 25, you must run Gradle with a Java 21 `JAVA_HOME` path).
- MySQL instance running on `localhost:3306` (or update `application.yml`).
- Redis instance running on `localhost:6379`.
- Kafka broker running on `localhost:9092`.

### Database Setup

The project uses Hibernate `ddl-auto: update`, which automatically generates the schema based on the JPA models. 

Alternatively, if you prefer manual SQL initialization, you can use the `src/main/resources/schema.sql` file provided to create the tables. To have Spring Boot run this script automatically on startup, add the following to `application.yml`:
```yaml
spring:
  sql:
    init:
      mode: always
```

### Building and Running

You can start the application using the Gradle wrapper (or local Gradle installation):

```bash
# Set your JAVA_HOME to Java 21 if needed, for example on Mac:
# export JAVA_HOME=/opt/homebrew/opt/openjdk@21

gradle bootRun
```

Or build an executable jar:

```bash
gradle build
java -jar build/libs/realtime-inventory-tracker-0.0.1-SNAPSHOT.jar
```

## Architecture

```text
┌─────────────────────────────────────────────────────┐
│                    CLIENT / UI                      │
└───────────────────┬─────────────────────────────────┘
                    │ REST API
┌───────────────────▼─────────────────────────────────┐
│             Spring Boot API Gateway                 │
└──────┬──────────────┬───────────────┬───────────────┘
       │              │               │
┌──────▼─────┐ ┌──────▼─────┐ ┌──────▼──────┐
│ Inventory  │ │  Warehouse │ │ CSV Import  │
│  Service   │ │  Service   │ │  Service    │
└──────┬─────┘ └──────┬─────┘ └──────┬──────┘
       │              │               │
       └──────────────▼───────────────┘
                       │
            ┌──────────▼──────────┐
            │    Apache Kafka     │
            │  (Event Bus)        │
            └──────────┬──────────┘
                       │
          ┌────────────▼────────────┐
          │   Kafka Consumers       │
          └────────────┬────────────┘
                       │
        ┌──────────────┼──────────────┐
┌───────▼──────┐ ┌─────▼──────┐ ┌────▼──────┐
│    MySQL     │ │   Redis    │ │  GCP GKE  │
└──────────────┘ └────────────┘ └───────────┘
```

## API Endpoints (`/v1`)

- `POST /v1/auth/login`
- `GET /v1/products`
- `GET /v1/warehouses`
- `GET /v1/inventory`
- `PATCH /v1/inventory/{id}/quantity`
- `POST /v1/transactions`
- `POST /v1/import/csv`

*(See the `com.inventory.tracker.controller` package for a full list of endpoints).*

# realtime-inventory-tracker
