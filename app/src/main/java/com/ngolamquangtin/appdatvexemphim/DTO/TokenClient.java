package com.ngolamquangtin.appdatvexemphim.DTO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenClient {

    @SerializedName("token")
    @Expose
    private String token;

    public TokenClient(String token) {
        this.token = token;
    }

    public TokenClient() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
