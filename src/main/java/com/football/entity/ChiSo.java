package com.football.entity;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "chi_so")
@Data
public class ChiSo {
    @Id
    @Column(name = "ma_ct")
    private String maCT; // Cột này VỪA LÀ Khóa chính, VỪA LÀ Khóa ngoại. 

    private int theLuc;
    private int tocDo;
    private int dutDiem;
    private int chuyenBong;

    // Nối 1-1 với Cầu Thủ.
    @OneToOne
    @MapsId // Lấy luôn cái khóa chính "maCT" của Cầu thủ làm khóa chính cho bảng Chỉ số này.
    @JoinColumn(name = "ma_ct")
    private CauThu cauThu;
}