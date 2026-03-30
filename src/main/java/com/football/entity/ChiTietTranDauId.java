package com.football.entity;
import javax.persistence.*;
import lombok.Data;
import java.io.Serializable;

@Embeddable // Lõi.
@Data
public class ChiTietTranDauId implements Serializable { // Dùng implements Serializable để truyền dữ liệu
    
    @Column(name = "ma_ct")
    private String maCT; // Nửa khóa thứ nhất
    
    @Column(name = "ma_tran")
    private String maTran; // Nửa khóa thứ hai
}