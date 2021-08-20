package com.ngolamquangtin.appdatvexemphim.Config;

import android.content.Context;

import com.ngolamquangtin.appdatvexemphim.Service.Service;

import retrofit2.Retrofit;

public class RetrofitUtil {
    private static final String BASE_URL = "https://cinemaplusapi.herokuapp.com/";

    public static Service getService(Context context){
        return RetrofitConfig.getRetrofit(context,BASE_URL).create(Service.class);
    }
}
