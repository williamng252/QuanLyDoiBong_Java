package com.football.entity;
import javax.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "hop_dong")
@Data
public class HopDong {
    @Id
    @Column(name = "ma_hd")
    private String maHD;

    @Temporal(TemporalType.DATE)
    @Column(name = "ngay_ky")
    private Date ngayKy;

    @Temporal(TemporalType.DATE)
    @Column(name = "ngay_het_han")
    private Date ngayHetHan;

    @Column(name = "gia_tri")
    private double giaTri;

    // Nhiều Hợp đồng có thể thuộc về 1 Cầu thủ (Cầu thủ gia hạn nhiều lần)
    @ManyToOne
    @JoinColumn(name = "ma_ct")
    private CauThu cauThu;

    // Nhiều Hợp đồng thuộc về 1 Đội bóng
    @ManyToOne
    @JoinColumn(name = "ma_doi")
    private DoiBong doiBong;
}