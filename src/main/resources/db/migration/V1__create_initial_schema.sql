-- V1__create_initial_schema.sql
CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE customers (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    document VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    address TEXT,
    active BOOLEAN DEFAULT true,
    registered_at TIMESTAMP
);

CREATE TABLE movies (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    imdb_id VARCHAR(20),
    director VARCHAR(100),
    genre VARCHAR(100),
    release_year INTEGER,
    synopsis TEXT,
    poster_url VARCHAR(500),
    imdb_rating DECIMAL(3,1),
    daily_rate DECIMAL(10,2) NOT NULL,
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP
);

CREATE TABLE fines (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    fine_type VARCHAR(30) NOT NULL,
    value DECIMAL(10,2) NOT NULL,
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP
);

CREATE TABLE rentals (
    id VARCHAR(36) PRIMARY KEY,
    customer_id VARCHAR(36) NOT NULL,
    movie_id VARCHAR(36) NOT NULL,
    rental_date DATE NOT NULL,
    expected_return_date DATE NOT NULL,
    actual_return_date DATE,
    daily_rate DECIMAL(10,2) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    fine_amount DECIMAL(10,2),
    status VARCHAR(20) NOT NULL,
    days_late INTEGER,
    days_rented INTEGER,
    created_at TIMESTAMP,
    CONSTRAINT fk_rental_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_rental_movie FOREIGN KEY (movie_id) REFERENCES movies(id)
);

CREATE TABLE damage_records (
    id VARCHAR(36) PRIMARY KEY,
    rental_id VARCHAR(36) NOT NULL,
    damage_type VARCHAR(20) NOT NULL,
    damage_cost DECIMAL(10,2) NOT NULL,
    description TEXT,
    paid BOOLEAN DEFAULT false,
    reported_at TIMESTAMP,
    CONSTRAINT fk_damage_rental FOREIGN KEY (rental_id) REFERENCES rentals(id)
);

-- Insert default fine
INSERT INTO fines (id, name, fine_type, value, active, created_at) 
VALUES ('1', 'Late Return Fine', 'DAILY_PERCENTAGE', 10.00, true, NOW());
