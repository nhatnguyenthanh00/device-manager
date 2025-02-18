Tạo database mới tên là "device_manage"

Tạo 1 user mới trong PostgreSQL với đủ quyền
usename : nhatnt
password : password

Nếu bạn muốn username,password khác thì hãy chỉnh sửa tương ứng trong 
server\src\main\resources\application.properties
spring.datasource.username= your_user_name
spring.datasource.password= your_pass_word

Chạy backend => http://localhost:8080
Lấy token từ /api/login truyền vào các authorization Bearer để có thể chạy được

Default password : admin
