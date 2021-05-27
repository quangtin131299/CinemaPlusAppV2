package com.ngolamquangtin.appdatvexemphim.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ngolamquangtin.appdatvexemphim.Adapter.RapAdapter;
import com.ngolamquangtin.appdatvexemphim.Adapter.TimeAdapter;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.Cinema;
import com.ngolamquangtin.appdatvexemphim.DTO.TickerBook;
import com.ngolamquangtin.appdatvexemphim.DTO.XuatChieu;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseSessionActivity extends AppCompatActivity implements TimeAdapter.onClickListenerRecyclerView {

    ArrayList<Cinema> cinemas = new ArrayList<>();
    ArrayList<XuatChieu> xuatChieus = new ArrayList<>();
    RapAdapter rapAdapter;
    Spinner spinner;
    TextView txtChooseDate;
    Button btnChonGhe;
    TimeAdapter timeAdapter;
    RecyclerView rvTime;
    ImageView imgback;
    Button btnchonngay;
    TickerBook tickerBook = new TickerBook();
    SharedPreferences sharedPreferences;
    XuatChieu xctemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_session);
        addControls();
        addEvents();
    }

    public void loadCinema() {
        Service service = RetrofitUtil.getService(ChooseSessionActivity.this);
        Call<List<Cinema>> listCall = service.getListCinema();
        listCall.enqueue(new Callback<List<Cinema>>() {
            @Override
            public void onResponse(Call<List<Cinema>> call, retrofit2.Response<List<Cinema>> response) {
                cinemas.addAll(response.body());
                rapAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Cinema>> call, Throwable t) {

            }
        });
    }

    private void addEvents() {
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnChonGhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = getIntent();
                if (i1.hasExtra("ID_MOVIE") && i1.hasExtra("TEN_PHIM")) {
                    Intent intent = new Intent(ChooseSessionActivity.this, SelectSeatActivity.class);
                    Cinema cinema = (Cinema) spinner.getSelectedItem();
                    if (cinema != null) {
                        intent.putExtra("ID_CINEMA", cinema.getId());

                    }
                    if (xctemp != null) {
                        intent.putExtra("SUATCHIEU", xctemp.getId());
                    }
                    intent.putExtra("ID_MOVIE", i1.getIntExtra("ID_MOVIE", 0));
                    tickerBook.setIdphim(i1.getIntExtra("ID_MOVIE", 0));
                    SimpleDateFormat formatngaydat = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat outputformatngaydat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date ngaydat = formatngaydat.parse(txtChooseDate.getText().toString().trim());
                        tickerBook.setNgaydat(outputformatngaydat.format(ngaydat));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String idkhachhang = sharedPreferences.getString("id", "");
                    if (idkhachhang.equals("") == false) {
                        tickerBook.setIdkhachhang(Integer.parseInt(idkhachhang));
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    intent.putExtra("NGAYDATHIENTAI", simpleDateFormat.format(Calendar.getInstance().getTime()));
                    intent.putExtra("TEN_PHIM", i1.getStringExtra("TEN_PHIM"));
                    intent.putExtra("TICKERBOOK", tickerBook);
                    intent.putExtra("TEN_RAP", cinema.getTenrap());
                    if (tickerBook.getIdsuat() != 0 && tickerBook.getNgaydat().equals("") == false) {
                        finish();
                        startActivity(intent);
                    }else{
                        Toast.makeText(ChooseSessionActivity.this, "Bạn phải chọn đẩy ba phần", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                    startActivity(intent);
                }


            }
        });

        btnchonngay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseDate();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cinema cinema = cinemas.get(position);
                Intent intent = getIntent();
                if (intent.hasExtra("ID_MOVIE")) {
                    xuatChieus.clear();
                    timeAdapter.notifyDataSetChanged();
                    loadXuatChieu(cinema.getId(), intent.getIntExtra("ID_MOVIE", 0));
                }
                tickerBook.setIdrap(cinema.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void addControls() {
        txtChooseDate = findViewById(R.id.txtChooseDate);
        btnChonGhe = findViewById(R.id.btnChonGhe);
        spinner = findViewById(R.id.spinner);
        rvTime = findViewById(R.id.lvTime);
        imgback = findViewById(R.id.imgback);
        rapAdapter = new RapAdapter(ChooseSessionActivity.this, cinemas);
        spinner.setAdapter(rapAdapter);
        timeAdapter = new TimeAdapter(getApplicationContext(), xuatChieus, ChooseSessionActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvTime.setLayoutManager(linearLayoutManager);
        rvTime.setAdapter(timeAdapter);
        btnchonngay = findViewById(R.id.btnchonngay);

        sharedPreferences = getSharedPreferences("datalogin", Context.MODE_PRIVATE);

    }


    public void ChooseDate() {
        final Calendar calendar = Calendar.getInstance();
        final int daydefault = calendar.get(Calendar.DAY_OF_MONTH);
        final int monthdefault = calendar.get(Calendar.MONTH);
        final int yeardefault = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(ChooseSessionActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (daydefault == dayOfMonth && monthdefault == month && yeardefault == year) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    txtChooseDate.setText(simpleDateFormat.format(calendar.getTime()));
                } else {
                    Toast.makeText(ChooseSessionActivity.this, "Ngày không hợp lệ", Toast.LENGTH_SHORT).show();
                }

            }
        }, yeardefault, monthdefault, daydefault);
        datePickerDialog.show();
    }


    public void loadXuatChieu(int idrap, int idphim) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String url = String.format(Util.LINK_LOADSUATCHIEUTHEORAP, idrap, idphim, simpleDateFormat.format(Calendar.getInstance().getTime()));
        Service service = RetrofitUtil.getService(ChooseSessionActivity.this);
        Call<List<XuatChieu>> listCall = service.getXuatChieu(idphim,idrap, simpleDateFormat.format(Calendar.getInstance().getTime()));
        listCall.enqueue(new Callback<List<XuatChieu>>() {
            @Override
            public void onResponse(Call<List<XuatChieu>> call, Response<List<XuatChieu>> response) {
                xuatChieus.addAll(response.body());
                timeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<XuatChieu>> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });

    }

    @Override
    public void onClicked(int position, View view) {
        xctemp = xuatChieus.get(position);

        Toast.makeText(ChooseSessionActivity.this, "Bạn đã chọn suất " + xctemp.getThoigian() + " " + xctemp.getId(), Toast.LENGTH_LONG).show();
        tickerBook.setIdsuat(xctemp.getId());

    }

    @Override
    protected void onResume() {
        super.onResume();
        cinemas.clear();
        rapAdapter.notifyDataSetChanged();
        loadCinema();
    }
}