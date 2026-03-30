package com.football.entity;
import javax.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "cau_thu")
@Data
public class CauThu {
    @Id
    @Column(name = "ma_ct")
    private String maCT; // Khóa chính kiểu String (VD: "CT01")
    
    @Column(name = "ho_ten")
    private String ten;

    @Temporal(TemporalType.DATE) // lưu Ngày-Tháng-Năm 
    @Column(name = "ngay_sinh")
    private Date ngaySinh;

    @Column(name = "chieu_cao")
    private double chieuCao;

    @Column(name = "can_nang")
    private double canNang;
    
    @Column(name = "so_ao")
    private int soAo; // 

    // QUAN HỆ NHIỀU - 1: Nhiều cầu thủ mang cùng 1 Quốc tịch
    @ManyToOne 
    @JoinColumn(name = "ma_qt") // Tạo cột "ma_qt" làm khóa ngoại
    private QuocTich quocTich;

    // QUAN HỆ NHIỀU - 1: Nhiều cầu thủ đá cùng 1 Vị trí
    @ManyToOne
    @JoinColumn(name = "ma_vt")
    private ViTri viTri;

    // QUAN HỆ NHIỀU - 1: Nhiều cầu thủ thuộc về 1 Đội bóng
    @ManyToOne
    @JoinColumn(name = "ma_doi")
    private DoiBong doiBong;
}