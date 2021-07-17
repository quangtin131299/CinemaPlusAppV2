package com.ngolamquangtin.appdatvexemphim.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ngolamquangtin.appdatvexemphim.Adapter.FilterMovieTickerAdapter;
import com.ngolamquangtin.appdatvexemphim.Adapter.TickerAdapter;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.DTO.TicketV2;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTicker extends Fragment {

    FilterMovieTickerAdapter filterMovieAdapter;
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

        return view;
    }

    private void loadDSVe() {
        refeslayoutticker.setRefreshing(true);
        Calendar calendar = Calendar.getInstance();
        Date curentDate = calendar.getTime();
        String tempngay = Util.formatDateClient(Util.getCurrentDate());
        String tempthoigian = Util.formatTimeClient(curentDate);
        String iduser = getIdCustomer();
        Service service = RetrofitUtil.getService(getActivity());

        Call<List<TicketV2>> listCall = service.getListTicker(tempngay,  Integer.parseInt(iduser));
        listCall.enqueue(new Callback<List<TicketV2>>() {
            @Override
            public void onResponse(Call<List<TicketV2>> call, Response<List<TicketV2>> response) {
                refeslayoutticker.setRefreshing(false);

                tickers.clear();

                if(response.body() != null && response.body().size() != 0){
                    lvticker.setVisibility(View.VISIBLE);
                    txtthongbaocovedat.setVisibility(View.INVISIBLE);

                    tickers.addAll(response.body());
                }else{
                    lvticker.setVisibility(View.INVISIBLE);
                    txtthongbaocovedat.setVisibility(View.VISIBLE);
                }

                tickerAdapter.notifyDataSetChanged();
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

                tickerAdapter.notifyDataSetChanged();
                loadDSVe();
            }
        });
    }

    private void addControls(View view) {
        sharedPreferences = getActivity().getSharedPreferences("datalogin", Context.MODE_PRIVATE);
        refeslayoutticker = view.findViewById(R.id.refeslayoutticker);
        txtthongbaocovedat = view.findViewById(R.id.txtthongbaocovedat);
        lvticker = view.findViewById(R.id.lvTicker);
        tickers = new ArrayList<>();
        tickerAdapter = new TickerAdapter(getActivity(), tickers);
        lvticker.setAdapter(tickerAdapter);
    }

    public String getIdCustomer(){
        String iduser = sharedPreferences.getString("id", "0");

        return iduser;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadDSVe();
    }
}
