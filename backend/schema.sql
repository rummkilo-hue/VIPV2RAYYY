-- VIPV2RAY PostgreSQL Database Schema

CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

INSERT INTO roles (name) VALUES ('Super Admin'), ('Admin'), ('User') ON CONFLICT DO NOTHING;

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role_id INT REFERENCES roles(id) DEFAULT 3,
    is_active BOOLEAN DEFAULT TRUE,
    traffic_limit_bytes BIGINT DEFAULT 107374182400, -- 100GB default
    used_traffic_bytes BIGINT DEFAULT 0,
    max_devices INT DEFAULT 5,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Default Super Admin Account Seed
INSERT INTO users (username, email, password_hash, role_id)
VALUES ('@error_rum', 'error_rum@vipv2ray.com', '$2b$10$e8pA1.G45P32lQvA2eB8/.sQx9B92qVpXk0p8W1V/3x4k1v4k1v4k', 1)
ON CONFLICT (username) DO NOTHING;

CREATE TABLE IF NOT EXISTS servers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    server_name VARCHAR(100) NOT NULL,
    country_code VARCHAR(10) NOT NULL,
    flag_emoji VARCHAR(10) NOT NULL,
    ip_address VARCHAR(45) NOT NULL,
    port INT NOT NULL,
    protocol VARCHAR(50) NOT NULL, -- VLESS, VMess, Trojan, Hysteria2, etc.
    capacity_limit INT DEFAULT 1000,
    current_online_users INT DEFAULT 0,
    ping_ms INT DEFAULT 30,
    is_premium_only BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS v2ray_configs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    server_id UUID REFERENCES servers(id) ON DELETE CASCADE,
    config_name VARCHAR(100) NOT NULL,
    protocol VARCHAR(50) NOT NULL,
    raw_uri TEXT NOT NULL,
    is_favorite BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS traffic_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    server_id UUID REFERENCES servers(id) ON DELETE SET NULL,
    download_bytes BIGINT DEFAULT 0,
    upload_bytes BIGINT DEFAULT 0,
    session_duration_seconds INT DEFAULT 0,
    logged_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE SET NULL,
    action VARCHAR(255) NOT NULL,
    ip_address VARCHAR(45),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
