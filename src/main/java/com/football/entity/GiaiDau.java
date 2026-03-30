package com.football.entity;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "giai_dau")
@Data
public class GiaiDau {
    @Id
    @Column(name = "ma_giai")
    private String maGiai;

    @Column(name = "ten_giai")
    private String tenGiai;

    @Column(name = "mua_giai")
    private String muaGiai; // VD: "2023-2024"
}