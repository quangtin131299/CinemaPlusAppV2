package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MovieType implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("tenLoai")
    @Expose
    private String tenloai;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTenLoai() {
        return tenloai;
    }

    public void setTenLoai(String tenloai) {
        this.tenloai = tenloai;
    }
}
