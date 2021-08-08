package com.ngolamquangtin.appdatvexemphim;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.MessageResponse;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceUpdateStatusTicker extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences("datalogin", Context.MODE_PRIVATE);
        int idCustomer = Integer.parseInt(sp.getString("id", "0"));
        Calendar calendar = Calendar.getInstance();
        String curentDate = Util.formatDateClient(calendar.getTime());
        String currentTime = Util.formatTimeClient(calendar.getTime());

        updateStatusTicker(context, idCustomer, currentTime ,curentDate);
    }

    public void updateStatusTicker(Context context,int idCustomer, String currentTime, String currentDate){
        Service service = RetrofitUtil.getService(context);
        Call<MessageResponse> call = service.updateStausExpiredTicker(idCustomer,currentTime,currentDate);

        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if(response.body() != null && response.body().getCountTickerExpired() != 0) {
                    Util.createNotifycation(context, "Thông báo", "Bạn có "+response.body().getCountTickerExpired()+" không nhận vé");
                }

            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {

            }
        });

    }
}
