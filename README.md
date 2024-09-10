# URL Shortener

The **URL Shortener** project is a web application designed to generate short, six-character URLs for long URLs. The application supports user registration, authentication, and role management.

This project is based on: [https://github.com/alex2808pl/url-shortener.git](https://github.com/alex2808pl/url-shortener.git).

## Functionality Overview

1. **User Registration and Authentication**: Users can register and log in to the system.
2. **Role Management**:
    - Upon registration, the user receives a `TRIAL` role, valid for 1 month.
    - The admin can assign the `PAID` role to a user, valid for 1 year.
    - The admin has a permanent role.
3. **User Capabilities**:
    - Users with the `TRIAL` role can:
        - View their role and user information by ID.
        - Create short URLs based on long URLs.
        - View their own links and the number of clicks on those short links.
        - Redirect from the short URL to the original URL.
        - Delete their own short links.
        - View the list of roles.
    - Users with the `PAID` role have the same capabilities as `TRIAL` users and can also update their personal information, except for their role.
4. **Administrator**:
    - Can assign roles to users.
    - Cannot change their own personal information.

## Technology Stack

- **Programming Language**: Java 17
- **Framework**: Spring Boot 3.2.0
    - Spring Web
    - Spring Data JPA
    - Spring Security (including JWT)
    - Spring Validation
- **Database**: H2 (for testing)
- **Database Migration Management**: Liquibase
- **API Documentation**: SpringDoc OpenAPI + Swagger UI
- **Testing**:
    - JUnit 5
    - Spring Security Test
    - H2 for the test database
- **Logging**: Log4j2
- **DTO Mapping**: MapStruct
- **Dependency Management**: Maven

## Configuration

- **Server port**: `8080`
- **Default active profile**: `test`
- **H2 Console**: `/h2-console`
- **Short URL settings**:
    - Allowed characters: `abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789`
    - Key length: `6`
- **JWT secrets** for access and refresh tokens.

## Running the Application

To run the application:

1. Clone the repository.
2. Run the command `mvn spring-boot:run` to start the server on port 8080.
3. Open [Swagger UI](http://localhost:8080/swagger-ui/index.html) to view the API documentation.

## Additional Information

- The H2 in-memory database is used for development and is reset after each test.
- The admin can manage user roles but cannot change their own personal information.

