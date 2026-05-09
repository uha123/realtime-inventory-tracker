# Real-Time Inventory & Supply Chain Tracker

A production-grade, event-driven inventory tracking system engineered for high performance and scalability. This system provides real-time stock monitoring across multiple warehouses using a modern microservices-ready architecture.

## 🚀 Key Features

*   **Real-Time Event Processing**: Utilizes **Apache Kafka** for decoupled, asynchronous stock updates and alert generation.
*   **High-Performance Caching**: Implements the **Cache-Aside Pattern** with **Redis** to minimize database latency and handle high-traffic read requests.
*   **Automated Stock Alerts**: Real-time monitoring of inventory levels with automated triggers when stock falls below minimum thresholds.
*   **Scalable Data Pipeline**: Includes a robust CSV import engine for batch processing large sets of inventory data with built-in tracking.
*   **Comprehensive Audit Logs**: Every critical action is asynchronously logged via Kafka to ensure a full traceability trail without impacting API performance.

## 🏗️ System Architecture

The project follows a modern event-driven architecture designed to ensure data consistency and system resilience.

![System Architecture](architecture_diagram.png)

### Components:
1.  **Spring Boot API**: The core service handling REST requests and business logic.
2.  **Apache Kafka**: The event backbone. It handles `inventory.update`, `stock.alert`, and `import.events` topics.
3.  **Redis Cache**: Stores frequently accessed product and warehouse data to offload the primary database.
4.  **MySQL**: The persistent system of record for all entities and transactions.

## 🌊 System Data Flows

To help developers understand the core processes, here are the step-by-step flows for the primary operations:

### 1. Real-Time Stock Update Flow
When a stock level is updated (e.g., via `PATCH /v1/inventory/{id}/quantity`):
1.  The **Inventory Service** receives the request and validates it.
2.  The inventory record is updated in the **MySQL** database.
3.  The relevant cache entries in **Redis** are evicted or updated to maintain data consistency.
4.  An **InventoryUpdateEvent** is published to the `inventory.update` **Kafka** topic.
5.  The **AuditLogConsumer** asynchronously picks up the event and records it in the system audit logs.
6.  If the quantity falls below the minimum threshold, a **StockAlertEvent** is triggered and published for immediate notification.

### 2. High-Performance Read Flow (Cache-Aside)
When a client requests inventory or product data:
1.  The API first checks **Redis** for the requested data.
2.  **Cache Hit:** The data is returned immediately to the client.
3.  **Cache Miss:** The API queries the **MySQL** database, stores the retrieved data in **Redis**, and then returns it.

### 3. Batch CSV Import Flow
When bulk inventory data is uploaded:
1.  The **Import Service** receives the `MultipartFile`.
2.  A tracking record (`CsvImportJob`) is saved in the database with a `PENDING` status, and a tracking ID is returned to the user immediately.
3.  The file is processed asynchronously. The system reads the CSV stream, parsing and saving the inventory records.
4.  The job progress is tracked, and finally marked as `COMPLETED` (or `FAILED`).

## 🛠️ Tech Stack

*   **Language**: Java 17 / 21
*   **Framework**: Spring Boot 3.4.x (with Spring Data JPA & Spring Kafka)
*   **Messaging**: Apache Kafka
*   **Caching**: Redis
*   **Database**: MySQL 8.0
*   **Build Tool**: Gradle

## 🚦 Getting Started

### 1. Prerequisites
*   **Docker Desktop** (for running the infrastructure)
*   **JDK 21**
*   **Postman** (for testing)

### 2. Launch Infrastructure
Start the database, message broker, and caching engine:
```bash
docker-compose up -d
```
This starts:
- **MySQL**: port 3306
- **Kafka**: port 9092
- **Redis**: port 6380
- **Kafka UI**: [http://localhost:8090](http://localhost:8090)

### 3. Run the Application
```bash
export JAVA_HOME=/path/to/your/jdk21
./gradlew bootRun
```

## 📖 API Documentation

The API is versioned (`/v1`) and follows RESTful principles.

| Feature | Method | Endpoint | Description |
| :--- | :--- | :--- | :--- |
| **Auth** | POST | `/v1/auth/login` | Authenticate and get session |
| **Products** | GET | `/v1/products` | List all available products |
| **Warehouses**| GET | `/v1/warehouses` | List all warehouse locations |
| **Inventory** | POST | `/v1/inventory` | Link a product to a warehouse |
| **Inventory** | PATCH | `/v1/inventory/{id}/quantity` | Update stock level (triggers Kafka) |
| **Transactions**| POST | `/v1/transactions` | Record a stock movement |
| **Audit** | GET | `/v1/audit-logs` | View system-wide event logs |
| **Import** | POST | `/v1/import/csv` | Batch upload inventory data |

## 🧪 Testing with Postman
A pre-configured Postman collection is included in the root directory: `postman_collection.json`.
1. Open Postman.
2. Click **Import** and select the file.
3. Ensure the **Postman Desktop Agent** is running to connect to `localhost:9000`.

# realtime-inventory-tracker
