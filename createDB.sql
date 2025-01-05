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
