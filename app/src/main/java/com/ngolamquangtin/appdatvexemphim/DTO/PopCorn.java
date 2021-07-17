package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PopCorn  implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("tenCombo")
    @Expose
    private String comboName;
    @SerializedName("donGia")
    @Expose
    private int unitPrice;
    @SerializedName("hinh")
    @Expose
    private String image;
    @SerializedName("moTa")
    @Expose
    private String description;
    private int count;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComboName() {
        return comboName;
    }

    public void setComboName(String comboName) {
        this.comboName = comboName;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int calulatorAmount(){
        return unitPrice * count;
    }
}
