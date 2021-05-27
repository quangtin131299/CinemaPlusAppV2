package com.ngolamquangtin.appdatvexemphim.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ngolamquangtin.appdatvexemphim.Activity.DetailTickerActivity;
import com.ngolamquangtin.appdatvexemphim.Adapter.TickerAdapter;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.Ticker;
import com.ngolamquangtin.appdatvexemphim.DTO.TicketV2;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTicker extends Fragment {

    TextView txtthongbaocovedat;
    SwipeRefreshLayout refeslayoutticker;
    ListView lvticker;
    TickerAdapter tickerAdapter;
    SharedPreferences sharedPreferences;
    ArrayList<TicketV2> tickers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticker, container, false);
        addControls(view);
        addEvents();
        loadDSVe();
        return view;
    }

    private void loadDSVe() {
        refeslayoutticker.setRefreshing(true);
        Calendar calendar = Calendar.getInstance();
        Date ngayhientai = calendar.getTime();
        SimpleDateFormat formatdate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formattime = new SimpleDateFormat("HH:mm");
        String tempngay = formatdate.format(ngayhientai);
        String tempthoigian = formattime.format(ngayhientai);
        sharedPreferences = getActivity().getSharedPreferences("datalogin", Context.MODE_PRIVATE);
        String iduser = sharedPreferences.getString("id", "0");
        Service service = RetrofitUtil.getService(getActivity());
//        Integer.parseInt(iduser)
        Call<List<TicketV2>> listCall = service.getListTicker("", "", 1);
        listCall.enqueue(new Callback<List<TicketV2>>() {
            @Override
            public void onResponse(Call<List<TicketV2>> call, Response<List<TicketV2>> response) {
                if(response.body() != null){
                    if(txtthongbaocovedat.getVisibility() == View.VISIBLE){
                        txtthongbaocovedat.setVisibility(View.INVISIBLE);
                        refeslayoutticker.setVisibility(View.VISIBLE);
                    }
                    refeslayoutticker.setRefreshing(false);
                    tickers.addAll(response.body());
                    tickerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<TicketV2>> call, Throwable t) {
                if (iduser.equals("0") == false) {
                    refeslayoutticker.setRefreshing(false);
                    if(txtthongbaocovedat.getVisibility() != View.VISIBLE){
                        txtthongbaocovedat.setVisibility(View.VISIBLE);
                        refeslayoutticker.setVisibility(View.INVISIBLE);
                    }
                    call.clone().enqueue(this);
                }

            }
        });



    }

    private void addEvents() {
        refeslayoutticker.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tickers.clear();
                tickerAdapter.notifyDataSetChanged();
                loadDSVe();
            }
        });
        lvticker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), DetailTickerActivity.class);
                TicketV2 ticker = tickers.get(position);
                i.putExtra("TICKER", ticker);
                startActivity(i);
            }
        });
    }

    private void addControls(View view) {
        refeslayoutticker = view.findViewById(R.id.refeslayoutticker);
        txtthongbaocovedat = view.findViewById(R.id.txtthongbaocovedat);
        lvticker = view.findViewById(R.id.lvTicker);
        tickers = new ArrayList<>();
        tickerAdapter = new TickerAdapter(getActivity(), tickers);
        lvticker.setAdapter(tickerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        tickers.clear();
        tickerAdapter.notifyDataSetChanged();
        loadDSVe();
    }
}
