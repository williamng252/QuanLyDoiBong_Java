package com.football.entity;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "hlv")
@Data
public class HLV {
    @Id
    @Column(name = "ma_hlv")
    private String maHLV;

    @Column(name = "ten_hlv")
    private String tenHLV;

    @Column(name = "bang_cap")
    private String bangCap; // VD: "GPLX Pro", "UEFA Pro"
}