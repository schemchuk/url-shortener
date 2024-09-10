# URL Shortener

The **URL Shortener** project is a web application designed to generate short six-character links for long URLs. The application supports user registration, authentication, and role management.

This project is based on: [https://github.com/alex2808pl/url-shortener.git](https://github.com/alex2808pl/url-shortener.git).

## Features Overview

1. **User Registration and Authentication**: Users can register and log in with role-based access control.
2. **Role Management**:
    - Upon registration, the user is assigned the `TRIAL` role, which is valid for 1 month.
    - The admin can assign the user the `PAID` role, which is valid for 1 year.
    - The admin has a permanent role that cannot be changed.
3. **User Capabilities**:
    - Users with the `TRIAL` role can:
        - View their profile information and role details by ID.
        - Create short links for long URLs.
        - View their short links and click statistics.
        - Use short links for redirecting to original URLs.
        - Delete their own short links.
        - View the list of available roles.
    - Users with the `PAID` role can perform all actions of `TRIAL` users and update their personal information, except for the role.
4. **Admin**:
    - Can assign roles to users.
    - Cannot change their own personal information.

## Technology Stack

- **Programming Language**: Java 17
- **Framework**: Spring Boot 3.2.0
    - Spring Web
    - Spring Data JPA
    - Spring Security (including JWT for authentication)
    - Spring Validation
- **Database**: MySQL (with automatic database creation if it does not exist) and H2 (for testing)
- **Database Migration**: Liquibase
- **API Documentation**: SpringDoc OpenAPI + Swagger UI
- **Testing**:
    - JUnit 5
    - Spring Security Test
    - H2 database for tests
- **Logging**: Log4j2
- **DTO Mapping**: MapStruct
- **Dependency Management**: Maven

## Configuration

- **Server Port**: `8080`
- **Active Spring Profile**: `prod`
- **H2 Console**: Accessible at `/h2-console`
- **Short URL Settings**:
    - Allowed characters: `abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789`
    - Key length: `6`
- **JWT Secret Keys**: Configured for access and refresh tokens.
- **Logging**:
    - Logs are saved in the file `logs/url-shortener.log`.
    - Log level: `DEBUG` for core code and Spring Web, `TRACE` for Hibernate SQL.

## Database Configuration

- **MySQL**:
    - URL: `jdbc:mysql://localhost:3306/url_shortener_db?createDatabaseIfNotExist=true`
    - Driver: `com.mysql.cj.jdbc.Driver`
    - Hibernate dialect: `org.hibernate.dialect.MySQL8Dialect`

- **H2 (for testing)**:
    - URL: `jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`
    - Console: `/h2-console`
    - Hibernate dialect: `org.hibernate.dialect.H2Dialect`

## Running the Application

To run the application locally:

1. Clone the repository.
2. Ensure your database (MySQL or H2) is properly set up.
3. Run the following command:

   ```bash
   mvn spring-boot:run
