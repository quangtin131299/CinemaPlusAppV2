package com.ngolamquangtin.appdatvexemphim.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.astritveliu.boom.Boom;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.RoomV2;
import com.ngolamquangtin.appdatvexemphim.DTO.SeatV2;
import com.ngolamquangtin.appdatvexemphim.DTO.TickerBook;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectSeatActivity extends AppCompatActivity {


    ArrayList<SeatV2> seatBookings = new ArrayList<>();
    ArrayList<LinearLayout> arrlinear = new ArrayList<>();
    ArrayList<SeatV2> seatSelects = new ArrayList<>();
    TextView txttime, txtsophong, txttenphim, txtCountSeat;
    TickerBook tickerBook;
    Button btnthanhtoan, btnBackTo;
    ImageView imgtryvet;
    Runnable runnable;
    Handler handler;
    RoomV2 currentRoom;
    Dialog dialogProcessing, dialogError;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_seat);

        addControls();

        initAllSeat();

        reupdateSeat();

        loadDataPhong();

        addEvents();
    }

    private void initAllSeat() {
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
                    setEventForSeat((ImageView) temp);
                }
            }
        }
    }

    private void setEventForSeat(ImageView seat) {
        seat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView1 = (ImageView) v;
                if (imageView1.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.seattrong).getConstantState()) {
                    if (imageView1.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.seatchon).getConstantState()) {
                        imageView1.setImageResource(R.drawable.seattrong);
                    } else {
                        seatSelects.add((SeatV2) imageView1.getTag());

                        imageView1.setImageResource(R.drawable.seatchon);

                    }
                } else if (imageView1.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.seatdadat).getConstantState()) {
                    Toast.makeText(SelectSeatActivity.this, "Ghế đã đặt rồi", Toast.LENGTH_SHORT).show();
                } else {
                    deleteSeatBooking((SeatV2) imageView1.getTag());

                    imageView1.setImageResource(R.drawable.seattrong);
                }
            }
        });

        new Boom(seat);
    }


    private void setSeatCode(int idroom) {
        int idCinema = getIdCinema();
        int idMovie = getIdMovie();
        String currentDate = getCurrentDate();
        int idShowTime = getIdShowtime();

        Service service = RetrofitUtil.getService(SelectSeatActivity.this);
        Call<List<SeatV2>> listCall = service.getSeats(idMovie, currentDate, idroom, idShowTime, idCinema);
        listCall.enqueue(new Callback<List<SeatV2>>() {
            @Override
            public void onResponse(Call<List<SeatV2>> call, retrofit2.Response<List<SeatV2>> response) {
                ArrayList<SeatV2> seats = (ArrayList<SeatV2>) response.body();

                if (seats != null && seats.size() != 0) {
                    int sl = arrlinear.size();
                    int k = 0;

                    for (int i = 0; i < sl; i++) {
                        LinearLayout lineartemp = arrlinear.get(i);
                        int childCount = lineartemp.getChildCount();

                        for (int j = 0; j < childCount; j++) {
                            View temp = lineartemp.getChildAt(j);

                            if (temp instanceof ImageView) {
                                temp.setTag(seats.get(k++));
                            }
                        }
                    }
                }

                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);

            }

            @Override
            public void onFailure(Call<List<SeatV2>> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });
    }

    private void reupdateSeat() {
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull @NotNull Message msg) {
                if (msg.what == 1) {

                    getSeatsBooking(currentRoom.getId());
                } else if (msg.what == 0) {
                    int idRoom = msg.arg1;
                    setSeatCode(idRoom);
                }
            }
        };
