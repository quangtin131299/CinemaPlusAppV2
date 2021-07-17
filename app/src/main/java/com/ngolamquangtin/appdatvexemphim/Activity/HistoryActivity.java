package com.ngolamquangtin.appdatvexemphim.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.ngolamquangtin.appdatvexemphim.Adapter.HistoryAdapter;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.BillV2;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {

    ArrayList<BillV2> bills;
    ListView lvTransaction;
    HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        addControls();

        loadBillByCustomerId(getIdCustomer());
    }

    private void loadBillByCustomerId(int idCustomer) {
        Service service = RetrofitUtil.getService(HistoryActivity.this);
        Call<ArrayList<BillV2>> call = service.getAllBillByCustomerId(idCustomer);

        call.enqueue(new Callback<ArrayList<BillV2>>() {
            @Override
            public void onResponse(Call<ArrayList<BillV2>> call, Response<ArrayList<BillV2>> response) {
                bills.clear();

                if(response.body() != null && response.body().size() != 0){
                    bills.addAll(response.body());
                }

                historyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<BillV2>> call, Throwable t) {

            }
        });
    }

    private void addControls() {
        bills = new ArrayList<>();
        lvTransaction = findViewById(R.id.lvtransaction);
        historyAdapter = new HistoryAdapter(HistoryActivity.this, bills);

        lvTransaction.setAdapter(historyAdapter);
    }

    public int getIdCustomer(){
        SharedPreferences sharedPreferences = getSharedPreferences("datalogin", Context.MODE_PRIVATE);

        try {
            return Integer.parseInt(sharedPreferences.getString("id", ""));
        }catch (Exception ex){
            ex.printStackTrace();
            return 0 ;
        }
    }

}