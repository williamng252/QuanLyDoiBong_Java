package com.football.entity;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "doi_bong")
@Data
public class DoiBong {
    @Id
    @Column(name = "ma_doi")
    private String maDoi;

    @Column(name = "ten_doi")
    private String ten;

    @Column(name = "nam_thanh_lap")
    private int namThanhLap;

    @Column(name = "logo")
    private String logo; // Lưu đường dẫn hình ảnh logo

    // QUAN HỆ 1-1: Một đội bóng chỉ có 1 HLV trưởng
    @OneToOne 
    @JoinColumn(name = "ma_hlv") // Tạo cột "ma_hlv" làm khóa ngoại trỏ sang bảng HLV
    private HLV hlv;

    // QUAN HỆ 1-1: Một đội bóng sở hữu 1 Sân vận động
    @OneToOne
    @JoinColumn(name = "ma_san") // Tạo cột "ma_san" làm khóa ngoại trỏ sang bảng SVD
    private SVD svd;
}