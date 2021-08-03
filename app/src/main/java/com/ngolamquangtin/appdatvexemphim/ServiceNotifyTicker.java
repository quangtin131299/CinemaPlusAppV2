package com.ngolamquangtin.appdatvexemphim;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.TicketV2;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceNotifyTicker extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        loadTickerOfCustomer(context);
    }


    public void loadTickerOfCustomer(Context context){
        Calendar calendar = Calendar.getInstance();
        Date curentDate = calendar.getTime();
        String tempngay = Util.formatDateClient(Util.getCurrentDate());
        SharedPreferences sp = context.getSharedPreferences("datalogin", Context.MODE_PRIVATE);
        String iduser = sp.getString("id", "0");

        Service service = RetrofitUtil.getService(context);
        Call<List<TicketV2>> listCall = service.getListTicker(tempngay,  Integer.parseInt(iduser));
        listCall.enqueue(new Callback<List<TicketV2>>() {
            @Override
            public void onResponse(Call<List<TicketV2>> call, Response<List<TicketV2>> response) {
                if(response.body() != null && response.body().size() != 0){
                    ArrayList<TicketV2> tickers = (ArrayList<TicketV2>) response.body();
                    int countTickeTimeComing = 0;
                    int countTicker = tickers.size();

                    for (int i = 0; i < countTicker; i++) {
                        TicketV2 ticker = tickers.get(i);
                        String [] date =  ticker.getNgayDat().split("-");
                        String [] time = ticker.getSuatchieu().getGio().split(":");
                        Calendar calBooking = Calendar.getInstance();

                        calBooking.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date[2]));
                        calBooking.set(Calendar.MONTH, Integer.parseInt(date[1]));
                        calBooking.set(Calendar.YEAR, Integer.parseInt(date[0]));

                        calBooking.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
                        calBooking.set(Calendar.MINUTE, Integer.parseInt(time[1]));
                        calBooking.set(Calendar.SECOND, Integer.parseInt(time[2]));

                        Calendar calCurrent = Calendar.getInstance();

                        Calendar calResult = Util.distanceTime(calBooking,calCurrent);
                        int hour = calResult.get(Calendar.HOUR_OF_DAY);

                        if(hour == 0){
                            countTickeTimeComing++;
                        }
                    }

                    if(countTickeTimeComing != 0){
                        Util.createNotifycation(context, "Thông báo", "Sắp tới giờ phim chiếu, bạn hiện có "+ countTickeTimeComing + " vé của phim sắp chiếu");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TicketV2>> call, Throwable t) {
                if (iduser.equals("0") == false) {
                    call.clone().enqueue(this);
                }

            }
        });
    }
}
