package com.football;

import com.football.dao.*;
import com.football.entity.CauThu;
import com.football.entity.DoiBong;
import com.football.entity.HopDong;
import com.football.entity.TranDau;

import java.util.Date;
import java.util.List;

public class MainApp {
    public static void main(String[] args) {
        System.out.println("=====================================================");
        System.out.println("🚀 HỆ THỐNG KIỂM THỬ TỔNG LỰC (FULL SYSTEM TEST) 🚀");
        System.out.println("=====================================================\n");

        // Khởi tạo các DAO cần thiết
        DoiBongDAO doiBongDAO = new DoiBongDAO();
        TranDauDAO tranDauDAO = new TranDauDAO();
        CauThuDAO cauThuDAO = new CauThuDAO();
        ChiTietTranDauDAO chiTietDAO = new ChiTietTranDauDAO();

        // ---------------------------------------------------------
        // KỊCH BẢN 1: TRẬN SIÊU KINH ĐIỂN & CẬP NHẬT BXH
        // ---------------------------------------------------------
        System.out.println(">>> [Kịch bản 1]: MU vs Real Madrid (Tỉ số 3-1)");
        
        // Lấy 2 đội từ DB (Giả sử mã là 'MU' và 'RM' đã có trong DB)
        DoiBong mu = doiBongDAO.getByID("MU");
        DoiBong real = doiBongDAO.getByID("RM");

        if (mu != null && real != null) {
            TranDau td = new TranDau();
            td.setMaTran("MATCH_001");
            td.setDoiChuNha(mu);
            td.setDoiKhach(real);
            td.setBanThangDoiNha(3);
            td.setBanThangDoiKhach(1);
            td.setNgayGio(new Date());

            // Chạy hàm logic "não to" nhất: Cập nhật kết quả & tính điểm
            boolean checkTD = tranDauDAO.capNhatKetQua(td);
            System.out.println(checkTD ? "✅ Đã lưu trận đấu và cộng 3 điểm cho MU!" : "❌ Lỗi cập nhật trận đấu.");
        }

        // ---------------------------------------------------------
        // KỊCH BẢN 2: XEM BẢNG XẾP HẠNG SAU TRẬN ĐẤU
        // ---------------------------------------------------------
        System.out.println("\n>>> [Kịch bản 2]: Xem Bảng Xếp Hạng Mới Nhất");
        List<DoiBong> bxh = doiBongDAO.getBangXepHang();
        System.out.printf("%-10s | %-5s | %-5s\n", "TÊN ĐỘI", "ĐIỂM", "HS");
        for (DoiBong db : bxh) {
            System.out.printf("%-10s | %-5d | %-5d\n", db.getTen(), db.getDiem(), db.getHieuSo());
        }

        // ---------------------------------------------------------
        // KỊCH BẢN 3: CHUYỂN NHƯỢNG RONALDO (MU -> RM)
        // ---------------------------------------------------------
        System.out.println("\n>>> [Kịch bản 3]: Chuyển nhượng CT01 (Ronaldo) sang Real Madrid");
        HopDong hd = new HopDong();
        hd.setMaHD("HD_MARCA_2026");
        hd.setNgayKy(new Date());
        hd.setGiaTri(150000000.0);

        boolean checkCN = cauThuDAO.thucHienChuyenNhuong("CT01", "RM", hd);
        System.out.println(checkCN ? "✅ Ronaldo đã chính thức gia nhập Real Madrid!" : "❌ Lỗi chuyển nhượng.");

        // ---------------------------------------------------------
        // KỊCH BẢN 4: THỐNG KÊ VUA PHÁ LƯỚI (TOP 3)
        // ---------------------------------------------------------
        System.out.println("\n>>> [Kịch bản 4]: Danh sách Vua Phá Lưới");
        List<Object[]> topSearch = chiTietDAO.getTopGhiBan(3);
        for (Object[] obj : topSearch) {
            CauThu ct = (CauThu) obj[0];
            Long soBan = (Long) obj[1];
            System.out.println(" - " + ct.getTen() + " (" + ct.getDoiBong().getTen() + "): " + soBan + " bàn.");
        }

        System.out.println("\n=====================================================");
        System.out.println("🏁 HOÀN TẤT TEST ALL - BACKEND SẴN SÀNG LÊN SÀN! 🏁");
        System.out.println("=====================================================");
        
        // Khởi động Giao diện Swing (GUI)
        javax.swing.SwingUtilities.invokeLater(() -> {
            com.football.view.MainFrame mainFrame = new com.football.view.MainFrame();
            mainFrame.setVisible(true);
        });
    }
}