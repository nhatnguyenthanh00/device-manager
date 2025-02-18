-- Cài đặt extension pgcrypto nếu chưa có
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Insert sample data into bkav_user
INSERT INTO bkav_user (id, username, password, name, role, gender)
VALUES
    (gen_random_uuid(), 'nhatnt', '$2y$11$IEe1lXgt6/0XPz5F9mI9ZOwdP2Xq3N.//qRUN8CG6gMqFAf2rcvl2', 'Nguyễn Thành Nhật', 'USER', 'MALE'),
    (gen_random_uuid(), 'admin', '$2y$11$IEe1lXgt6/0XPz5F9mI9ZOwdP2Xq3N.//qRUN8CG6gMqFAf2rcvl2', 'Admin', 'ADMIN', 'MALE'),
    (gen_random_uuid(), 'huyendt', '$2y$11$IEe1lXgt6/0XPz5F9mI9ZOwdP2Xq3N.//qRUN8CG6gMqFAf2rcvl2', 'Chị Huyền', 'USER', 'FEMALE')
    ON CONFLICT (username) DO NOTHING;

-- Insert sample data into device
INSERT INTO device (id, name, description, category, status, bkav_user_id)
VALUES
    (gen_random_uuid(), 'Laptop Dell XPS 13', 'Laptop Dell hiệu suất cao, phù hợp cho công việc và giải trí.', 'LAPTOP', 0, (select id from bkav_user where username = 'nhatnt')),
    (gen_random_uuid(), 'iPhone 14 Pro', 'Smartphone cao cấp với camera đỉnh cao và hiệu năng mạnh mẽ.', 'PHONE', 0, (select id from bkav_user where username = 'nhatnt')),
    (gen_random_uuid(), 'Logitech MX Master 3', 'Chuột không dây dành cho dân văn phòng và sáng tạo.', 'MOUSE', 0, (select id from bkav_user where username = 'nhatnt')),
    (gen_random_uuid(), 'Lenovo ThinkPad T14', 'Laptop doanh nghiệp với độ bền cao và hiệu năng ổn định.', 'LAPTOP', -1, NULL),
    (gen_random_uuid(), 'Razer DeathAdder V2', 'Chuột chơi game với thiết kế công thái học và cảm biến chính xác.', 'MOUSE', -1, NULL)
    ON CONFLICT (name) DO NOTHING;