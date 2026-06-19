# Insurance Calculator REST API

REST API for insurance premium calculation based on tariff, bonus, customer age, and policy inception date.

## Tech Stack

- Java 21
- Spring Boot 3.3.6
- Maven (with wrapper)
- H2 (in-memory database)
- JUnit 5 + Mockito
- SpringDoc OpenAPI 3 (Swagger UI)
- OpenTelemetry (traces + metrics via OTLP)
- Logback with structured JSON logging (Logstash encoder)
- JaCoCo (≥ 80% line coverage enforcement)
- Docker (multi-stage build)

## Project Structure

Multi-module Maven project:

| Module | Responsibility |
|--------|---------------|
| `insurance-api` | API contracts: DTOs (Java records), service interfaces |
| `insurance-engine` | Business logic: tariff rates, date calculations, price engine |
| `insurance-data` | Persistence layer: JPA entities, repositories, service implementations |
| `insurance-controller` | Web layer: REST controllers, exception handling, observability config |
#About this project.

A rest api for premium calculation depending on tariff, bonus, customer age and customer inception date.

## Tech Stack:
-	Java 8
-	Maven
-	SpringBoot 2.1.7
-	H2
-	jUnit 5
-	Mockito
-	Swagger 2.7.0
-	Project Lombok
-   ValidationAPI
-   Log4j

## Project structure.

Maven multi module project consisting of 3 parts:
-	“insurance-controller” main module with controllers, swagger and SpringBootApplication 
-	“insurance-data” module related to database features
-	“insurance-engine” module related to final price calculation

## How to Start

### Requirements

- JDK 21+
- Free port 8080

### Steps

```bash
# Build (uses Maven Wrapper — no local Maven installation needed)
./mvnw clean package

# Run
java -jar insurance-controller/target/insurance-controller-0.0.1-SNAPSHOT.jar

# Open Swagger UI
open http://localhost:8080/swagger-ui.html
```

### Docker

```bash
# Build and run with Docker Compose (includes Datadog Agent for OTel)
DD_API_KEY=<your-key> docker compose up --build

# Or build standalone
docker build -t insurance-app .
docker run -p 8080:8080 insurance-app
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/customers` | List all customers |
| GET | `/api/v1/customers/{id}` | Get customer by ID |
| POST | `/api/v1/customers` | Create customer |
| PUT | `/api/v1/customers/{id}` | Update customer |
| DELETE | `/api/v1/customers/{id}` | Delete customer |

## Observability

Environment variables for OTel configuration:

| Variable | Default | Description |
|----------|---------|-------------|
| `OTEL_SERVICE_NAME` | `insurance-calculator` | Service name in traces |
| `OTEL_EXPORTER_OTLP_ENDPOINT` | `http://localhost:4317` | OTLP collector endpoint |
| `OTEL_EXPORTER_OTLP_PROTOCOL` | `grpc` | OTLP protocol |
| `SPRING_PROFILES_ACTIVE` | `default` | Set to `json` for structured logs |

Actuator endpoints: `/actuator/health`, `/actuator/info`, `/actuator/metrics`, `/actuator/prometheus`

## Testing

```bash
# Run all tests with coverage
./mvnw clean verify

# Coverage reports at: {module}/target/site/jacoco/index.html
```
