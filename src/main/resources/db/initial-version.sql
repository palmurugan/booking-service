-- Checkout Session
CREATE TABLE checkout_sessions (
    id UUID PRIMARY KEY,
    org_id UUID NOT NULL,
    user_id UUID NOT NULL,
    service_id UUID NOT NULL,
    provider_id UUID NOT NULL,
    slot_id UUID NOT NULL,
    selected_addons JSONB,
    applied_coupon VARCHAR(50),
    subtotal DECIMAL(10, 2) NOT NULL,
    discount DECIMAL(10, 2) DEFAULT 0,
    tax DECIMAL(10, 2) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bookings
CREATE TABLE bookings (
    id UUID PRIMARY KEY,
    org_id UUID NOT NULL,
    user_id UUID NOT NULL,
    service_id UUID NOT NULL,
    provider_id UUID NOT NULL,
    slot_id UUID NOT NULL,
    booking_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    payment_id UUID,
    special_instructions TEXT,
    cancellation_reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

alter table booking.checkout_sessions add column is_active boolean;
alter table booking.checkout_sessions drop column tax;

alter table booking.bookings add column is_active boolean;

-------------------------------------------------------------------------------------------------------------

-- Payments
CREATE TABLE payments (
    payment_id UUID PRIMARY KEY,
    booking_id UUID NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'INR',
    payment_method VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    transaction_id VARCHAR(100),
    gateway_response JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_booking_id (booking_id),
    INDEX idx_status (status),
    INDEX idx_transaction_id (transaction_id),
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id)
);

-- Coupons
CREATE TABLE coupons (
    coupon_id UUID PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    discount_type VARCHAR(20) NOT NULL,
    discount_value DECIMAL(10, 2) NOT NULL,
    min_order_amount DECIMAL(10, 2),
    max_discount DECIMAL(10, 2),
    valid_from TIMESTAMP NOT NULL,
    valid_to TIMESTAMP NOT NULL,
    usage_limit INT,
    used_count INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_code (code),
    INDEX idx_valid_dates (valid_from, valid_to)
);