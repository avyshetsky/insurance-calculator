# Security — CVE Remediations

## Changes Made

### Log4j 1.2.17 (CVE-2019-17571, CVE-2021-4104)
- **Removed** `log4j:log4j:1.2.17` entirely from all modules
- **Replaced** with SLF4J + Logback (Spring Boot default logging)
- All `org.apache.log4j.Logger` usages migrated to `org.slf4j.Logger`
- Structured JSON logging via `logstash-logback-encoder` for production

### Springfox Swagger 2.7.0 (outdated, transitive vulnerabilities)
- **Removed** `springfox-swagger2` and `springfox-swagger-ui`
- **Replaced** with `springdoc-openapi-starter-webmvc-ui:2.5.0` (OpenAPI 3)

### Spring Boot 2.1.7 → 3.3.6
- Addresses all CVEs in Spring Framework 5.x and Spring Boot 2.x
- Full Jakarta EE 10 namespace migration (`javax.*` → `jakarta.*`)
- Java 21 (LTS) for latest security patches and language features

### Java 8 → Java 21
- Addresses all JDK security advisories from Java 8 through 20

### Docker Image
- Base image: `eclipse-temurin:21-jre-alpine` (minimal attack surface)
- Non-root user execution
- Multi-stage build (no build tools in runtime image)

## Reporting Vulnerabilities

Please report security vulnerabilities to info@global-scale.com.
