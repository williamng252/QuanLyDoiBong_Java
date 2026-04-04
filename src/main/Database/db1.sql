USE ql_doibong;

-- 1. Quốc tịch
INSERT IGNORE INTO quoc_tich (ma_qt, ten_quoc_gia) VALUES 
('GB', N'Vương Quốc Anh'), ('AR', N'Argentina'), ('PT', N'Bồ Đào Nha'),
('TH', N'Thái Lan'),       ('KR', N'Hàn Quốc'),  ('JP', N'Nhật Bản'),
('FR', N'Pháp'),           ('BR', N'Brazil'),    ('DE', N'Đức'),
('VN', N'Việt Nam');

-- 2. Vị trí
INSERT IGNORE INTO vi_tri (ma_vt, ten_vi_tri, ki_hieu) VALUES 
('VT01', N'Tiền đạo', 'ST'),    ('VT02', N'Tiền vệ', 'CM'), 
('VT03', N'Hậu vệ', 'CB'),      ('VT04', N'Thủ môn', 'GK'),
('VT05', N'Tiền vệ cánh', 'LW'), ('VT06', N'Hậu vệ biên', 'LB');

-- 3. Huấn luyện viên
INSERT IGNORE INTO hlv (ma_hlv, ten_hlv, bang_cap) VALUES 
('HLV01', N'Chu Đình Nghiêm', 'AFC Pro'),
('HLV02', N'Pep Guardiola', 'UEFA Pro'),
('HLV03', N'Kiatisuk Senamuang', 'AFC Pro'),
('HLV04', N'Jurgen Klopp', 'UEFA Pro'),
('HLV05', N'Gong Oh-kyun', 'AFC Pro'),
('HLV06', N'Park Hang-seo', 'AFC Pro');

-- 4. Sân vận động
INSERT IGNORE INTO svd (ma_san, ten_san, dia_chi, suc_chua) VALUES 
('SVD01', N'Lạch Tray', N'Hải Phòng', 30000),
('SVD02', N'Etihad', N'Manchester', 53000),
('SVD03', N'Thống Nhất', N'TP.HCM', 15000),
('SVD04', N'Mỹ Đình', N'Hà Nội', 40000),
('SVD05', N'Anfield', N'Liverpool', 54000);

-- 5. Giải đấu
INSERT IGNORE INTO giai_dau (ma_giai, ten_giai, mua_giai) VALUES 
('VLEAGUE', N'V-League 1', '2023-2024'),
('EPL', N'Premier League', '2023-2024'),
('LALIGA', N'La Liga', '2023-2024'),
('WORLD_CUP', N'World Cup', '2022');

-- 6. Đội bóng
INSERT IGNORE INTO doi_bong (ma_doi, ten_doi, nam_thanh_lap, logo, diem_so, hieu_so, ma_hlv, ma_san) VALUES
('DB01', N'Hải Phòng FC', 1952, 'haiphongfc.png', 12, 5, 'HLV03', 'SVD01'),
('DB02', N'Manchester City', 1880, 'mancityfc.png', 20, 15, 'HLV02', 'SVD02'),
('DB03', N'Hà Nội FC', 2006, 'hanoi.png', 15, 10, 'HLV01', 'SVD04'),
('DB04', N'Liverpool', 1892, 'liverpool.png', 18, 12, 'HLV04', 'SVD05');

