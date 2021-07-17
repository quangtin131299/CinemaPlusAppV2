package com.ngolamquangtin.appdatvexemphim.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.astritveliu.boom.Boom;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ngolamquangtin.appdatvexemphim.Adapter.CinemaAdapter;
import com.ngolamquangtin.appdatvexemphim.Adapter.FilterCinemaNearMeAdapter;
import com.ngolamquangtin.appdatvexemphim.Adapter.TimeAdapter;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.Cinema;
import com.ngolamquangtin.appdatvexemphim.DTO.Schedule;
import com.ngolamquangtin.appdatvexemphim.DTO.TickerBook;
import com.ngolamquangtin.appdatvexemphim.DTO.TimeV2;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;
import com.vdx.designertoast.DesignerToast;
import com.vivekkaushik.datepicker.DatePickerTimeline;
import com.vivekkaushik.datepicker.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseSessionActivity extends AppCompatActivity implements TimeAdapter.onClickListenerRecyclerView {

    final int REQUEST_CODE_LOCALTION_PERMISSION = 1;

    LinearLayoutManager linearLayoutManager;
    DatePickerTimeline datePickerTimeline;
    ListView lvSuggestCinema;
    FilterCinemaNearMeAdapter filterNearMeAdapter;
    Location currentlocation;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationClient;
    LottieAnimationView processing;
    ArrayList<Cinema> cinemas, cinemaSuggest;
    Schedule xuatChieus = new Schedule();
    CinemaAdapter rapAdapter;
    Spinner spinner;
    TextView  txtMessShowTime, txtdatebooking;
    Button btnChonGhe,btnBackTo;
    TimeAdapter timeAdapter;
    RecyclerView rvTime;
    Button btnSuggestCinema;
    TickerBook tickerBook = new TickerBook();
    SharedPreferences sharedPreferences;
    TimeV2 showTimeSelected;
    CardView trace;
    Cinema cinemaSelect;
    Dialog dialogError;
    Handler handler;
    String datebooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_session);

        addControls();

        initDatePickerTime();

        requestPermisson();

        registerHandler();

        addEvents();
    }

    public void initDatePickerTime() {
        Calendar calendar = Calendar.getInstance();

        datePickerTimeline.setInitialDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        String date = Util.formatDateByCalendar(calendar);
        datebooking = date;

        txtdatebooking.setText("Ngày hiện đặt tại là: " + date);
    }

    public void requestPermisson(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    ChooseSessionActivity.this
                    , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , REQUEST_CODE_LOCALTION_PERMISSION);
        } else {
            Util.turnOnLocation(ChooseSessionActivity.this);
            getCurrentLocation();
        }
    }

    public void loadCinema() {
        int idMovie = getIdMovie();

        Service service = RetrofitUtil.getService(ChooseSessionActivity.this);
        Call<List<Cinema>> listCall = service.getCinemaByMovieId(idMovie);
        listCall.enqueue(new Callback<List<Cinema>>() {
            @Override
            public void onResponse(Call<List<Cinema>> call, retrofit2.Response<List<Cinema>> response) {
                if (response.body() != null && response.body().size() != 0) {
                    cinemas.clear();
                    cinemas.addAll(response.body());
                    rapAdapter.notifyDataSetChanged();
                }

                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<List<Cinema>> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
    }

    private void addEvents() {
        btnSuggestCinema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetDialog bottomSuggestionCinema = new BottomSheetDialog(ChooseSessionActivity.this
                                                                                        , R.style.BottomSheetDialogTheme);
                View layout = LayoutInflater.from(ChooseSessionActivity.this).inflate(R.layout.dialogbottom_suggest_cinema, null);

                bottomSuggestionCinema.setContentView(layout);
                lvSuggestCinema = bottomSuggestionCinema.findViewById(R.id.lvcinemasuggest);
                lvSuggestCinema.setAdapter(filterNearMeAdapter);

                lvSuggestCinema.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Cinema cinema = cinemaSuggest.get(i);
                        int index = getIndexCinema(cinema.getId());

                        if(index != -1){
                            spinner.setSelection(index);
                            bottomSuggestionCinema.dismiss();
                        }
                    }
                });

                bottomSuggestionCinema.show();
            }
        });

        btnChonGhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMovie = getIntent();

                Intent intentToSelectSeatScreen = new Intent(ChooseSessionActivity.this, SelectSeatActivity.class);
                Cinema cinema = (Cinema) spinner.getSelectedItem();

                if (cinema != null) {
                    intentToSelectSeatScreen.putExtra("ID_CINEMA", cinema.getId());
                }

                if (showTimeSelected != null) {
                    intentToSelectSeatScreen.putExtra("Id_SHOWTIME", showTimeSelected.getId());
                }

                intentToSelectSeatScreen.putExtra("ID_MOVIE", intentMovie.getIntExtra("ID_MOVIE", 0));

                tickerBook.setIdphim(intentMovie.getIntExtra("ID_MOVIE", 0));
                tickerBook.setNgaydat(Util.formatDateClientToServer(datebooking));

                String idkhachhang = sharedPreferences.getString("id", "");

                if (idkhachhang.equals("") == false) {
                    tickerBook.setIdkhachhang(Integer.parseInt(idkhachhang));
                }

                intentToSelectSeatScreen.putExtra("NGAYDATHIENTAI", Util.formatDateClientToServer(datebooking));
                intentToSelectSeatScreen.putExtra("TEN_PHIM", intentMovie.getStringExtra("TEN_PHIM"));
                intentToSelectSeatScreen.putExtra("IMAGE_MOVIE", intentMovie.getStringExtra("IMAGE_MOVIE"));
                intentToSelectSeatScreen.putExtra("TICKERBOOK", tickerBook);
                intentToSelectSeatScreen.putExtra("TEN_RAP", cinema.getTenrap());
                intentToSelectSeatScreen.putExtra("TIME_MOVIE", getTime());

                if (showTimeSelected != null) {
                    intentToSelectSeatScreen.putExtra("SHOW_TIME", Util.formatTime(showTimeSelected.getGio()));
                }

                if (tickerBook.getIdsuat() != 0 && tickerBook.getNgaydat().equals("") == false) {
                    finish();
                    startActivity(intentToSelectSeatScreen);
                } else {
                    Util.ShowToastErrorMessage(ChooseSessionActivity.this, "Bạn chưa chọn đầy đủ thông tin!");
                }

            }
        });

        btnBackTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        datePickerTimeline.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int dayOfWeek) {
                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                datebooking = Util.formatDateByCalendar(calendar);

                txtdatebooking.setText("Ngày đặt hiện tại là: " + Util.formatDateByCalendar(calendar));

                if(cinemaSelect != null) {
                    loadXuatChieu(cinemaSelect.getId(), getIdMovie(), Util.formatDateClientToServer(Util.formatDateByCalendar(calendar)));
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cinemaSelect = cinemas.get(position);

                tickerBook.setIdrap(cinemaSelect.getId());

                loadXuatChieu(cinemaSelect.getId(), getIdMovie(), Util.formatDateClientToServer(datebooking));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        new Boom(btnSuggestCinema);
        new Boom(btnChonGhe);
        new Boom(btnBackTo);
    }

    public void registerHandler(){
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                switch (msg.what){
                    case 1:
                        loadCinemaSuggest();
                        break;
                    case 2:
                        updateUI();
                        break;
                    case 3:
                        updateShowTimeSelect();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void addControls() {
        datebooking = "";
        cinemas = new ArrayList<>();
        dialogError = new Dialog(ChooseSessionActivity.this);
        txtMessShowTime = findViewById(R.id.txtmessshowtime);
        datePickerTimeline =  findViewById(R.id.datePickerTimeline);
        btnChonGhe = findViewById(R.id.btnChonGhe);
        btnSuggestCinema = findViewById(R.id.btnsuggestcinema);
        processing = findViewById(R.id.progressing);
        spinner = findViewById(R.id.spinner);
        rvTime = findViewById(R.id.lvTime);
        btnBackTo = findViewById(R.id.btnbackto);
        txtdatebooking = findViewById(R.id.txtdatebooking);
        rapAdapter = new CinemaAdapter(ChooseSessionActivity.this, cinemas, 0);
        spinner.setAdapter(rapAdapter);
        timeAdapter = new TimeAdapter(getApplicationContext(), (ArrayList<TimeV2>) xuatChieus.getGio(), ChooseSessionActivity.this, 0);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvTime.setLayoutManager(linearLayoutManager);
        rvTime.setAdapter(timeAdapter);
        cinemaSuggest = new ArrayList<>();

        locationRequest = LocationRequest.create();

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(ChooseSessionActivity.this);

        sharedPreferences = getSharedPreferences("datalogin", Context.MODE_PRIVATE);
    }

    public int getIdMovie(){
        Intent intentMovie = getIntent();

        if(intentMovie.hasExtra("ID_MOVIE")){
            return intentMovie.getIntExtra("ID_MOVIE", 0);
        }

        return 0;
    }
    
    public void loadXuatChieu(int idrap, int idphim, String date) {
        processing.setVisibility(View.VISIBLE);
        processing.playAnimation();
        Service service = RetrofitUtil.getService(ChooseSessionActivity.this);
        Call<Schedule> listCall = service.getShowTimes(idphim, idrap, date);
        listCall.enqueue(new Callback<Schedule>() {
            @Override
            public void onResponse(Call<Schedule> call, Response<Schedule> response) {
                processing.pauseAnimation();
                processing.setVisibility(View.INVISIBLE);

                if (response.body() != null && response.body().getGio().size() != 0) {
                    rvTime.setVisibility(View.VISIBLE);
                    txtMessShowTime.setVisibility(View.INVISIBLE);

                    xuatChieus.getGio().clear();
                    xuatChieus.getGio().addAll(response.body().getGio());
                    timeAdapter.notifyDataSetChanged();

                    Message msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                } else {
                    rvTime.setVisibility(View.INVISIBLE);
                    txtMessShowTime.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<Schedule> call, Throwable t) {
                processing.pauseAnimation();
                processing.setVisibility(View.INVISIBLE);

                call.clone().enqueue(this);
            }
        });
    }

    public void updateShowTimeSelect(){

    }

    public int getIndexShowTime(int idShoTime){
        if(xuatChieus.getGio() != null && xuatChieus.getGio().size() != 0){
            ArrayList<TimeV2> showsTimes = (ArrayList<TimeV2>) xuatChieus.getGio();
            int countShowTime = showsTimes.size();

            for (int i = 0; i < countShowTime; i++) {
                TimeV2 time = showsTimes.get(i);

                if(time.getId() == idShoTime){
                    return i;
                }
            }
        }

        return 0;
    }

    @Override
    public void onClicked(int position, int idMovie,View view, CardView cardTime) {
        if (trace == null) {
            String valueColor = Integer.toHexString(getResources().getColor(R.color.colorSelect, null));
            cardTime.setCardBackgroundColor(Color.parseColor("#" + valueColor));
            showTimeSelected = xuatChieus.getGio().get(position);
            trace = cardTime;
        }
        else if(trace == cardTime){
            String colorUnSelect = Integer.toHexString(getResources().getColor(R.color.colorBackround, null));
            String colorCard = Integer.toHexString(cardTime.getCardBackgroundColor().getDefaultColor()) ;

            if(colorCard.equals(colorUnSelect)){
                String colorSelect = Integer.toHexString(getResources().getColor(R.color.colorSelect, null));
                trace.setCardBackgroundColor(Color.parseColor("#" + colorSelect));
                showTimeSelected = xuatChieus.getGio().get(position);
            }else{
                trace.setCardBackgroundColor(Color.parseColor("#" + colorUnSelect));
                showTimeSelected = null;
                tickerBook.setIdsuat(0);
            }
        }else {
            String valueColorTrace = Integer.toHexString(getResources().getColor(R.color.colorBackround, null));
            String valueColorCurrent = Integer.toHexString(getResources().getColor(R.color.colorSelect, null));
            trace.setCardBackgroundColor(Color.parseColor("#" + valueColorTrace));
            cardTime.setCardBackgroundColor(Color.parseColor("#" + valueColorCurrent));
            trace = cardTime;
            showTimeSelected = xuatChieus.getGio().get(position);
        }
        
        if(showTimeSelected != null){
            tickerBook.setIdsuat(showTimeSelected.getId());
        }else{
            Util.ShowToastErrorMessage(ChooseSessionActivity.this, "Bạn chưa chọn suất chiếu.");
        }

    }
    
    public void showMessageError(String mess){
        if(dialogError != null){
            dialogError.setContentView(R.layout.dialog_failed);

            dialogError.getWindow().setBackgroundDrawableResource(R.color.transparent);

            TextView txtMess = dialogError.findViewById(R.id.txtmess);
            Button btnOk = dialogError.findViewById(R.id.btnOK);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissMessageError();
                }
            });

            txtMess.setText(mess);

            dialogError.show();
        }
    }

    public void dismissMessageError(){
        if(dialogError != null && dialogError.isShowing()){
            dialogError.dismiss();
        }
    }

    public int getTime(){
        Intent intenScreenDetailMovie = getIntent();

        if(intenScreenDetailMovie.hasExtra("TIME_MOVIE")){
            return intenScreenDetailMovie.getIntExtra("TIME_MOVIE", 0);
        }

        return 0;
    }

    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(ChooseSessionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(ChooseSessionActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest,new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                currentlocation = locationResult.getLastLocation();

                filterNearMeAdapter = new FilterCinemaNearMeAdapter(ChooseSessionActivity.this, cinemaSuggest,currentlocation);

                Message msg = new Message();
                msg.what =1;
                handler.sendMessage(msg);

                fusedLocationClient.removeLocationUpdates(this);
            }
        }, Looper.getMainLooper());
    }

    public ArrayList<Integer> filterCinema(Location currentLocation){
        ArrayList idCinemas = new ArrayList();

        if(currentLocation != null && cinemas.size() != 0){
            int countCinema = cinemas.size();
            double lat = currentLocation.getLatitude();
            double lng = currentLocation.getLongitude();

            for(int i = 0; i < countCinema; i++){
                Cinema cinema = cinemas.get(i);
                double distance = Util.distance(lat, lng, Double.parseDouble(cinema.getVido()), Double.parseDouble(cinema.getKinhdo()));

                if(distance < 5){
                    idCinemas.add(cinema.getId());
                }

            }
        }

        return idCinemas;
    }

    public void loadCinemaSuggest(){
        Integer[] idCinemasRequest = new Integer[50];
        ArrayList<Integer> idCinemas = filterCinema(currentlocation);
        idCinemas.toArray(idCinemasRequest);
        int idMovie = getIdMovie();
        Service service = RetrofitUtil.getService(ChooseSessionActivity.this);
        Call<ArrayList<Cinema>> call = service.getCinemaSuggest(idCinemasRequest,idMovie);

        call.enqueue(new Callback<ArrayList<Cinema>>() {
            @Override
            public void onResponse(Call<ArrayList<Cinema>> call, Response<ArrayList<Cinema>> response) {
                cinemaSuggest.clear();

                if(response.body() != null &&response.body().size() != 0 ){
                    cinemaSuggest.addAll(response.body());
                }

                filterNearMeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Cinema>> call, Throwable t) {

            }
        });
    }

    public int getIndexCinema(int idCinema){
        if(cinemas != null && cinemas.size() != 0){
            int countCinema = cinemas.size();

            for(int i = 0; i < countCinema; i++){
                if(cinemas.get(i).getId() == idCinema){
                    return i;
                }
            }
        }

        return -1;
    }

    public void updateUI(){
        Intent intentScreenSchedule = getIntent();

        if(intentScreenSchedule.hasExtra("SCREEN_CINEMA")){
            int idCinema = intentScreenSchedule.getIntExtra("ID_CINEMA", 0);
            int idMovie = intentScreenSchedule.getIntExtra("ID_MOVIE", 0);

            spinner.setSelection(getIndexCinema(idCinema));
            spinner.setEnabled(false);

            loadXuatChieu(idCinema, idMovie, Util.formatDateClient(Util.getCurrentDate()));
        }else{
            Cinema cinema = (Cinema) spinner.getSelectedItem();

            if(cinema != null){
                String currentDate = Util.formatDateByCalendar(Calendar.getInstance());
                loadXuatChieu(cinema.getId(), getIdMovie(), Util.formatDateClientToServer(currentDate));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCinema();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCALTION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Util.ShowToastErrorMessage(ChooseSessionActivity.this, "Quyền từ chối");
            }
        }
    }

}