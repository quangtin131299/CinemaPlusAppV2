package com.ngolamquangtin.appdatvexemphim.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.astritveliu.boom.Boom;
import com.ngolamquangtin.appdatvexemphim.Adapter.PopCornAdapter;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.PopCorn;
import com.ngolamquangtin.appdatvexemphim.DTO.TickerBook;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopCornActivity extends AppCompatActivity {

    TickerBook tickerBook;
    SwipeRefreshLayout refeshPopcorn;
    PopCornAdapter popCornAdapter;
    ArrayList<PopCorn> popCorns;
    Button btnPayment, btnBackto;
    ListView lvPopCorn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_corn);

        addControls();

        setTickerBook();

        addEvents();

        loadAllPopCorn();
    }

    public void setTickerBook() {
        Intent intenScreenSelectSeat = getIntent();

        if(intenScreenSelectSeat.hasExtra("TICKERBOOK")){
            tickerBook = (TickerBook) intenScreenSelectSeat.getSerializableExtra("TICKERBOOK");
        }

    }

    private void loadAllPopCorn() {
        Service service = RetrofitUtil.getService(PopCornActivity.this);
        Call<ArrayList<PopCorn>> call = service.getAllPopCorn();

        call.enqueue(new Callback<ArrayList<PopCorn>>() {
            @Override
            public void onResponse(Call<ArrayList<PopCorn>> call, Response<ArrayList<PopCorn>> response) {
                refeshPopcorn.setRefreshing(false);
                popCorns.clear();

                if(response.body() != null && response.body().size() != 0){
                    popCorns.addAll(response.body());
                }

                popCornAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<PopCorn>> call, Throwable t) {

            }
        });
    }

    private void addEvents() {
        btnBackto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intenToScreenPayment = new Intent(PopCornActivity.this, PaymentActivity.class);

                setPopCornArray();

                intenToScreenPayment.putExtra("TICKERBOOK", tickerBook);
                intenToScreenPayment.putExtra("NAME_MOVIE", getNameMovie());
                intenToScreenPayment.putExtra("NAME_CINEMA", getNameCinema());
                intenToScreenPayment.putExtra("IMAGE_MOVIE", getImageMovie());
                intenToScreenPayment.putExtra("SHOW_TIME",getShowTime());
                intenToScreenPayment.putExtra("TIME_MOVIE",getTime());

                startActivity(intenToScreenPayment);
            }
        });

        refeshPopcorn.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAllPopCorn();
            }
        });

        new Boom(btnBackto);
        new Boom(btnPayment);
    }

    private void addControls() {
        refeshPopcorn = findViewById(R.id.refeshpopcorn);
        popCorns = new ArrayList<>();
        popCornAdapter = new PopCornAdapter(PopCornActivity.this, popCorns);
        btnBackto = findViewById(R.id.btnbackto);
        btnPayment = findViewById(R.id.btnactionpayment);
        lvPopCorn = findViewById(R.id.lvpopcorn);
        lvPopCorn.setAdapter(popCornAdapter);
    }

    public int getCountPopCornItem(int index){
         View view = lvPopCorn.getChildAt(index - lvPopCorn.getFirstVisiblePosition());

         if(view == null){
             return 0;
         }

        TextView txtCount = view.findViewById(R.id.txtcount);
        int count = Integer.parseInt(txtCount.getText().toString());

        return count;
    }

    public void setPopCornArray(){
        int countPopCorn = popCorns.size();

        ArrayList<PopCorn> popCornSelected = new ArrayList<>();

        for(int i = 0; i < countPopCorn; i++){
            int countPopcorn = getCountPopCornItem(i);

            if( countPopcorn != 0){
                PopCorn popCorn = popCorns.get(i);

                popCorn.setCount(countPopcorn);

                popCornSelected.add(popCorn);
            }
        }

        tickerBook.getPopcorns().addAll(popCornSelected);
    }

    public String getNameMovie(){
        Intent intentScreenChooseSession = getIntent();

        if (intentScreenChooseSession.hasExtra("TEN_PHIM")) {
            return intentScreenChooseSession.getStringExtra("TEN_PHIM");
        }

        return "";
    }

    public String getNameCinema() {
        Intent intentScreenChooseSession = getIntent();

        if (intentScreenChooseSession.hasExtra("TEN_RAP")) {
            return intentScreenChooseSession.getStringExtra("TEN_RAP");
        }

        return "";
    }

    public String getImageMovie(){
        Intent intentScreenChooseSession = getIntent();

        if (intentScreenChooseSession.hasExtra("IMAGE_MOVIE")) {
            return intentScreenChooseSession.getStringExtra("IMAGE_MOVIE");
        }

        return "";
    }

    public String getShowTime(){
        Intent intentScreenChooseSession = getIntent();

        if (intentScreenChooseSession.hasExtra("SHOW_TIME")) {
            return intentScreenChooseSession.getStringExtra("SHOW_TIME");
        }

        return "";
    }

    public int getTime(){
        Intent intenScreenSelectSeat = getIntent();

        if(intenScreenSelectSeat.hasExtra("TIME_MOVIE")){
            return intenScreenSelectSeat.getIntExtra("TIME_MOVIE", 0);
        }

        return 0;
    }
}