-- 7. Cầu thủ
INSERT IGNORE INTO cau_thu (ma_ct, ho_ten, ngay_sinh, chieu_cao, can_nang, so_ao, ma_qt, ma_vt, ma_doi) VALUES 
('CT01', N'Nguyễn Văn Toản', '1999-11-26', 1.86, 78, 1, 'VN', 'VT04', 'DB01'),
('CT02', N'Kevin De Bruyne', '1991-06-28', 1.81, 75, 17, 'GB', 'VT02', 'DB02'),
('CT03', N'Erling Haaland', '2000-07-21', 1.94, 88, 9, 'GB', 'VT01', 'DB02'),
('CT04', N'Nguyễn Quang Hải', '1997-04-12', 1.68, 65, 19, 'VN', 'VT02', 'DB03'),
('CT05', N'Đỗ Hùng Dũng', '1993-09-08', 1.70, 68, 88, 'VN', 'VT02', 'DB03'),
('CT06', N'Mohamed Salah', '1992-06-15', 1.75, 71, 11, 'DE', 'VT05', 'DB04'),
('CT07', N'Virgil van Dijk', '1991-07-08', 1.93, 92, 4, 'DE', 'VT03', 'DB04');

-- 8. Chỉ số (Sử dụng camelCase cho cột: theLuc, tocDo, dutDiem, chuyenBong)
INSERT IGNORE INTO chi_so (ma_ct, theLuc, tocDo, dutDiem, chuyenBong) VALUES 
('CT01', 75, 60, 20, 70), ('CT02', 85, 78, 88, 95), ('CT03', 95, 94, 98, 75),
('CT04', 80, 75, 85, 90), ('CT05', 85, 72, 70, 88), ('CT06', 90, 93, 90, 82), 
('CT07', 92, 85, 60, 80);

-- 9. Hợp đồng
INSERT IGNORE INTO hop_dong (ma_hd, ngay_ky, ngay_het_han, gia_tri, ma_ct, ma_doi) VALUES 
('HD01', '2022-01-01', '2025-01-01', 2000000,   'CT01', 'DB01'),
('HD02', '2023-06-01', '2028-06-01', 150000000, 'CT02', 'DB02'),
('HD03', '2021-01-01', '2025-01-01', 120000000, 'CT05', 'DB03'),
('HD04', '2023-01-01', '2026-01-01', 500000,    'CT04', 'DB03'),
('HD05', '2022-06-01', '2027-06-01', 150000000, 'CT03', 'DB02');

-- 10. Trận đấu
INSERT IGNORE INTO tran_dau (ma_tran, ngay_gio, ti_so, ban_thang_doi_nha, ban_thang_doi_khach, ma_giai, doi_chu_nha, doi_khach) VALUES 
('T01', '2024-04-10 19:00:00', '1-2', 1, 2, 'EPL',     'DB03', 'DB02'),
('T02', '2024-03-05 21:00:00', '3-3', 3, 3, 'EPL',     'DB02', 'DB04'),
('T03', '2024-03-10 18:00:00', '1-0', 1, 0, 'VLEAGUE', 'DB01', 'DB03'),
('T04', '2024-03-01 19:00:00', '2-1', 2, 1, 'VLEAGUE', 'DB03', 'DB01');

-- 11. Chi tiết trận đấu
INSERT IGNORE INTO chi_tiet_tran_dau (ma_ct, ma_tran, so_phut_da, ban_thang, kien_tao) VALUES 
('CT01', 'T01', 90, 0, 0), ('CT02', 'T01', 80, 0, 2), ('CT03', 'T01', 90, 2, 0),
('CT04', 'T02', 90, 2, 0), ('CT05', 'T02', 90, 0, 2), ('CT06', 'T02', 80, 1, 1),
('CT07', 'T03', 90, 0, 0);
use ql_doibong;
UPDATE doi_bong SET logo = 'haiphongfc.png' WHERE ma_doi = 'DB01';
update doi_bong set logo = 'hanoi.png' where ma_doi = 'DB03';
update doi_bong set logo = 'mancityfc.png' where ma_doi = 'DB02';
USE ql_doibong;

UPDATE doi_bong
SET
    ten_doi = N'Hà Nội FC',
    nam_thanh_lap = 2006,
    ma_hlv = 'HLV01', -- Chu Đình Nghiêm
    ma_san = 'SVD04', -- Mỹ Đình
    diem_so = 15,
    hieu_so = 10,
    logo = 'hanoi.png'
WHERE ma_doi = 'DB03';