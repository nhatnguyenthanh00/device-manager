
-- Create the user if it does not exist
DO
$$
BEGIN
   IF NOT EXISTS (
      SELECT 
      FROM   pg_catalog.pg_roles
      WHERE  rolname = 'nhatnt') THEN

      CREATE USER nhatnt WITH LOGIN PASSWORD 'password';
   END IF;
END
$$;

-- Grant all privileges on the database to the user
GRANT ALL PRIVILEGES ON DATABASE "TestDB" TO nhatnt;

-- Enable the pgcrypto extension for UUID generation
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Create the bkav_user table
CREATE TABLE bkav_user (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    role VARCHAR(10) CHECK (role IN ('ADMIN', 'USER')) NOT NULL,
    gender VARCHAR(6) CHECK (gender IN ('MALE', 'FEMALE')) NOT NULL
);

-- Create the device table
CREATE TABLE device (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    image TEXT,
    category VARCHAR(50),
    status INT NOT NULL CHECK (status IN (-1, 0, 1)),
    bkav_user_id UUID REFERENCES bkav_user(id) ON DELETE SET NULL
);

-- Insert sample data into bkav_user
INSERT INTO bkav_user (id, username, password, name, role, gender)
VALUES
(gen_random_uuid(), 'nhatnt', '$2y$11$IEe1lXgt6/0XPz5F9mI9ZOwdP2Xq3N.//qRUN8CG6gMqFAf2rcvl2', 'Nguyễn Thành Nhật', 'ADMIN', 'MALE'),
(gen_random_uuid(), 'admin', '$2y$11$IEe1lXgt6/0XPz5F9mI9ZOwdP2Xq3N.//qRUN8CG6gMqFAf2rcvl2', 'Admin', 'ADMIN', 'MALE'),
(gen_random_uuid(), 'huyendt', '$2y$11$IEe1lXgt6/0XPz5F9mI9ZOwdP2Xq3N.//qRUN8CG6gMqFAf2rcvl2', 'Chị Huyền', 'USER', 'FEMALE');

-- Insert sample data into device
INSERT INTO device (id, name, description, category, status, bkav_user_id)
VALUES
(gen_random_uuid(), 'Laptop Dell XPS 13', 'Laptop Dell hiệu suất cao, phù hợp cho công việc và giải trí.', 'LAPTOP', -1, NULL),
(gen_random_uuid(), 'iPhone 14 Pro', 'Smartphone cao cấp với camera đỉnh cao và hiệu năng mạnh mẽ.', 'PHONE', -1, NULL),
(gen_random_uuid(), 'Logitech MX Master 3', 'Chuột không dây dành cho dân văn phòng và sáng tạo.', 'MOUSE', 1, NULL),
(gen_random_uuid(), 'Lenovo ThinkPad T14', 'Laptop doanh nghiệp với độ bền cao và hiệu năng ổn định.', 'LAPTOP', -1, NULL),
(gen_random_uuid(), 'Razer DeathAdder V2', 'Chuột chơi game với thiết kế công thái học và cảm biến chính xác.', 'MOUSE', -1, NULL);
