package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Seat {

    @SerializedName("ID")
    @Expose
    private int id;
    @SerializedName("TenGhe")
    @Expose
    private String tenghe;
    @SerializedName("TrangThai")
    @Expose
    private String trangthai;

    public Seat(int id, String tenghe, String trangthai) {
        this.id = id;
        this.tenghe = tenghe;
        this.trangthai = trangthai;
    }

    public Seat() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenghe() {
        return tenghe;
    }

    public void setTenghe(String tenghe) {
        this.tenghe = tenghe;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }


}
