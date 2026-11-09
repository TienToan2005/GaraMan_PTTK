# GaraMan - Module Đặt lịch hẹn & Thống kê nhà cung cấp

Java 21 + Maven, Tomcat nhúng, H2, HTML/CSS/JS thuần.

## Chạy
    mvn -q compile exec:java      (chạy từ thư mục gốc project)
Mở http://localhost:8080/ (đổi cổng: -Dport=9090)

## Tài khoản (mật khẩu 123456)
- khach1, khach2 : khách hàng -> Đặt lịch hẹn
- quanli1        : quản lí   -> Báo cáo -> Thống kê nhà cung cấp theo lượng nhập (thử 01/05/2023 - 31/05/2023)
- kho1           : nhân viên kho

CSDL H2 tự tạo ở ./data/ (xóa thư mục này để reset).
