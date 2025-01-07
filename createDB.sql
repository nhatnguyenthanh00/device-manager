--- Thêm extension để sử dụng gen_random_uuid()

CREATE EXTENSION IF NOT EXISTS pgcrypto;

--- Tạo bảng bkav_user

CREATE TABLE bkav_user (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),  
	-- Primary key với kiểu UUID, tự động tạo giá trị
    username VARCHAR(255) NOT NULL UNIQUE,          
	-- Username duy nhất
    password VARCHAR(255) NOT NULL,                 
	-- Mật khẩu mã hóa
    name VARCHAR(255) NOT NULL,                     
	-- Tên hiển thị, tên thật
    role VARCHAR(10) CHECK (role IN ('ADMIN', 'USER')) NOT NULL,  
	-- Vai trò (Admin, User)
    gender VARCHAR(6) CHECK (gender IN ('MALE', 'FEMALE')) NOT NULL  
	-- Giới tính
);

--- Tạo bảng device

CREATE TABLE device (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),  
	-- Primary key với kiểu UUID, tự động tạo giá trị
    name VARCHAR(255) NOT NULL UNIQUE,             
	-- Tên thiết bị, phải là duy nhất
    description TEXT,                              
	-- Mô tả thiết bị
    image TEXT,                                    
	-- Ảnh thiết bị (base64)
    category VARCHAR(50),                          
	-- Danh mục thiết bị (Laptop, PC, Mouse, ...)
    status INT NOT NULL CHECK (status IN (-1, 0, 1)),  
	-- Trạng thái thiết bị 
	--(-1: Unassigned, 0: Assigned, 1: Requested for return)
    bkav_user_id UUID REFERENCES bkav_user(id) ON DELETE SET NULL 
	-- Quan hệ với bảng `bkav_user`
);

--- Thêm data mẫu vào database

INSERT INTO bkav_user (id, username, password, name, role, gender)
VALUES
(gen_random_uuid(), 'nhatnt', '$2y$11$IEe1lXgt6/0XPz5F9mI9ZOwdP2Xq3N.//qRUN8CG6gMqFAf2rcvl2', 'Nguyễn Thành Nhật', 'ADMIN', 'MALE'),
(gen_random_uuid(), 'admin', '$2y$11$IEe1lXgt6/0XPz5F9mI9ZOwdP2Xq3N.//qRUN8CG6gMqFAf2rcvl2', 'Admin', 'ADMIN', 'MALE'),
(gen_random_uuid(), 'huyendt', '$2y$11$IEe1lXgt6/0XPz5F9mI9ZOwdP2Xq3N.//qRUN8CG6gMqFAf2rcvl2', 'Chị Huyền', 'USER', 'FEMALE');

INSERT INTO device (id, name, description, category, status, bkav_user_id)
VALUES
(gen_random_uuid(), 'Laptop Dell XPS 13', 'Laptop Dell hiệu suất cao, phù hợp cho công việc và giải trí.', 'LAPTOP', -1, NULL),
(gen_random_uuid(), 'iPhone 14 Pro', 'Smartphone cao cấp với camera đỉnh cao và hiệu năng mạnh mẽ.', 'PHONE', -1, NULL),
(gen_random_uuid(), 'Logitech MX Master 3', 'Chuột không dây dành cho dân văn phòng và sáng tạo.', 'MOUSE', 1, NULL),
(gen_random_uuid(), 'Lenovo ThinkPad T14', 'Laptop doanh nghiệp với độ bền cao và hiệu năng ổn định.', 'LAPTOP', -1, NULL),
(gen_random_uuid(), 'Razer DeathAdder V2', 'Chuột chơi game với thiết kế công thái học và cảm biến chính xác.', 'MOUSE', -1, NULL);

