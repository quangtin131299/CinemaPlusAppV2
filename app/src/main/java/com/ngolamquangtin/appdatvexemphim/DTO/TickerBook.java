package com.ngolamquangtin.appdatvexemphim.DTO;

import java.io.Serializable;

public class TickerBook implements Serializable {

    private int idtickerbook;
    private int idsuat;
    private int idghe;
    private int idphim;
    private int idkhachhang;
    private int idrap;
    private int idhoadon;
    private String trangthai;
    private String ngaydat;

    private int idphong;

    public int getIdphong() {
        return idphong;
    }

    public void setIdphong(int idphong) {
        this.idphong = idphong;
    }

    public TickerBook() {
        this.idtickerbook = 0;
        this.idsuat = 0;
        this.idghe = 0;
        this.idphim = 0;
        this.idkhachhang = 0;
        this.idrap = 0;
        this.idhoadon = 0;
        this.trangthai = "";
        this.ngaydat = "";
    }

    public int getIdhoadon() {
        return idhoadon;
    }

    public void setIdhoadon(int idhoadon) {
        this.idhoadon = idhoadon;
    }

    public TickerBook(int idtickerbook, int idsuat, int idghe, int idphim, int idkhachhang, int idrap, int idhoadon, String trangthai, String ngaydat) {
        this.idtickerbook = idtickerbook;
        this.idsuat = idsuat;
        this.idghe = idghe;
        this.idphim = idphim;
        this.idkhachhang = idkhachhang;
        this.idrap = idrap;
        this.idhoadon = idhoadon;
        this.trangthai = trangthai;
        this.ngaydat = ngaydat;
    }

    public int getIdtickerbook() {
        return idtickerbook;
    }

    public void setIdtickerbook(int idtickerbook) {
        this.idtickerbook = idtickerbook;
    }

    public int getIdsuat() {
        return idsuat;
    }

    public void setIdsuat(int idsuat) {
        this.idsuat = idsuat;
    }

    public int getIdghe() {
        return idghe;
    }

    public void setIdghe(int idghe) {
        this.idghe = idghe;
    }

    public int getIdphim() {
        return idphim;
    }

    public void setIdphim(int idphim) {
        this.idphim = idphim;
    }

    public int getIdkhachhang() {
        return idkhachhang;
    }

    public void setIdkhachhang(int idkhachhang) {
        this.idkhachhang = idkhachhang;
    }

    public int getIdrap() {
        return idrap;
    }

    public void setIdrap(int idrap) {
        this.idrap = idrap;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    public String getNgaydat() {
        return ngaydat;
    }

    public void setNgaydat(String ngaydat) {
        this.ngaydat = ngaydat;
    }

}
