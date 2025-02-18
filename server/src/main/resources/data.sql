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
    (gen_random_uuid(), 'LAP_00', 'Laptop Dell XPS 13.', 'LAPTOP', 0, (select id from bkav_user where username = 'nhatnt')),
    (gen_random_uuid(), 'IP_00', 'iPhone 14 Pro hỏng cam.', 'PHONE', 0, (select id from bkav_user where username = 'nhatnt')),
    (gen_random_uuid(), 'MOUSE_00', 'Logitech MX Master 3 mới.', 'MOUSE', 0, (select id from bkav_user where username = 'nhatnt')),
    (gen_random_uuid(), 'LAP_01', 'Lenovo ThinkPad T14.', 'LAPTOP', -1, NULL),
    (gen_random_uuid(), 'MOUSE_02', 'Razer DeathAdder V2 chơi game tốt.', 'MOUSE', -1, NULL)
    ON CONFLICT (name) DO NOTHING;