package com.football.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity 
@Table(name = "quoc_tich")  
@Data // Tự động tạo Getter, Setter, Constructor
public class QuocTich {
    
    @Id // 
    @Column(name = "ma_qt")  
    private String maQT; 

    @Column(name = "ten_quoc_gia", length = 100, nullable = false) // Cột tên quốc gia, tối đa 100 ký tự, không được để trống (nullable = false)
    private String tenQuocGia;
}