package com.football.entity;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vi_tri")
@Data
public class ViTri {
    @Id
    @Column(name = "ma_vt")
    private String maVT; // VD: "TDO" (Tiền đạo), "HVE" (Hậu vệ)

    @Column(name = "ten_vi_tri")
    private String tenViTri;

    @Column(name = "ki_hieu")
    private String kiHieu; // VD: "ST", "CB"
}