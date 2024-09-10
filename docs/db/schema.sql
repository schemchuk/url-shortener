-- Create the database
CREATE DATABASE IF NOT EXISTS urlShortener;
USE urlShortener;

-- Table for users
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       userName VARCHAR(255) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       refresh_token VARCHAR(255)
);

-- Table for roles
CREATE TABLE roles (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name ENUM('ADMIN', 'TRIAL', 'PAID') NOT NULL UNIQUE,
                       expiry_date DATE
);

-- Join table for Many-to-Many relationship between users and roles
CREATE TABLE user_roles (
                            user_id BIGINT,
                            role_id BIGINT,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                            FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Table for short URLs
CREATE TABLE short_urls (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            shortKey VARCHAR(255) NOT NULL UNIQUE,
                            fullUrl VARCHAR(2048) NOT NULL,
                            click_count BIGINT DEFAULT 0,
                            user_id BIGINT,
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- Table for subscriptions
CREATE TABLE subscriptions (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               user_id BIGINT UNIQUE,
                               startDate DATETIME NOT NULL,
                               endDate DATETIME,
                               subscriptionType ENUM('ADMIN', 'TRIAL', 'PAID') NOT NULL,
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
