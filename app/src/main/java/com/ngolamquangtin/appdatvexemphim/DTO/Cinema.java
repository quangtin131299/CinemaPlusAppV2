package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Cinema implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("tenRap")
    @Expose
    private String tenrap;
    @SerializedName("hinh")
    @Expose
    private String hinh;
    @SerializedName("diaChi")
    @Expose
    private String diachi;
    @SerializedName("viDo")
    @Expose
    private String vido;
    @SerializedName("kinhDo")
    @Expose
    private String kinhdo;

    public Integer getId() {
        return id;
    }

    public Cinema setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTenrap() {
        return tenrap;
    }

    public Cinema setTenrap(String tenrap) {
        this.tenrap = tenrap;
        return this;
    }

    public String getHinh() {
        return hinh;
    }

    public Cinema setHinh(String hinh) {
        this.hinh = hinh;
        return this;
    }

    public String getVido() {
        return vido;
    }

    public Cinema setVido(String vido) {
        this.vido = vido;
        return this;
    }

    public String getKinhdo() {
        return kinhdo;
    }

    public Cinema setKinhdo(String kinhdo) {
        this.kinhdo = kinhdo;
        return this;
    }

    public String getDiachi() {
        return diachi;
    }

    public Cinema setDiachi(String diachi) {
        this.diachi = diachi;
        return this;
    }
}
