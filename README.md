# Document Management System (DMS)

> A Spring Boot backend for storing and serving documents on MongoDB GridFS, with client management and auditing.

A document management backend that handles file upload, storage, and download using MongoDB GridFS. It includes client/user management with password handling, HTTP Basic authentication enforced on every request, CORS support, and automatic audit tracking on stored entities.

## Features

- **File storage on GridFS** — single and batch (multiple-file) uploads, with metadata persisted in MongoDB
- **Download** — fetch a stored file by document id (GET by path, or POST with a request body)
- **Client/user management** — create clients, change password, and reset a password to a default
- **Authentication** — HTTP Basic auth validated on every request by a servlet `AuthFilter`
- **BCrypt password hashing**
- **CORS filter** with common security response headers
- **Auditing** — Spring Data Mongo auditing populates created/last-modified fields on entities
- **API documentation** via Swagger / OpenAPI UI

## Tech Stack

- **Java 17**
- **Spring Boot 4.1.0-M1** (packaged as a **WAR**, deployable to a servlet container or run standalone)
- **MongoDB** with **GridFS**
- **Spring Data MongoDB**
- **Spring Security** (BCrypt password encoding)
- **springdoc-openapi** (Swagger UI)
- **Apache POI**, **Joda-Time**, **Gson**, **Lombok**
- **Maven**

## Getting Started

### Prerequisites

- JDK 17
- A running MongoDB instance (default `127.0.0.1:27017`, database `dms`)

### Build

```bash
./mvnw clean package
```

This produces a deployable WAR under `target/`.

### Run

Run the WAR directly:

```bash
java -jar target/dms.war
```

…or deploy it to a Tomcat / servlet container. The service runs on port `8080` under the context path `/dms`.

## Configuration

Configuration is in `src/main/resources/application.properties`. **Provide your own values for the keys below — never commit real credentials.**

| Key | Description |
| --- | --- |
| `server.port` / `server.address` | Listen port and bind address |
| `server.contextPath` | Application context path (`/dms`) |
| `spring.data.mongodb.host` / `.port` / `.database` | MongoDB connection target |
| `spring.data.mongodb.username` | MongoDB user — set to `your-mongo-user` |
| `spring.data.mongodb.password` | MongoDB password — set to `your-mongo-password` |
| `default.user` / `default.password` | Default system account used for auditing — use your own values |
| `default.reset.password` | Password applied by the reset-password flow |
| `spring.http.multipart.max-file-size` | Max upload size (20MB) |
| `byPass.services` | Paths excluded from auth (Swagger/API-docs resources) |

> Security note: keep MongoDB credentials and default passwords out of version control — supply them via environment-specific configuration or environment variables rather than committing them.

## API

All endpoints (except the Swagger/API-docs paths) require an HTTP Basic `Authorization` header.

### Files

| Method | Path | Description |
| --- | --- | --- |
| POST | `/uploadFile` | Upload a single file; returns the upload id and filename |
| POST | `/uploadMultipleFiles` | Upload multiple files in one request |
| GET | `/downloadFile/{docId}` | Download a file by document id |
| POST | `/downloadFile` | Download a file using a request body |

### Users / Clients

| Method | Path | Description |
| --- | --- | --- |
| POST | `/addClient` | Create a new client |
| POST | `/changePassword` | Change an existing client's password |
| GET | `/resetPassword/{clientName}` | Reset a client's password to the configured default |

## Project Structure

```
src/main/java/com/document/backend
├── DmsApplication.java        # Spring Boot entry point
├── ServletInitializer.java    # WAR servlet bootstrap
├── controller/                # FileController, UserController
├── service/ (+ impl, helper)  # File, User, Authentication services
├── filters/                   # AuthFilter, CORSFilter
├── config/                    # Mongo, Swagger, Audit, System config
├── data/models/               # Clients, DocumentMapping, Counters, AuditEntity
└── packets/                   # Request/response payloads
```
