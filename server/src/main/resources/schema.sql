-- Cài đặt extension pgcrypto nếu chưa có
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Tạo bảng bkav_user nếu chưa tồn tại
CREATE TABLE IF NOT EXISTS bkav_user (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(10) CHECK (role IN ('ADMIN', 'USER')) NOT NULL,
    gender VARCHAR(6) CHECK (gender IN ('MALE', 'FEMALE')) NOT NULL
);

-- Tạo bảng device nếu chưa tồn tại
CREATE TABLE IF NOT EXISTS device (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    image TEXT,
    category VARCHAR(20) CHECK (category IN ('LAPTOP', 'PC', 'MOUSE', 'PHONE')),
    status INT CHECK (status IN (-1, 0, 1)),
    bkav_user_id UUID REFERENCES bkav_user(id) ON DELETE SET NULL
);

-- Tạo view device_info_view nếu chưa tồn tại (cập nhật lại nếu đã tồn tại)
CREATE OR REPLACE VIEW device_info_view AS
SELECT
    d.id AS id,
    d.name AS name,
    d.description AS description,
    d.image AS image,
    d.category AS category,
    d.status AS status,
    u.username AS username
FROM
    device d
        LEFT JOIN
    bkav_user u ON d.bkav_user_id = u.id;
