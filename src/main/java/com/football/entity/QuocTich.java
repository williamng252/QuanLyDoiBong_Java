package com.football.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "quoc_tich")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuocTich {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_qt")
    private int maQT;

    @Column(name = "ten_quoc_gia", length = 100, nullable = false)
    private String tenQuocGia;
}