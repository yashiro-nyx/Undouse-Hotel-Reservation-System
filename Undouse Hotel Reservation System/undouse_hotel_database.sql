-- ============================================
-- Undouse Hotel Management System Database
-- MySQL Database Schema
-- ============================================

-- Create Database
DROP DATABASE IF EXISTS undouse_hotel;
CREATE DATABASE undouse_hotel CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE undouse_hotel;

-- ============================================
-- TABLE: users
-- Stores registered user accounts
-- ============================================
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    city VARCHAR(100),
    country VARCHAR(100),
    registration_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    last_login_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    total_bookings INT DEFAULT 0,
    status ENUM('active', 'inactive', 'suspended') DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_last_login (last_login_date),
    INDEX idx_registration (registration_date)
) ENGINE=InnoDB;

-- ============================================
-- TABLE: admin_credentials
-- Stores admin login credentials
-- ============================================
CREATE TABLE admin_credentials (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username)
) ENGINE=InnoDB;

-- ============================================
-- TABLE: guests
-- Stores guest information from bookings
-- ============================================
CREATE TABLE guests (
    guest_id INT AUTO_INCREMENT PRIMARY KEY,
    prefix VARCHAR(10),
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    mobile VARCHAR(20),
    address1 VARCHAR(255),
    address2 VARCHAR(255),
    city VARCHAR(100),
    zip_code VARCHAR(20),
    country VARCHAR(100),
    registration_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_phone (phone),
    INDEX idx_city (city),
    INDEX idx_country (country)
) ENGINE=InnoDB;

-- ============================================
-- TABLE: room_types
-- Stores different types of rooms available
-- ============================================
CREATE TABLE room_types (
    room_type_id INT AUTO_INCREMENT PRIMARY KEY,
    room_type_name VARCHAR(100) NOT NULL UNIQUE,
    capacity VARCHAR(20),
    base_price DECIMAL(10, 2) NOT NULL,
    description TEXT,
    amenities TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_room_type (room_type_name)
) ENGINE=InnoDB;

-- ============================================
-- TABLE: room_inventory
-- Stores individual room units
-- ============================================
CREATE TABLE room_inventory (
    room_id VARCHAR(50) PRIMARY KEY,
    room_type_id INT NOT NULL,
    location VARCHAR(100),
    status ENUM('Available', 'Occupied', 'Maintenance', 'Out of Service') DEFAULT 'Available',
    image_path VARCHAR(500),
    last_updated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (room_type_id) REFERENCES room_types(room_type_id) ON DELETE CASCADE,
    INDEX idx_room_type (room_type_id),
    INDEX idx_status (status)
) ENGINE=InnoDB;

-- ============================================
-- TABLE: room_images
-- Stores multiple images for each room unit
-- ============================================
CREATE TABLE room_images (
    image_id INT AUTO_INCREMENT PRIMARY KEY,
    room_id VARCHAR(50) NOT NULL,
    image_path VARCHAR(500) NOT NULL,
    image_order INT DEFAULT 0,
    is_primary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (room_id) REFERENCES room_inventory(room_id) ON DELETE CASCADE,
    INDEX idx_room_id (room_id),
    INDEX idx_primary (is_primary)
) ENGINE=InnoDB;

-- ============================================
-- TABLE: bookings
-- Stores booking records
-- ============================================
CREATE TABLE bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    receipt_number VARCHAR(50) NOT NULL UNIQUE,
    guest_id INT NOT NULL,
    user_id INT,
    room_type_id INT NOT NULL,
    room_id VARCHAR(50),
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    number_of_rooms INT DEFAULT 1,
    number_of_guests INT DEFAULT 1,
    total_amount DECIMAL(10, 2) NOT NULL,
    booking_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status ENUM('Pending', 'Confirmed', 'Checked-In', 'Checked-Out', 'Cancelled') DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (guest_id) REFERENCES guests(guest_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL,
    FOREIGN KEY (room_type_id) REFERENCES room_types(room_type_id) ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES room_inventory(room_id) ON DELETE SET NULL,
    INDEX idx_receipt (receipt_number),
    INDEX idx_guest (guest_id),
    INDEX idx_user (user_id),
    INDEX idx_check_in (check_in_date),
    INDEX idx_check_out (check_out_date),
    INDEX idx_status (status),
    INDEX idx_booking_date (booking_date)
) ENGINE=InnoDB;

-- ============================================
-- TABLE: payments
-- Stores payment information for bookings
-- ============================================
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    receipt_number VARCHAR(50) NOT NULL,
    payment_method ENUM('Credit Card', 'Debit Card', 'Cash', 'Bank Transfer', 'Online Payment') NOT NULL,
    account_name VARCHAR(255),
    reference_number VARCHAR(100),
    amount_paid DECIMAL(10, 2) NOT NULL,
    payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    payment_status ENUM('Pending', 'Completed', 'Failed', 'Refunded') DEFAULT 'Completed',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE,
    INDEX idx_booking (booking_id),
    INDEX idx_receipt (receipt_number),
    INDEX idx_payment_date (payment_date),
    INDEX idx_payment_status (payment_status)
) ENGINE=InnoDB;

