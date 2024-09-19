# URL Shortener

The **URL Shortener** project is a web application designed to generate short links for long URLs. The application supports user registration, authentication, and role management.

This project is based on: [https://github.com/alex2808pl/url-shortener.git](https://github.com/alex2808pl/url-shortener.git).

## Features Overview

1. **User Registration and Authentication**: Users can register and log in with role-based access control.
2. **Role Management**:
    - Upon registration, the user is assigned the `TRIAL` role, which is valid for 30 days.
    - The admin can assign the user the `PAID` role, which is valid for 365 days.
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
- **Database**: MySQL and H2 (for testing)
- **Database Migration**: Liquibase
- **API Documentation**: SpringDoc OpenAPI + Swagger UI
- **Testing**: JUnit 5, Spring Security Test
- **Logging**: Log4j2
- **DTO Mapping**: MapStruct
- **Dependency Management**: Maven

## Configuration

- **Server Port**: `8080`
- **Active Spring Profile**: `prod`
- **JWT Secret Keys**: Configured for access and refresh tokens.

## Database Configuration

- **MySQL**:
    - **URL**: `jdbc:mysql://mysql:3306/url_shortener_db?createDatabaseIfNotExist=true`
    - **Driver**: `com.mysql.cj.jdbc.Driver`
    - **Hibernate Dialect**: `org.hibernate.dialect.MySQL8Dialect`
- **H2 (for testing)**:
    - **URL**: `jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`
    - **Console**: `/h2-console`
    - **Hibernate Dialect**: `org.hibernate.dialect.H2Dialect`

## Running the Application

### Local Run

1. Clone the repository:
   ```bash
   git clone https://github.com/alex2808pl/url-shortener.git
   cd url-shortener
2. Ensure your database (MySQL or H2) is properly set up.
3. Run the application:


### Running with Docker
1. Build and run the containers: Ensure Docker and Docker Compose are installed. Run the following command:
   ```bash
   docker-compose up --build
2. Access the application: The application is available at http://localhost:8080.
3. Access the MySQL database: Connection URL: jdbc:mysql://localhost:3306/url_shortener_db, user root, password 123.
4. (Optional) Access the H2 Console: URL: http://localhost:8080/h2-console.