//        handler.postDelayed(runnable, 5000);
    }

    private void loadDataPhong() {
        showDiaLogProcessing();
        Intent i = getIntent();
        txttenphim.setText(i.getStringExtra("TEN_PHIM"));
        if (i.hasExtra("ID_MOVIE")
                && i.hasExtra("Id_SHOWTIME")
                && i.hasExtra("ID_CINEMA")) {
            Service service = RetrofitUtil.getService(SelectSeatActivity.this);
            int idShowTime = i.getIntExtra("Id_SHOWTIME", 0);
            int idMovie = i.getIntExtra("ID_MOVIE", 0);
            int idCinema = i.getIntExtra("ID_CINEMA", 0);

            String currentDate = getCurrentDate();
            Call<RoomV2> listCall = service.getPhong(idMovie, idCinema, currentDate, idShowTime);
            listCall.enqueue(new Callback<RoomV2>() {
                @Override
                public void onResponse(Call<RoomV2> call, retrofit2.Response<RoomV2> response) {
                    if (response.body() != null) {
                        currentRoom = response.body();

                        dismissDialogProcessing();

                        updateRoom(response.body());

                        Message message = new Message();
                        message.what = 0;
                        message.arg1 = response.body().getId();
                        handler.sendMessage(message);
                    }
                }

                @Override
                public void onFailure(Call<RoomV2> call, Throwable t) {
                    Log.d("ABC", t.getMessage());
                    call.clone().enqueue(this);
                    dismissDialogProcessing();
                }
            });

        } else {
            dismissDialogProcessing();
        }
    }

    private void updateRoom(RoomV2 room) {
        tickerBook.setIdphong(room.getId());
        txtsophong.setText(room.getTenphong());
        txttime.setText(getShowTime());
    }

    private void addEvents() {
        btnBackTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tickerBook.setIdSeats(seatSelects);


                Intent intentToScreenPopCorn = new Intent(SelectSeatActivity.this, PopCornActivity.class);

                if (currentRoom != null) {
                    intentToScreenPopCorn.putExtra("ID_PHONG", currentRoom.getId());
                    tickerBook.setIdphong(currentRoom.getId());
                }

                intentToScreenPopCorn.putExtra("TICKERBOOK", tickerBook);

                intentToScreenPopCorn.putExtra("TEN_PHIM", getNameMovie());
                intentToScreenPopCorn.putExtra("TEN_RAP", getNameCinema());
                intentToScreenPopCorn.putExtra("IMAGE_MOVIE", getImageMovie());
                intentToScreenPopCorn.putExtra("SHOW_TIME", getShowTime());
                intentToScreenPopCorn.putExtra("TIME_MOVIE", getTime());

                startActivity(intentToScreenPopCorn);
            }
        });

        new Boom(btnBackTo);
        new Boom(btnthanhtoan);
    }

    public void setSeatBookings() {
        int countSeatBooking = seatBookings.size();

        for (int i = 0; i < countSeatBooking; i++) {
            SeatV2 seatV2 = seatBookings.get(i);
            updateUISeats(seatV2);
        }

    }

    private void updateUISeats(SeatV2 seat) {
        int sl = arrlinear.size();

        for (int i = 0; i < sl; i++) {
            LinearLayout lineartemp = arrlinear.get(i);
            int slchild = lineartemp.getChildCount();

            for (int j = 0; j < slchild; j++) {
                View temp = lineartemp.getChildAt(j);

                if (temp instanceof ImageView) {
                    ImageView tempimage = (ImageView) temp;
                    SeatV2 tempseat = seat;

//                    if (tempimage.getTag() != null) {

                    if (tempseat.getId() == Integer.parseInt(String.valueOf(((SeatV2)tempimage.getTag()).getId()))) {

                        if (tempseat.getTrangThai().equals("Đã đặt")) {

                            tempimage.setImageResource(R.drawable.seatdadat);

                        }
//                            else {
//
//                                tempimage.setImageResource(R.drawable.seattrong);
//                            }

                    }
//                    }
                }
            }
        }
    }


    public String getShowTime() {
        Intent intentScreenChoose = getIntent();

        if (intentScreenChoose.hasExtra("SHOW_TIME")) {
            return intentScreenChoose.getStringExtra("SHOW_TIME");
        }

        return "";
    }

    public int getIdMovie() {
        Intent intentScreenChooseTime = getIntent();

        if (intentScreenChooseTime.hasExtra("ID_MOVIE")) {
            return intentScreenChooseTime.getIntExtra("ID_MOVIE", 0);
        }

        return 0;
    }

    public String getCurrentDate() {
        Intent intentScreenChooseTime = getIntent();

        if (intentScreenChooseTime.hasExtra("NGAYDATHIENTAI")) {
            return intentScreenChooseTime.getStringExtra("NGAYDATHIENTAI");
        }

        return "";
    }

    public int getIdShowtime() {
        Intent intentScreenChoose = getIntent();

        if (intentScreenChoose.hasExtra("Id_SHOWTIME")) {
            return intentScreenChoose.getIntExtra("Id_SHOWTIME", 0);
        }

        return 0;
    }

    public int getIdCinema() {
        Intent intentScreenChoose = getIntent();

        if (intentScreenChoose.hasExtra("ID_CINEMA")) {
            return intentScreenChoose.getIntExtra("ID_CINEMA", 0);
        }

        return 0;
    }

    private void getSeatsBooking(int idRoom) {
        final Intent intent = getIntent();
        int idCinema = getIdCinema();
        int idMovie = intent.getIntExtra("ID_MOVIE", 0);
        int idShowTime = intent.getIntExtra("Id_SHOWTIME", 0);
        String currentDate = intent.getStringExtra("NGAYDATHIENTAI");
        Service service = RetrofitUtil.getService(SelectSeatActivity.this);
        Call<List<SeatV2>> listCall = service.getSeatBookings(idMovie, currentDate, idRoom, idShowTime, idCinema);
        listCall.enqueue(new Callback<List<SeatV2>>() {
            @Override
            public void onResponse(Call<List<SeatV2>> call, Response<List<SeatV2>> response) {
                seatBookings = (ArrayList<SeatV2>) response.body();

                setSeatBookings();
            }

            @Override
            public void onFailure(Call<List<SeatV2>> call, Throwable t) {
                listCall.clone().enqueue(this);
            }
        });
    }

    private void addControls() {
        dialogProcessing = new Dialog(SelectSeatActivity.this);
        dialogError = new Dialog(SelectSeatActivity.this);
        imgtryvet = new ImageView(SelectSeatActivity.this);
        txttime = findViewById(R.id.txttime);
        txtsophong = findViewById(R.id.txtsophong);
        txttenphim = findViewById(R.id.txttenphim);
        btnBackTo = findViewById(R.id.btnhuy);
        btnthanhtoan = findViewById(R.id.btnthanhtoan);

        Intent intent = getIntent();

        if (intent.hasExtra("TICKERBOOK")) {
            tickerBook = (TickerBook) intent.getSerializableExtra("TICKERBOOK");

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        reupdateSeat();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
//            handler.removeCallbacks(runnable);
    }

    private void deleteSeatBooking(SeatV2 idSeatUnselect) {
        if (seatSelects != null && seatSelects.size() != 0) {
            int indexOfIdSelectUnSelect = seatSelects.indexOf(idSeatUnselect);
            seatSelects.remove(indexOfIdSelectUnSelect);
        }
    }

    private void showDiaLogProcessing() {
        if (dialogProcessing != null) {
            dialogProcessing.setCanceledOnTouchOutside(false);
            dialogProcessing.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialogProcessing.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogProcessing.setContentView(R.layout.dialog_processing);
            dialogProcessing.show();
        }
    }

    private void dismissDialogProcessing() {
        if (dialogProcessing != null && dialogProcessing.isShowing()) {
            dialogProcessing.dismiss();
        }
    }

    private void showMessageError(String mess) {
        if (dialogError != null) {
            dialogError.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialogError.setContentView(R.layout.dialog_failed);
            Button btnOK = dialogError.findViewById(R.id.btnOK);
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogError.dismiss();
                }
            });
            dialogError.show();
        }
    }

    private void dismissDialogError() {
        if (dialogError != null && dialogError.isShowing()) {
            dialogError.dismiss();
        }
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

    public int getTime(){
        Intent intenScreenChooseSession = getIntent();

        if(intenScreenChooseSession.hasExtra("TIME_MOVIE")){
            return intenScreenChooseSession.getIntExtra("TIME_MOVIE", 0);
        }

        return 0;
    }

}

