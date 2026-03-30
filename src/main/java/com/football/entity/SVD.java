package com.football.entity;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "svd")
@Data
public class SVD {
    @Id
    @Column(name = "ma_san")
    private String maSan;

    @Column(name = "ten_san")
    private String tenSan;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "suc_chua")
    private int sucChua; 
}