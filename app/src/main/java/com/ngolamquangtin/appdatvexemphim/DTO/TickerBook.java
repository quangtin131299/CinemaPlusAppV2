package com.ngolamquangtin.appdatvexemphim.DTO;

import java.io.Serializable;
import java.util.ArrayList;

public class TickerBook implements Serializable {

    private int idtickerbook;
    private int idsuat;
    private ArrayList<SeatV2> idSeats;
    private ArrayList<PopCorn> popcorns;
    private int idphim;
    private int idkhachhang;
    private int idrap;
    private int idhoadon;
    private String trangthai;
    private String ngaydat;
    private int idphong;
    private int methodPay;
    private int unitPrice;

    public TickerBook() {
        this.idtickerbook = 0;
        this.idsuat = 0;
        this.idSeats = new ArrayList<>();
        this.popcorns = new ArrayList<>();
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

    public TickerBook(int idtickerbook, int idsuat, ArrayList<SeatV2> idghe, int idphim, int idkhachhang, int idrap, int idhoadon, String trangthai, String ngaydat) {
        this.idtickerbook = idtickerbook;
        this.idsuat = idsuat;
        this.idSeats = idghe;
        this.idphim = idphim;
        this.idkhachhang = idkhachhang;
        this.idrap = idrap;
        this.idhoadon = idhoadon;
        this.trangthai = trangthai;
        this.ngaydat = ngaydat;

    }

    public ArrayList<PopCorn> getPopcorns() {
        return popcorns;
    }

    public void setPopcorns(ArrayList<PopCorn> popcorns) {
        this.popcorns = popcorns;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getMethodPay() {
        return methodPay;
    }

    public void setMethodPay(int methodPay) {
        this.methodPay = methodPay;
    }

    public int getIdphong() {
        return idphong;
    }

    public void setIdphong(int idphong) {
        this.idphong = idphong;
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

    public ArrayList<SeatV2> getIdSeats() {
        return idSeats;
    }

    public void setIdSeats(ArrayList<SeatV2> idSeats) {
        this.idSeats = idSeats;
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
