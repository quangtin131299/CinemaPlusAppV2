package com.ngolamquangtin.appdatvexemphim.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.ngolamquangtin.appdatvexemphim.Adapter.ScheduleAdapter;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.Cinema;
import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.DTO.ScheduleOfDate;
import com.ngolamquangtin.appdatvexemphim.DTO.TimeV2;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;
import com.vivekkaushik.datepicker.DatePickerTimeline;
import com.vivekkaushik.datepicker.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleActivity extends AppCompatActivity  {

    SwipeRefreshLayout refeshSchedule;
    LottieAnimationView processing;
    String currentDate;
    Cinema cinema;
    TextView txtDate, txtMess;
    ListView lvSchedule;
    ScheduleAdapter scheduleAdapter;
    ArrayList<Movie> movies;
    ArrayList<TimeV2> showTimes;
    DatePickerTimeline dateChoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        setCinema();

        addControls();

        initDatePicker();

        addEvents();

        updateUI(Util.formatDateServerToClient(currentDate));

        loadScheduleByDate(currentDate);
    }

    public void initDatePicker() {
        Calendar calendar = Calendar.getInstance();

        dateChoose.setInitialDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void addEvents() {
        dateChoose.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int dayOfWeek) {
                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                String dateUI = Util.formatDateByCalendar(calendar);

                currentDate = Util.formatDateClientToServer(dateUI);

                scheduleAdapter.setDateBooking(calendar);

                updateUI(dateUI);

                loadScheduleByDate(currentDate);
            }
        });

        refeshSchedule.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!currentDate.equals("")){
                    loadScheduleByDate(Util.formatDateServerToClient(currentDate));
                }
            }
        });
    }

    public void addControls() {
        currentDate = Util.formatDateClientToServer(Util.formatDateByCalendar(Calendar.getInstance())); //yyy-MM-dd
        showTimes = new ArrayList<>();
        movies = new ArrayList<>();
        refeshSchedule = findViewById(R.id.refeshschedule);
        processing = findViewById(R.id.progressing);
        txtMess = findViewById(R.id.txtmess);
        txtDate = findViewById(R.id.txtdate);
        dateChoose = findViewById(R.id.datePickerTimeline);
        lvSchedule = findViewById(R.id.lvSchedule);
        scheduleAdapter = new ScheduleAdapter(ScheduleActivity.this ,movies, cinema);
        scheduleAdapter.setDateBooking(Calendar.getInstance());
        lvSchedule.setAdapter(scheduleAdapter);
    }

    public void setCinema(){
        Intent intentScreenHome = getIntent();

        if(intentScreenHome.hasExtra("CINEMA")){
            cinema = (Cinema) intentScreenHome.getSerializableExtra("CINEMA");
        }

    }

    public void loadScheduleByDate(String date){
        processing.playAnimation();
        processing.setVisibility(View.VISIBLE);

        Service service = RetrofitUtil.getService(ScheduleActivity.this);
        Call<ScheduleOfDate> call = service.getScheduleOfDate(cinema.getId(), date);
        call.enqueue(new Callback<ScheduleOfDate>() {
            @Override
            public void onResponse(Call<ScheduleOfDate> call, Response<ScheduleOfDate> response) {
                movies.clear();

                if(response.body() != null){
                    processing.pauseAnimation();

                    processing.setVisibility(View.INVISIBLE);

                    txtMess.setVisibility(View.INVISIBLE);

                    movies.clear();

                    movies.addAll(response.body().getMovies());

                    scheduleAdapter.notifyDataSetChanged();
                }else{
                    processing.pauseAnimation();

                    processing.setVisibility(View.INVISIBLE);

                    txtMess.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ScheduleOfDate> call, Throwable t) {
                processing.pauseAnimation();

                processing.setVisibility(View.INVISIBLE);

                txtMess.setVisibility(View.VISIBLE);

                call.clone().enqueue(this);
            }
        });
    }

    public void updateUI(String date){
        txtDate.setText("Ngày chọn hiện tại: " + date);
    }



}