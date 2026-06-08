-- ============================================================================
-- 1. USERS TABLE
-- ============================================================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    phone VARCHAR(20) NOT NULL,
    blood_group VARCHAR(10) NOT NULL,
    city VARCHAR(100) NOT NULL,
    address VARCHAR(500) NOT NULL,
    account_type VARCHAR(50) NOT NULL,
    role VARCHAR(50) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    last_donation_date DATE,
    is_active BOOLEAN DEFAULT TRUE, -- Fixed boolean definition
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- 2. BLOOD REQUESTS TABLE
-- ============================================================================
CREATE TABLE IF NOT EXISTS blood_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    requester_id BIGINT NOT NULL,
    blood_group VARCHAR(10) NOT NULL,
    city VARCHAR(100) NOT NULL,
    urgency_level VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    message VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_blood_requests_requester FOREIGN KEY (requester_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ============================================================================
-- 3. DONATION OFFERS TABLE
-- ============================================================================
CREATE TABLE IF NOT EXISTS donation_offers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    donor_id BIGINT NOT NULL,
    blood_request_id BIGINT,
    status VARCHAR(50) NOT NULL,
    message VARCHAR(1000),
    offered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    responded_at TIMESTAMP,
    CONSTRAINT fk_donation_offers_donor FOREIGN KEY (donor_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_donation_offers_request FOREIGN KEY (blood_request_id) REFERENCES blood_requests(id) ON DELETE SET NULL
);