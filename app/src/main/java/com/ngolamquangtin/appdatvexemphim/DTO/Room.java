package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Room {
    //    private int id;
//    private String tenphong;
//    private String thoigian;
//
//    public String getThoigian() {
//        return thoigian;
//    }
//
//    public void setThoigian(String thoigian) {
//        this.thoigian = thoigian;
//    }
//
//    public Room(int id, String tenphong) {
//        this.id = id;
//        this.tenphong = tenphong;
//    }
//
//    public Room() {
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getTenphong() {
//        return tenphong;
//    }
//
//    public void setTenphong(String tenphong) {
//        this.tenphong = tenphong;
//    }
    @SerializedName("Gio")
    @Expose
    private String thoigian;
    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("TenPhong")
    @Expose
    private String tenphong;

    public Room() {
    }

    public Room(String thoigian, Integer id, String tenphong) {
        this.thoigian = thoigian;
        this.id = id;
        this.tenphong = tenphong;
    }

    public String getThoigian() {
        return thoigian;
    }

    public Room setThoigian(String thoigian) {
        this.thoigian = thoigian;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public Room setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTenphong() {
        return tenphong;
    }

    public Room setTenphong(String tenphong) {
        this.tenphong = tenphong;
        return this;
    }
}
