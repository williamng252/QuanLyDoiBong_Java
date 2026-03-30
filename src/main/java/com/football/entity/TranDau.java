package com.football.entity;
import javax.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "tran_dau")
@Data
public class TranDau {
    @Id
    @Column(name = "ma_tran")
    private String maTran;
    
    @Temporal(TemporalType.TIMESTAMP) // Lưu cả Ngày-Tháng-Năm lẫn Giờ-Phút-Giây đá bóng
    @Column(name = "ngay_gio")
    private Date ngayGio;

    @Column(name = "ti_so")
    private String tiSo; // VD: "2-1"

    @ManyToOne
    @JoinColumn(name = "ma_giai")
    private GiaiDau giaiDau;

    @ManyToOne
    @JoinColumn(name = "doi_chu_nha") // Đội bóng thứ 1 (Chủ nhà)
    private DoiBong doiChuNha;

    @ManyToOne
    @JoinColumn(name = "doi_khach") // Đội bóng thứ 2 (Khách)
    private DoiBong doiKhach;
}