SELECT * FROM bkav_user

INSERT INTO bkav_user (id, username, password, name, role, gender)
VALUES (gen_random_uuid(), 'nhatnt', '$2y$11$IEe1lXgt6/0XPz5F9mI9ZOwdP2Xq3N.//qRUN8CG6gMqFAf2rcvl2', 'Nguyễn Thành Nhật', 'ADMIN', 'MALE');

INSERT INTO bkav_user (id, username, password, name, role, gender)
VALUES (gen_random_uuid(), 'huyendt', '$2y$11$IEe1lXgt6/0XPz5F9mI9ZOwdP2Xq3N.//qRUN8CG6gMqFAf2rcvl2', 'Chị Huyền', 'ADMIN', 'FEMALE');