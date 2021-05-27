package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SeatV2 implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("tenGhe")
    @Expose
    private String tenGhe;
    @SerializedName("idPhong")
    @Expose
    private Integer idPhong;
    @SerializedName("trangThai")
    @Expose
    private String trangThai;

    public Integer getId() {
        return id;
    }

    public SeatV2 setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTenGhe() {
        return tenGhe;
    }

    public SeatV2 setTenGhe(String tenGhe) {
        this.tenGhe = tenGhe;
        return this;
    }

    public Integer getIdPhong() {
        return idPhong;
    }

    public SeatV2 setIdPhong(Integer idPhong) {
        this.idPhong = idPhong;
        return this;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public SeatV2 setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        return this;
    }
}
