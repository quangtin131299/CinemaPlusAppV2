package com.ngolamquangtin.appdatvexemphim.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.RotatingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.TokenClient;
import com.ngolamquangtin.appdatvexemphim.Notifycation;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.ServiceNotifyTicker;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();

        startServiceNotify();

        startServieNotifyTicker();

        getToken();

        new PrefetchData().execute();
    }

    public void init(){
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
        Sprite doubleBounce = new RotatingCircle();
        progressBar.setIndeterminateDrawable(doubleBounce);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    public void startServieNotifyTicker() {
        Intent intent = new Intent(SplashActivity.this, ServiceNotifyTicker.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(SplashActivity.this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), 60000, pendingIntent);
    }

    public void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
               String tokenMessage = task.getResult();
               addNewTokenClient(tokenMessage);
            }
        });
    }

    public void startServiceNotify(){
        Intent intentBackgroundService = new Intent(SplashActivity.this, Notifycation.class);
        startService(intentBackgroundService);
    }

    public void addNewTokenClient(String token){
        TokenClient newToken = new TokenClient();

        newToken.setToken(token);

        Service service = RetrofitUtil.getService(SplashActivity.this);
        Call<Integer> call = service.addNewTokenClient(newToken);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.body() != null && response.body() == 0){
                    Log.d("/////" ,"Lỗi rồi");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

    }


    class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //Nap data ở đây
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 2000);
        }
    }
}