-- ============================================
-- TABLE: discounts
-- Stores discount information applied to bookings
-- ============================================
CREATE TABLE discounts (
    discount_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    discount_type ENUM('NONE', 'SENIOR_CITIZEN', 'PWD', 'CHILD') DEFAULT 'NONE',
    discount_rate DECIMAL(5, 2) DEFAULT 0.00,
    discount_amount DECIMAL(10, 2) DEFAULT 0.00,
    original_price DECIMAL(10, 2) NOT NULL,
    final_price DECIMAL(10, 2) NOT NULL,
    vat_amount DECIMAL(10, 2) DEFAULT 0.00,
    vat_exempt BOOLEAN DEFAULT FALSE,
    id_number VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE,
    INDEX idx_booking (booking_id),
    INDEX idx_discount_type (discount_type)
) ENGINE=InnoDB;

-- ============================================
-- TABLE: room_statistics
-- Stores booking statistics for each room type
-- ============================================
CREATE TABLE room_statistics (
    stat_id INT AUTO_INCREMENT PRIMARY KEY,
    room_type_id INT NOT NULL,
    booking_count INT DEFAULT 0,
    total_revenue DECIMAL(15, 2) DEFAULT 0.00,
    last_booked_date DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (room_type_id) REFERENCES room_types(room_type_id) ON DELETE CASCADE,
    INDEX idx_room_type (room_type_id)
) ENGINE=InnoDB;

