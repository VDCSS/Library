# Library Backend (Spring Boot)

## Run locally (postgres)
1. Install Java 17 and Maven
2. Create Postgres DB or use docker-compose:
   `docker-compose up -d`
3. Build
   `mvn clean package -DskipTests`
4. Run
   `java -jar target/demo-1.0.0.jar`

## Environment variables
- SPRING_DATASOURCE_URL
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD
- APP_JWT_SECRET (min 32 characters)

## Endpoints
- POST /api/auth/register
- POST /api/auth/login
- GET /api/books
- POST /api/loans (requires auth)
- POST /api/loans/{id}/return (requires auth)

