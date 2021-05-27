package com.ngolamquangtin.appdatvexemphim.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.Room;
import com.ngolamquangtin.appdatvexemphim.DTO.Seat;
import com.ngolamquangtin.appdatvexemphim.DTO.TickerBook;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectSeatActivity extends AppCompatActivity {


    ArrayList<Seat> seatdat = new ArrayList<>();
    ArrayList<LinearLayout> arrlinear = new ArrayList<>();
    ArrayList<Seat> seats = new ArrayList<>();
    TextView txttime, txtsophong, txttenphim;
    TickerBook tickerBook;
    Button btnthanhtoan;
    Room room;
    ImageView imgtryvet;
    int countclick = 1;
    Runnable runnable;
    Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_seat);
        addControls();
//        loadDataPhong();
        addEvents();
    }

    private void setMaGhe(int idroom) {
        Service service = RetrofitUtil.getService(SelectSeatActivity.this);
        Call<List<Seat>> listCall = service.getListGhe(idroom);
        listCall.enqueue(new Callback<List<Seat>>() {
            @Override
            public void onResponse(Call<List<Seat>> call, retrofit2.Response<List<Seat>> response) {
                ArrayList<Seat> seats = (ArrayList<Seat>) response.body();
                int sl = arrlinear.size();
                int k = 0;
                for (int i = 0; i < sl; i++) {
                    LinearLayout lineartemp = arrlinear.get(i);
                    int slchild = lineartemp.getChildCount();
                    for (int j = 0; j < slchild; j++) {
                        View temp = lineartemp.getChildAt(j);
                        if (temp instanceof ImageView) {
                            temp.setTag(seats.get(k++).getId());
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Seat>> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });

    }

    private void HamA() {
        arrlinear.add(findViewById(R.id.lineardong1));
        arrlinear.add(findViewById(R.id.lineardong2));
        arrlinear.add(findViewById(R.id.lineardong3));
        arrlinear.add(findViewById(R.id.lineardong4));
        arrlinear.add(findViewById(R.id.lineardong5));
        arrlinear.add(findViewById(R.id.lineardong6));
        arrlinear.add(findViewById(R.id.lineardong7));
        arrlinear.add(findViewById(R.id.lineardong8));
        arrlinear.add(findViewById(R.id.lineardong9));

        int count = 0;
        int sl = arrlinear.size();
        for (int i = 0; i < sl; i++) {
            LinearLayout lineartemp = arrlinear.get(i);
            int slchild = lineartemp.getChildCount();
            for (int j = 0; j < slchild; j++) {
                View temp = lineartemp.getChildAt(j);
                if (temp instanceof ImageView) {
                    count++;
                }
            }
        }


    }

    private void reupdateSeat() {
        handler = new Handler();
        handler.postDelayed(runnable, 5000);
    }

    private void loadDataPhong() {
        Intent i = getIntent();
        txttenphim.setText(i.getStringExtra("TEN_PHIM"));
        if (i.hasExtra("ID_MOVIE") && i.hasExtra("NGAYDATHIENTAI") && i.hasExtra("SUATCHIEU") && i.hasExtra("ID_CINEMA") && i.hasExtra("TEN_PHIM")) {
            Service service = RetrofitUtil.getService(SelectSeatActivity.this);
            int idsuatchieu = i.getIntExtra("SUATCHIEU",0);
            int idphim = i.getIntExtra("ID_MOVIE", 0);
            int idrap = i.getIntExtra("ID_CINEMA", 0);
            String ngayhientai = i.getStringExtra("NGAYDATHIENTAI");
            Call<List<Room>> listCall = service.getPhong(idsuatchieu, idphim, idrap, ngayhientai);
            listCall.enqueue(new Callback<List<Room>>() {
                @Override
                public void onResponse(Call<List<Room>> call, retrofit2.Response<List<Room>> response) {
                    room = response.body().get(0);
                    setMaGhe(room.getId());
                    loadData(room.getId());
                    runnable =  new Runnable() {
                        @Override
                        public void run() {
                            loadData(room.getId());
                            new Handler().postDelayed(this, 7000);
                        }
                    };
                    updateRoom(response.body().get(0));
                }

                @Override
                public void onFailure(Call<List<Room>> call, Throwable t) {
                    call.clone().enqueue(this);
                }
            });
        }


    }

    private void updateRoom(Room room) {
        tickerBook.setIdphong(room.getId());
        txtsophong.setText(room.getTenphong());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("HH:mm");
        try {
            Date date = simpleDateFormat.parse(room.getThoigian());
            txttime.setText(output.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void addEvents() {
        btnthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectSeatActivity.this, PaymentActivity.class);
                startActivity(i);
//                if (tickerBook.getIdghe() != 0) {
//                    Intent temp = getIntent();
//                    Intent i = new Intent(SelectSeatActivity.this, PaymentActivity.class);
//                    if (room != null) {
//                        i.putExtra("ID_PHONG", room.getId());
//                        tickerBook.setIdphong(room.getId());
//                    }
//                    i.putExtra("TICKERBOOK", tickerBook);
//
//                    i.putExtra("TEN_PHIM", temp.getStringExtra("TEN_PHIM"));
//                    i.putExtra("TEN_RAP", temp.getStringExtra("TEN_RAP"));
//                    startActivity(i);
//                } else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(SelectSeatActivity.this);
//                    builder.setMessage("Mời bạn chọn ghế");
//                    builder.show();
//                }
            }
        });

    }

    private void updateUI() {
//        for (int l = 0; l <= seats.size() - 1; l++) {
//            for (int i = 0; i < arrlinear.size(); i++) {
//                for (int j = 0; j < arrlinear.get(i).getChildCount(); j++) {
//                    LinearLayout linearLayout = (LinearLayout) arrlinear.get(i).getChildAt(j);
//                    for (int k = 0; k < linearLayout.getChildCount(); k++) {
//                        Seat tempseat = seats.get(l);
//                        ImageView tempimage = (ImageView) linearLayout.getChildAt(k);
//                        if (tempimage.getTag() != null) {
//                            if (tempseat.getId() == Integer.parseInt(String.valueOf(tempimage.getTag()))) {
//                                if (tempimage.getDrawable().getConstantState() != getDrawable(R.drawable.seatchon).getConstantState()) {
//                                    if (tempseat.getTrangthai().equals("Đã đặt")) {
//                                        tempimage.setImageResource(R.drawable.seatdadat);
//                                    } else {
//                                        tempimage.setImageResource(R.drawable.seattrong);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
        int slghedat = seatdat.size();
        int sl = arrlinear.size();
        for (int k = 0; k < slghedat; k++) {
            for (int i = 0; i < sl; i++) {
                LinearLayout lineartemp = arrlinear.get(i);
                int slchild = lineartemp.getChildCount();
                for (int j = 0; j < slchild; j++) {
                    View temp = lineartemp.getChildAt(j);
                    if (temp instanceof ImageView) {
                        ImageView tempimage = (ImageView) temp;
                        Seat tempseat = seatdat.get(k);
                        if (tempimage.getTag() != null) {
                            if (tempseat.getId() == Integer.parseInt(String.valueOf(tempimage.getTag()))) {
                                if (tempimage.getDrawable().getConstantState() != getDrawable(R.drawable.seatchon).getConstantState()) {
                                    if (tempseat.getTrangthai().equals("Đã đặt")) {
                                        tempimage.setImageResource(R.drawable.seatdadat);
                                    } else {
                                        tempimage.setImageResource(R.drawable.seattrong);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }

        private void loadData(int roomid){
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = Calendar.getInstance().getTime();
            final Intent intent = getIntent();
//        String url = "";
//        if (intent.hasExtra("NGAYDATHIENTAI") && intent.hasExtra("ID_MOVIE") && intent.hasExtra("ID_CINEMA") && intent.hasExtra("SUATCHIEU")) {
//            url = String.format(Util.LINK_LOADGHE, intent.getIntExtra("ID_CINEMA", 0), intent.getIntExtra("ID_MOVIE", 0), intent.getStringExtra("SUATCHIEU"), intent.getStringExtra("NGAYDATHIENTAI"));
//        }
//        RequestQueue requestQueue = Volley.newRequestQueue(SelectSeatActivity.this);
//        StringRequest stringRequest = new StringRequest(Request.Method.PUT, Util.LINK_LOADGHE, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                if (response != null) {
//
//                    try {
//                        JSONArray jsonArray = new JSONArray(response);
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            Seat seat = new Seat();
//                            seat.setId(jsonArray.getJSONObject(i).getInt("ID"));
//                            seat.setTenghe(jsonArray.getJSONObject(i).getString("TenGhe"));
//                            seat.setTrangthai(jsonArray.getJSONObject(i).getString("TrangThai"));
//                            seats.add(seat);
//                        }
//                        updateUI();
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<>();
//                map.put("rapphim", String.valueOf(intent.getIntExtra("ID_CINEMA", 0)));
//                map.put("idphim", String.valueOf(intent.getIntExtra("ID_MOVIE", 0)));
//                map.put("suatchieu", intent.getStringExtra("SUATCHIEU"));
//                map.put("ngaydathientai", intent.getStringExtra("NGAYDATHIENTAI"));
//                return map;
//            }
//        };
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        requestQueue.add(stringRequest);
            int idraphim = intent.getIntExtra("ID_CINEMA", 0);
            int idphim = intent.getIntExtra("ID_MOVIE", 0);
            int idsuatchieu = intent.getIntExtra("SUATCHIEU", 0);
            String ngay = intent.getStringExtra("NGAYDATHIENTAI");
            Service service = RetrofitUtil.getService(SelectSeatActivity.this);
            Call<List<Seat>> listCall = service.getSeat(roomid,idraphim, idphim, idsuatchieu, ngay);
            listCall.enqueue(new Callback<List<Seat>>() {
                @Override
                public void onResponse(Call<List<Seat>> call, Response<List<Seat>> response) {
                    Log.d("TAG", String.valueOf(response.body().size()));
                    seatdat = (ArrayList<Seat>) response.body();
                    updateUI();
                }

                @Override
                public void onFailure(Call<List<Seat>> call, Throwable t) {

                }
            });
        }

        private void addControls () {
            imgtryvet = new ImageView(SelectSeatActivity.this);
            txttime = findViewById(R.id.txttime);
            txtsophong = findViewById(R.id.txtsophong);
            txttenphim = findViewById(R.id.txttenphim);
            btnthanhtoan = findViewById(R.id.btnthanhtoan);

            Intent intent = getIntent();

            if (intent.hasExtra("TICKERBOOK")) {
                tickerBook = (TickerBook) intent.getSerializableExtra("TICKERBOOK");

            }

            arrlinear.add(findViewById(R.id.lineardong1));
            arrlinear.add(findViewById(R.id.lineardong2));
            arrlinear.add(findViewById(R.id.lineardong3));
            arrlinear.add(findViewById(R.id.lineardong4));
            arrlinear.add(findViewById(R.id.lineardong5));
            arrlinear.add(findViewById(R.id.lineardong6));
            arrlinear.add(findViewById(R.id.lineardong7));
            arrlinear.add(findViewById(R.id.lineardong8));
            arrlinear.add(findViewById(R.id.lineardong9));

            int sl = arrlinear.size();
            for (int i = 0; i < sl; i++) {
                LinearLayout lineartemp = arrlinear.get(i);
                int slchild = lineartemp.getChildCount();
                for (int j = 0; j < slchild; j++) {
                    View temp = lineartemp.getChildAt(j);
                    if (temp instanceof ImageView) {
                        temp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ImageView imageView1 = (ImageView) v;
                                Toast.makeText(SelectSeatActivity.this, String.valueOf(imageView1.getTag()), Toast.LENGTH_SHORT).show();
                                if (imageView1.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.seattrong).getConstantState()) {
                                    if (countclick == 1) {
                                        imageView1.setImageResource(R.drawable.seatchon);
                                        tickerBook.setIdghe(Integer.parseInt(String.valueOf(imageView1.getTag())));
                                        imgtryvet = imageView1;
                                        countclick = 2;
                                    } else {
                                        imgtryvet.setImageResource(R.drawable.seattrong);
                                        tickerBook.setIdghe(Integer.parseInt(String.valueOf(imageView1.getTag())));
                                        imageView1.setImageResource(R.drawable.seatchon);
                                        imgtryvet = imageView1;
                                    }
                                } else if (imageView1.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.seatdadat).getConstantState()) {
                                    Toast.makeText(SelectSeatActivity.this, "Ghế đã đặt rồi", Toast.LENGTH_SHORT).show();
                                } else {
                                    imageView1.setImageResource(R.drawable.seattrong);
                                    tickerBook.setIdghe(0);
                                }
                            }
                        });
                    }
                }
            }
        }

        @Override
        protected void onResume () {
            super.onResume();
//            reupdateSeat();
        }

        @Override
        protected void onPause () {
            super.onPause();
            handler.removeCallbacks(runnable);
        }
    }