-- ============================================
-- TABLE: audit_log
-- Tracks all important system activities
-- ============================================
CREATE TABLE audit_log (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_type ENUM('admin', 'user', 'guest', 'system') NOT NULL,
    user_id INT,
    action VARCHAR(100) NOT NULL,
    table_name VARCHAR(50),
    record_id VARCHAR(50),
    old_values TEXT,
    new_values TEXT,
    ip_address VARCHAR(45),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_type (user_type),
    INDEX idx_user_id (user_id),
    INDEX idx_action (action),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB;

-- ============================================
-- INSERT DEFAULT DATA
-- ============================================

-- Insert default admin credentials
-- Default: username = admin@undousehotel.com, password = admin123 (hashed)
INSERT INTO admin_credentials (username, password_hash) VALUES 
('admin@undousehotel.com', '3eb3fe66b31e3b4d10fa70b5cad49c7112294af6ae4e476a1c405155d45aa121');

-- Insert room types
INSERT INTO room_types (room_type_name, capacity, base_price, description) VALUES
('Classic Room', '2', 5000.00, 'Perfect for 2 guests with garden view.'),
('Standard Deluxe Room', '2-4', 6500.00, 'Ideal for couples, includes balcony.'),
('Premier Deluxe Room', '4', 8000.00, 'Spacious room with pool view.'),
('Executive Suite', '5', 10000.00, 'Executive desk and lounge access.'),
('Family Suite', '8-10', 12000.00, 'Fits 4 people, kids stay free.'),
('Presidential Suite', '10-15', 20000.00, 'Luxury amenities and top floor view.');

-- Insert room inventory (3 units per room type)
INSERT INTO room_inventory (room_id, room_type_id, location, status, last_updated) VALUES
-- Classic Rooms
('CLA-001', 1, 'Floor 2', 'Available', NOW()),
('CLA-002', 1, 'Floor 3', 'Available', NOW()),
('CLA-003', 1, 'Floor 1', 'Available', NOW()),
-- Standard Deluxe Rooms
('STA-001', 2, 'Floor 2', 'Available', NOW()),
('STA-002', 2, 'Floor 3', 'Available', NOW()),
('STA-003', 2, 'Floor 1', 'Available', NOW()),
-- Premier Deluxe Rooms
('PRE-001', 3, 'Floor 2', 'Available', NOW()),
('PRE-002', 3, 'Floor 3', 'Available', NOW()),
('PRE-003', 3, 'Floor 1', 'Available', NOW()),
-- Executive Suites
('EXE-001', 4, 'Floor 2', 'Available', NOW()),
('EXE-002', 4, 'Floor 3', 'Available', NOW()),
('EXE-003', 4, 'Floor 1', 'Available', NOW()),
-- Family Suites
('FAM-001', 5, 'Floor 2', 'Available', NOW()),
('FAM-002', 5, 'Floor 3', 'Available', NOW()),
('FAM-003', 5, 'Floor 1', 'Available', NOW()),
-- Presidential Suites
('PRS-001', 6, 'Floor 2', 'Available', NOW()),
('PRS-002', 6, 'Floor 3', 'Available', NOW()),
('PRS-003', 6, 'Floor 1', 'Available', NOW());

-- Initialize room statistics
INSERT INTO room_statistics (room_type_id, booking_count, total_revenue)
SELECT room_type_id, 0, 0.00 FROM room_types;

-- ============================================
-- STORED PROCEDURES
-- ============================================

DELIMITER //

-- Procedure to create a new booking
CREATE PROCEDURE create_booking(
    IN p_receipt_number VARCHAR(50),
    IN p_guest_id INT,
    IN p_user_id INT,
    IN p_room_type_id INT,
    IN p_check_in_date DATE,
    IN p_check_out_date DATE,
    IN p_number_of_rooms INT,
    IN p_number_of_guests INT,
    IN p_total_amount DECIMAL(10,2),
    IN p_payment_method VARCHAR(50),
    IN p_account_name VARCHAR(255),
    IN p_reference_number VARCHAR(100)
)
BEGIN
    DECLARE v_booking_id INT;
    
    START TRANSACTION;
    
    -- Insert booking
    INSERT INTO bookings (receipt_number, guest_id, user_id, room_type_id, 
                         check_in_date, check_out_date, number_of_rooms, 
                         number_of_guests, total_amount, status)
    VALUES (p_receipt_number, p_guest_id, p_user_id, p_room_type_id, 
           p_check_in_date, p_check_out_date, p_number_of_rooms, 
           p_number_of_guests, p_total_amount, 'Confirmed');
    
    SET v_booking_id = LAST_INSERT_ID();
    
    -- Insert payment
    INSERT INTO payments (booking_id, receipt_number, payment_method, 
                         account_name, reference_number, amount_paid)
    VALUES (v_booking_id, p_receipt_number, p_payment_method, 
           p_account_name, p_reference_number, p_total_amount);
    
    -- Update room statistics
    UPDATE room_statistics 
    SET booking_count = booking_count + 1,
        total_revenue = total_revenue + p_total_amount,
        last_booked_date = NOW()
    WHERE room_type_id = p_room_type_id;
    
    -- Update user's total bookings if user_id is provided
    IF p_user_id IS NOT NULL THEN
        UPDATE users 
        SET total_bookings = total_bookings + 1 
        WHERE user_id = p_user_id;
    END IF;
    
    COMMIT;
    
    SELECT v_booking_id AS booking_id;
END //

-- Procedure to cancel a booking
CREATE PROCEDURE cancel_booking(IN p_receipt_number VARCHAR(50))
BEGIN
    DECLARE v_booking_id INT;
    DECLARE v_room_type_id INT;
    DECLARE v_total_amount DECIMAL(10,2);
    
    START TRANSACTION;
    
    -- Get booking details
    SELECT booking_id, room_type_id, total_amount 
    INTO v_booking_id, v_room_type_id, v_total_amount
    FROM bookings 
    WHERE receipt_number = p_receipt_number;
    
    -- Update booking status
    UPDATE bookings 
    SET status = 'Cancelled' 
    WHERE receipt_number = p_receipt_number;
    
    -- Update payment status
    UPDATE payments 
    SET payment_status = 'Refunded' 
    WHERE booking_id = v_booking_id;
    
    -- Update room statistics
    UPDATE room_statistics 
    SET booking_count = booking_count - 1,
        total_revenue = total_revenue - v_total_amount
    WHERE room_type_id = v_room_type_id;
    
    COMMIT;
END //

-- Procedure to get available rooms
CREATE PROCEDURE get_available_rooms(
    IN p_check_in_date DATE,
    IN p_check_out_date DATE
)
BEGIN
    SELECT 
        rt.room_type_id,
        rt.room_type_name,
        rt.base_price,
        rt.description,
        COUNT(ri.room_id) as available_count
    FROM room_types rt
    LEFT JOIN room_inventory ri ON rt.room_type_id = ri.room_type_id
    WHERE ri.status = 'Available'
    AND ri.room_id NOT IN (
        SELECT room_id FROM bookings 
        WHERE room_id IS NOT NULL
        AND status NOT IN ('Cancelled', 'Checked-Out')
        AND (
            (check_in_date <= p_check_out_date AND check_out_date >= p_check_in_date)
        )
    )
    GROUP BY rt.room_type_id, rt.room_type_name, rt.base_price, rt.description
    HAVING available_count > 0;
END //

DELIMITER ;

-- ============================================
-- VIEWS
-- ============================================

-- View for booking summary
CREATE VIEW booking_summary AS
SELECT 
    b.booking_id,
    b.receipt_number,
    CONCAT(g.first_name, ' ', g.last_name) AS guest_name,
    g.email,
    rt.room_type_name,
    b.check_in_date,
    b.check_out_date,
    b.number_of_rooms,
    b.number_of_guests,
    b.total_amount,
    b.status AS booking_status,
    p.payment_method,
    p.payment_status,
    b.booking_date
FROM bookings b
JOIN guests g ON b.guest_id = g.guest_id
JOIN room_types rt ON b.room_type_id = rt.room_type_id
LEFT JOIN payments p ON b.booking_id = p.booking_id;

-- View for room availability
CREATE VIEW room_availability AS
SELECT 
    rt.room_type_name,
    COUNT(CASE WHEN ri.status = 'Available' THEN 1 END) AS available,
    COUNT(CASE WHEN ri.status = 'Occupied' THEN 1 END) AS occupied,
    COUNT(CASE WHEN ri.status = 'Maintenance' THEN 1 END) AS maintenance,
    COUNT(CASE WHEN ri.status = 'Out of Service' THEN 1 END) AS out_of_service,
    COUNT(*) AS total_rooms,
    ROUND((COUNT(CASE WHEN ri.status = 'Occupied' THEN 1 END) * 100.0 / COUNT(*)), 2) AS occupancy_rate
FROM room_types rt
LEFT JOIN room_inventory ri ON rt.room_type_id = ri.room_type_id
GROUP BY rt.room_type_name;

-- View for revenue statistics
CREATE VIEW revenue_statistics AS
SELECT 
    rt.room_type_name,
    rs.booking_count,
    rs.total_revenue,
    rs.last_booked_date,
    ROUND(rs.total_revenue / NULLIF(rs.booking_count, 0), 2) AS avg_booking_value
FROM room_statistics rs
JOIN room_types rt ON rs.room_type_id = rt.room_type_id;

-- View for guest booking history
CREATE VIEW guest_booking_history AS
SELECT 
    g.guest_id,
    CONCAT(g.first_name, ' ', g.last_name) AS guest_name,
    g.email,
    g.phone,
    g.city,
    g.country,
    COUNT(b.booking_id) AS total_bookings,
    SUM(b.total_amount) AS total_spent,
    MAX(b.booking_date) AS last_booking_date
FROM guests g
LEFT JOIN bookings b ON g.guest_id = b.guest_id
GROUP BY g.guest_id, g.first_name, g.last_name, g.email, g.phone, g.city, g.country;

-- ============================================
-- TRIGGERS
-- ============================================

DELIMITER //

-- Trigger to update room status when booking is confirmed
CREATE TRIGGER after_booking_insert
AFTER INSERT ON bookings
FOR EACH ROW
BEGIN
    IF NEW.room_id IS NOT NULL THEN
        UPDATE room_inventory 
        SET status = 'Occupied' 
        WHERE room_id = NEW.room_id;
    END IF;
END //

-- Trigger to update room status when booking is cancelled
CREATE TRIGGER after_booking_update
AFTER UPDATE ON bookings
FOR EACH ROW
BEGIN
    IF NEW.status = 'Cancelled' AND OLD.room_id IS NOT NULL THEN
        UPDATE room_inventory 
        SET status = 'Available' 
        WHERE room_id = OLD.room_id;
    END IF;
    
    IF NEW.status = 'Checked-Out' AND OLD.room_id IS NOT NULL THEN
        UPDATE room_inventory 
        SET status = 'Available' 
        WHERE room_id = OLD.room_id;
    END IF;
END //

-- Trigger to log user activities
CREATE TRIGGER after_user_login
AFTER UPDATE ON users
FOR EACH ROW
BEGIN
    IF NEW.last_login_date != OLD.last_login_date THEN
        INSERT INTO audit_log (user_type, user_id, action, table_name, record_id)
        VALUES ('user', NEW.user_id, 'LOGIN', 'users', NEW.user_id);
    END IF;
END //

DELIMITER ;

-- ============================================
-- INDEXES FOR PERFORMANCE OPTIMIZATION
-- ============================================

-- Additional indexes for frequently queried columns
CREATE INDEX idx_booking_dates ON bookings(check_in_date, check_out_date);
CREATE INDEX idx_guest_name ON guests(first_name, last_name);
CREATE INDEX idx_payment_date_range ON payments(payment_date);

-- ============================================
-- GRANT PERMISSIONS (Adjust as needed)
-- ============================================

-- Create application user (optional)
-- CREATE USER 'undouse_app'@'localhost' IDENTIFIED BY 'your_secure_password';
-- GRANT SELECT, INSERT, UPDATE, DELETE ON undouse_hotel.* TO 'undouse_app'@'localhost';
-- FLUSH PRIVILEGES;

-- ============================================
-- END OF SCRIPT
-- ============================================

SELECT 'Database undouse_hotel created successfully!' AS message;
