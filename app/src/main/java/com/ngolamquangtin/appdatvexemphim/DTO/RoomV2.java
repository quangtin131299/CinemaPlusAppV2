package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RoomV2 implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer Id;
    @SerializedName("tenPhong")
    @Expose
    private String tenphong;
    @SerializedName("idRap")
    @Expose
    private Integer idrap;

    public Integer getId() {
        return Id;
    }

    public RoomV2 setId(Integer id) {
        Id = id;
        return this;
    }

    public String getTenphong() {
        return tenphong;
    }

    public RoomV2 setTenphong(String tenphong) {
        this.tenphong = tenphong;
        return this;
    }

    public Integer getIdrap() {
        return idrap;
    }

    public RoomV2 setIdrap(Integer idrap) {
        this.idrap = idrap;
        return this;
    }
}
