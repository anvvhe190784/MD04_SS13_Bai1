# Session 13: Xây dựng hệ thống xác thực Microservices với JWT

Hệ thống bao gồm các dịch vụ:
- `identity-service`: Quản lý người dùng, băm mật khẩu BCrypt, cấp phát và xác thực JWT.
- `gateway-service`: API Gateway (Spring Cloud Gateway WebFlux) định tuyến, chặn bắt và kiểm tra tính hợp lệ của JWT, trích xuất và chuyển tiếp User Context.
- `product-service`: Dịch vụ đích tiếp nhận request và phân quyền dựa trên User Context (`X-User-Role`).

---

## [Bài tập 1] Khởi tạo Identity Service và Bảo mật mật khẩu
- Quản lý entity `User` (id: UUID, username, password, role).
- Băm mật khẩu an toàn với `BCryptPasswordEncoder`.
- Cấu hình Spring Security tắt CSRF và cho phép truy cập công khai endpoint đăng ký.
- API: `POST /api/auth/register` tiếp nhận thông tin người dùng và lưu vào database PostgreSQL `identity_db`.
