-- Du lieu mau. Mat khau cua moi tai khoan mau: 123456 (luu dang SHA-256).
INSERT INTO tblMember6 (id, username, password, fullName, phone, email, address) VALUES
 (1, 'khach1', LOWER(RAWTOHEX(HASH('SHA-256', STRINGTOUTF8('123456')))), 'Nguyễn Văn A', '0912345678', 'a@example.com', 'Hà Nội'),
 (2, 'quanli1', LOWER(RAWTOHEX(HASH('SHA-256', STRINGTOUTF8('123456')))), 'Phạm Quản Lý', '0900000001', 'ql@garaman.vn', 'Hà Nội'),
 (3, 'kho1', LOWER(RAWTOHEX(HASH('SHA-256', STRINGTOUTF8('123456')))), 'Lê Văn K', '0900000002', 'kho@garaman.vn', 'Hà Nội'),
 (4, 'khach2', LOWER(RAWTOHEX(HASH('SHA-256', STRINGTOUTF8('123456')))), 'Trần Thị B', '0987654321', 'b@example.com', 'Hải Phòng');
INSERT INTO tblCustomer6 (tblMember6id) VALUES (1), (4);
INSERT INTO tblStaff6 (tblMember6id, position) VALUES (2, 'Quản lí'), (3, 'Nhân viên kho');
INSERT INTO tblManager6 (tblMember6id) VALUES (2);
INSERT INTO tblInventoryStaff6 (tblMember6id) VALUES (3);

INSERT INTO tblSupplier6 (id, name, address, phone) VALUES
 (1, 'Phụ tùng Toyota Auto', 'Hà Nội', '0241111111'),
 (2, 'Phụ tùng Honda', 'Hà Nội', '0242222222'),
 (3, 'Lốp Michelin Việt Nam', 'Hải Phòng', '0313333333'),
 (4, 'Dầu nhớt Castrol', 'TP. Hồ Chí Minh', '0284444444');

INSERT INTO tblAccessory6 (id, name, description, stock, price) VALUES
 (1, 'Lốp xe Michelin 17inch', 'Lốp xe du lịch 17 inch', 120, 2000000),
 (2, 'Dầu nhớt Castrol 5W-30', 'Can 4 lít', 300, 450000),
 (3, 'Má phanh trước Toyota', 'Bộ 4 miếng', 80, 850000),
 (4, 'Lọc gió động cơ', 'Lọc gió Honda City', 150, 180000),
 (5, 'Bugi NGK', 'Bộ 4 bugi', 200, 320000);

INSERT INTO tblImportReceipt6 (id, importDate, note, tblSupplier6id, tblInventoryStaff6id) VALUES
 (1, DATE '2023-05-15', 'Nhập phụ tùng đợt 1', 1, 3),
 (2, DATE '2023-05-22', 'Nhập phụ tùng đợt 2', 1, 3),
 (3, DATE '2023-05-10', 'Nhập phụ tùng Honda', 2, 3),
 (4, DATE '2023-06-05', 'Nhập lốp xe', 3, 3),
 (5, DATE '2023-05-28', 'Nhập dầu nhớt', 4, 3);

INSERT INTO tblImportDetail6 (id, quantity, unitPrice, tblImportReceipt6id, tblAccessory6id) VALUES
 (1, 50, 2000000, 1, 1),
 (2, 30, 850000, 2, 3),
 (3, 70, 450000, 2, 2),
 (4, 100, 180000, 3, 4),
 (5, 100, 320000, 3, 5),
 (6, 40, 2000000, 4, 1),
 (7, 60, 450000, 5, 2);
