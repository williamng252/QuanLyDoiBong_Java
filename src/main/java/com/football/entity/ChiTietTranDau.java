package com.football.entity;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "chi_tiet_tran_dau")
@Data
public class ChiTietTranDau {
    
    @EmbeddedId // Gọi  "Lõi" tạo trong chitiettrandauid vào làm Khóa chính cho bảng này
    private ChiTietTranDauId id; 

    @Column(name = "so_phut_da")
    private int soPhutDa;

    @Column(name = "ban_thang")
    private int banThang;

    @Column(name = "kien_tao")
    private int kienTao;

    // Nối Khóa ngoại về bảng Cầu Thủ dựa trên nửa khóa "ma_ct"
    @ManyToOne
    @MapsId("maCT") 
    @JoinColumn(name = "ma_ct")
    private CauThu cauThu;

    // Nối Khóa ngoại về bảng Trận Đấu dựa trên nửa khóa "ma_tran"
    @ManyToOne
    @MapsId("maTran")
    @JoinColumn(name = "ma_tran")
    private TranDau tranDau;
}