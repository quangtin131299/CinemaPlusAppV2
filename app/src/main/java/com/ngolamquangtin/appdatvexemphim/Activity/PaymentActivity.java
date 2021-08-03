package com.ngolamquangtin.appdatvexemphim.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astritveliu.boom.Boom;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.makeramen.roundedimageview.RoundedImageView;
import com.ngolamquangtin.appdatvexemphim.Adapter.PopCornPayMentAdapter;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.PopCorn;
import com.ngolamquangtin.appdatvexemphim.DTO.SeatV2;
import com.ngolamquangtin.appdatvexemphim.DTO.TickerBook;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.momo.momo_partner.AppMoMoLib;

public class PaymentActivity extends AppCompatActivity {

    final int PAYMENT_ONLINE_MOMO = 1;
    final int PAYMENT_ONLINE_PAPAL = 0;
    final String MERCHANTNAME = "Cinema Plust +";
    final String merchantCode = "MOMO34SR20201026";
    final String merchantNameLabel = "Cinema Plust +";
    final String description = "Thanh toán mua ve online";

    Runnable runnable;
    Handler handler;
    int mave = 0;
    int idghe = 0;
    int idphong = 0;
    int idsuat = 0;
    int idhoadon = 0;
    int environment = 0;//developer default
    String ngaydat;
    TickerBook tickerBook;
    EditText edttenkhachhang, edtsodienthoai;
    Button btnthanhtoan, btnBackTo;
    String amount = "45000";
    String fee = "0";
    Dialog dialogError, dialogProcess, dialogSucess;
    RadioGroup rdoGroupMethodPay;
    RadioButton rdoMethodPaytMoMo;
    TextView txtCountSeatBooking
            , txtMessPopcorn
            , txtTotalAmount
            , txtTime
            , txtNameMovie
            , txtDateBooking
            , txtTimeMovie
            , txtCinema
            , txtSeat
            , txtTotalAmoutPopCorn;

    PopCornPayMentAdapter popCornPayAdapter;
    RecyclerView ryPopCorn;
    RoundedImageView imgmoive;
    CountDownTimer timerPayment;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);

        addControls();

        startCountDow();

        setTickerBooking();

        initAdapterPopCornPay();

        updateUI();

        addEvents();

    }

    public void initAdapterPopCornPay() {
        if(tickerBook.getPopcorns().size() != 0){
            popCornPayAdapter = new PopCornPayMentAdapter(PaymentActivity.this, tickerBook.getPopcorns());

            LinearLayoutManager linearManger = new LinearLayoutManager(PaymentActivity.this);
            linearManger.setOrientation(RecyclerView.HORIZONTAL);

            ryPopCorn.setLayoutManager(linearManger);
            ryPopCorn.hasFixedSize();
            ryPopCorn.setAdapter(popCornPayAdapter);
        }else{
            ryPopCorn.setVisibility(View.INVISIBLE);
            txtMessPopcorn.setVisibility(View.VISIBLE);
        }

    }

    public void startCountDow() {
        timerPayment =  new CountDownTimer(180000 , 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;


                txtTime.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                txtTime.setText("00:00:00");

                Intent intentToScreenHome = new Intent(PaymentActivity.this, HomeActivity.class);

                startActivity(intentToScreenHome);

                finish();
            }

        }.start();
    }

    public void updateTrangThaiVe() {
//        RequestQueue requestQueue = Volley.newRequestQueue(PaymentActivity.this);
//        String url = String.format(Util.LINK_UPDATESTATUSVE, idphong, idghe, ngaydat, idsuat, mave, idhoadon);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                if (response != null) {
//                    Toast.makeText(PaymentActivity.this, "Vé của bạn đã bị hủy !", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(stringRequest);
    }

    private void addControls() {
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                updateTrangThaiVe();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this)
//                                .setTitle("Thông báo")
//                                .setMessage("Vé của bạn đã bị hủy!");
//                        builder.show();
//                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                            @Override
//                            public void onDismiss(DialogInterface dialog) {
//                                Intent i = new Intent(PaymentActivity.this, HomeActivity.class);
//                                startActivity(i);
//                            }
//                        });
//                    }
//                });
//            }
//        };

//        handler = new Handler();

        dialogError = new Dialog(PaymentActivity.this);
        dialogProcess = new Dialog(PaymentActivity.this);
        dialogSucess = new Dialog(PaymentActivity.this);
        imgmoive = findViewById(R.id.imgmoive);
        txtNameMovie = findViewById(R.id.txtnamemovie);
        txtDateBooking = findViewById(R.id.txtdatebooking);
        txtTimeMovie = findViewById(R.id.txttimemovie);
        txtCinema = findViewById(R.id.txtcinema);
        txtSeat = findViewById(R.id.txtseat);
        txtTotalAmoutPopCorn = findViewById(R.id.txttotalamoutpopcorn);
        ryPopCorn = findViewById(R.id.rypopcorn);
        rdoGroupMethodPay = findViewById(R.id.rdogroupmethopayment);
        rdoMethodPaytMoMo = findViewById(R.id.rdopaymomo);
        btnthanhtoan = findViewById(R.id.btnactionpayment);
        edttenkhachhang = findViewById(R.id.edttenkhachhang);
        btnBackTo = findViewById(R.id.btnbackto);
        txtCountSeatBooking = findViewById(R.id.txtcountseatbooking);
        txtTotalAmount = findViewById(R.id.txttotalamount);
        txtTime = findViewById(R.id.txttime);
        txtMessPopcorn = findViewById(R.id.txtmesspopcorn);
//        handler.postDelayed(runnable, 30000);
    }

    private void addEvents() {
        btnthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                handler.removeCallbacks(runnable);
                if(rdoGroupMethodPay.getCheckedRadioButtonId() != -1){
                    if(rdoMethodPaytMoMo.isChecked()){
                        amount = String.valueOf(calulatorTotalAmount());

                        showDialogProcess();

//                        requestPayment();

                        processTickerBooking(PAYMENT_ONLINE_MOMO);
                    }else{

                    }
                }

//                xulyDatVe();
            }
        });

        btnBackTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        new Boom(btnthanhtoan);
        new Boom(btnBackTo);
    }

    private void updateUI() {
        sharedPreferences = getSharedPreferences("datalogin", Context.MODE_PRIVATE);
        edttenkhachhang.setText(sharedPreferences.getString("hoten", ""));

        if(txtCountSeatBooking != null){
            txtCountSeatBooking.setText(tickerBook.getIdSeats().size() + getResources().getString(R.string.seat));
        }

        txtNameMovie.setText(getNameMovie());
        txtCinema.setText(getNameCinema());
        txtDateBooking.setText(Util.formatDateServerToClient(tickerBook.getNgaydat()));

        String strImageMovie = getImageMovie();

        if(!strImageMovie.isEmpty()){
            Glide.with(PaymentActivity.this).asBitmap().load(strImageMovie).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource,  Transition<? super Bitmap> transition) {
                    imgmoive.setImageBitmap(null);
                    imgmoive.setBackground(new BitmapDrawable(getResources(), resource));
                }

                @Override
                public void onLoadCleared(@Nullable  Drawable placeholder) {
                    return;
                }

                @Override
                public void onLoadFailed(@Nullable  Drawable errorDrawable) {
                    return;
                }
            });
        }

        txtTotalAmoutPopCorn.setText(getResources().getString(R.string.totalAmountPopcorn)+ " " + Util.formatAmount(calulatorAmountPopCorn()) + "đ" );
        txtSeat.setText(getResources().getString(R.string.seat) +": "  + getAllSeats());
        txtTimeMovie.setText(getShowTime() + " -> " + calulatorEndTimeMovie(getShowTime(), getTimeMovie()));
        txtTotalAmount.setText(Util.formatAmount(calulatorTotalAmount()) +"đ");
    }

    private int calulatorTotalAmount(){
        int totalAmount = 0;

        if(tickerBook != null && tickerBook.getIdSeats().size() != 0){
            int countSeat = tickerBook.getIdSeats().size();

            for (int i = 0; i < countSeat ; i++){
                totalAmount += 45000;
            }
        }

        return totalAmount + calulatorAmountPopCorn();
    }

    private void processTickerBooking(int methoddPay) {
        if (tickerBook != null) {
            if (tickerBook.getIdSeats() != null
                        && tickerBook.getIdSeats().size() != 0
                        && tickerBook.getIdkhachhang() != 0
                        && tickerBook.getIdphong() != 0
                        && tickerBook.getIdrap() != 0
                        && tickerBook.getNgaydat().equals("") == false) {
                tickerBook.setTrangthai("Đã đặt");
                tickerBook.setMethodPay(methoddPay);
                tickerBook.setUnitPrice(45000); //hash code

                Service service = RetrofitUtil.getService(PaymentActivity.this);
                Call<Integer> call = service.processTickerBooking(tickerBook);
                call.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        int statusCode = response.body();

                        if(statusCode == 1){
                            dismissDialogProcess();

                            showDiaLogSuccess("Đặt vé thành công !");

                        }else{
                            dismissDialogProcess();
                            showDialogMessageError("Đặt vé thất bại !");
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        dismissDialogProcess();
                        showDialogMessageError("Đặt vé thất bại !");
                    }
                });
            } else {
                dismissDialogProcess();
                showDialogMessageError("Thong tin dat ve khong hop le !");
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if (data != null) {
                if (data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
//                    processTickerBooking(PAYMENT_ONLINE_MOMO);
//                    AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
//                    builder.setTitle("Thông báo !");
//                    builder.setMessage("Vé của bạn đã được đặt thành công !");
//                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                        @Override
//                        public void onDismiss(DialogInterface dialog) {
//                            Intent i = new Intent(PaymentActivity.this, HomeActivity.class);
//                            startActivity(i);
//                        }
//                    });

                    String token = data.getStringExtra("data"); //Token response
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if (env == null) {
                        env = "app";
                    }

                    if (token != null && !token.equals("")) {
                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
                        // IF Momo topup success, continue to process your order
                        processTickerBooking(PAYMENT_ONLINE_MOMO);
                    } else {
                        showDialogMessageError("Đặt vé thất bại");
                    }
                } else if (data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL
                    String message = data.getStringExtra("message") != null ? data.getStringExtra("message") : "Thất bại";
                    showDialogMessageError("Đặt vé thất bại");

                } else if (data.getIntExtra("status", -1) == 2) {

                    showDialogMessageError("Đặt vé thất bại");
                } else {
                    showDialogMessageError("Đặt vé thất bại");
                }
            } else {
                showDialogMessageError("Đặt vé thất bại");
            }
        } else {
            showDialogMessageError("Đặt vé thất bại");
        }
    }

    //Get token through MoMo app
    private void requestPayment() {

        Intent i = getIntent();
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);

        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", MERCHANTNAME); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", amount); //Kiểu integer
        eventValue.put("orderId", "orderId123456789"); //uniqueue id cho BillId, giá trị duy nhất cho mỗi BILL
        eventValue.put("orderLabel", "Mã đơn hàng"); //gán nhãn
        eventValue.put("merchantnamelabel", "Mua bán");//gán nhãn
        eventValue.put("fee", fee); //Kiểu integer
        eventValue.put("description", description); //mô tả đơn hàng - short description
        eventValue.put("requestId", merchantCode + "merchant_billId_" + System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);

        JSONObject objExtraData = new JSONObject();

        try {
            objExtraData.put("site_code", "1");
            objExtraData.put("site_name", i.getStringExtra("TEN_RAP"));
            objExtraData.put("screen_code", 0);
            objExtraData.put("screen_name", "Special");
            objExtraData.put("movie_name", i.getStringExtra("TEN_PHIM"));
            objExtraData.put("movie_format", "HD");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        eventValue.put("extraData", objExtraData.toString());
        eventValue.put("extra", "");

        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);
    }

    private void showDialogMessageError(String messError){
        if(dialogError != null){
            dialogError.setContentView(R.layout.dialog_failed);

            dialogError.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            TextView txtMessError =  dialogError.findViewById(R.id.txtmess);
            Button btnOK = dialogError.findViewById(R.id.btnOK);

            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            txtMessError.setText(messError);

            new Boom(btnOK);

            dialogError.show();
        }
    }

    private void dismissDialogMessageError(){
        if(dialogError != null && dialogError.isShowing()){
            dialogError.dismiss();
        }
    }

    private void showDialogProcess(){
        if(dialogProcess != null){
            dialogProcess.setContentView(R.layout.dialog_processing);
            dialogProcess.setCanceledOnTouchOutside(false);
            dialogProcess.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialogProcess.show();
        }
    }

    private void dismissDialogProcess(){
        if(dialogProcess != null && dialogProcess.isShowing()){
            dialogProcess.dismiss();
        }
    }

    private void setTickerBooking(){
        Intent intentScreenSelectSeat = getIntent();
        if(intentScreenSelectSeat.hasExtra("TICKERBOOK")){
            tickerBook = (TickerBook) intentScreenSelectSeat.getSerializableExtra("TICKERBOOK");
        }
    }

    private void showDiaLogSuccess(String messSuccess){
        if(dialogSucess != null){
            dialogSucess.setContentView(R.layout.dialog_sucess);
            dialogSucess.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            TextView txtMessSuccess = dialogSucess.findViewById(R.id.txtmess);
            if(messSuccess.equals("") == false){
                txtMessSuccess.setText(messSuccess);
            }
            Button btnOk = dialogSucess.findViewById(R.id.btnOK);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissDialogSuccess();
                    Intent intentToScreenMain = new Intent(PaymentActivity.this, HomeActivity.class);
                    startActivity(intentToScreenMain);
                    finish();
                }
            });

            new Boom(btnOk);

            dialogSucess.show();
        }
    }

    private void dismissDialogSuccess(){
        if(dialogSucess != null && dialogSucess.isShowing()){
            dialogSucess.dismiss();
        }
    }

    public String getNameMovie(){
        Intent intentScreenSelectSeat = getIntent();

        if(intentScreenSelectSeat.hasExtra("NAME_MOVIE")){
            return  intentScreenSelectSeat.getStringExtra("NAME_MOVIE");
        }

        return "";
    }

    public String getNameCinema(){
        Intent intentScreenSelectSeat = getIntent();

        if(intentScreenSelectSeat.hasExtra("NAME_CINEMA")){
            return  intentScreenSelectSeat.getStringExtra("NAME_CINEMA");
        }

        return "";
    }

    public String getImageMovie(){
        Intent intentScreenPopCorn = getIntent();

        if (intentScreenPopCorn.hasExtra("IMAGE_MOVIE")) {
            return intentScreenPopCorn.getStringExtra("IMAGE_MOVIE");
        }

        return "";
    }

    public String calulatorEndTimeMovie(String showTime, int time){
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date d = null;
        String endTime = "";

        try {
            d = df.parse(showTime);
            Calendar cal = Calendar.getInstance();

            cal.setTime(d);

            cal.add(Calendar.MINUTE, time);

            endTime = df.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return endTime;
    }

    public String getAllSeats(){
        ArrayList<SeatV2> seats = tickerBook.getIdSeats();
        String result = "";

        if(seats.size() != 0){
            int countSeat = seats.size();

            for (int i = 0; i < countSeat; i++) {
                result += seats.get(i).getTenGhe().trim().concat(", ");
            }
        }

        return result;
    }

    public String getShowTime(){
        Intent intentScreenPopCorn = getIntent();

        if (intentScreenPopCorn.hasExtra("SHOW_TIME")) {
            return intentScreenPopCorn.getStringExtra("SHOW_TIME");
        }

        return "";
    }

    public int getTimeMovie(){
        Intent intentScreenPopCorn = getIntent();

        if (intentScreenPopCorn.hasExtra("TIME_MOVIE")) {
            return intentScreenPopCorn.getIntExtra("TIME_MOVIE", 0);
        }

        return 0;
    }

    public int calulatorAmountPopCorn(){
        int amount = 0;

        ArrayList<PopCorn> popCorns = tickerBook.getPopcorns();

        if(tickerBook.getPopcorns().size() != 0){
            int countPopCorn = tickerBook.getPopcorns().size();

            for(int i = 0; i < countPopCorn; i++){
                amount += popCorns.get(i).calulatorAmount();
            }
        }

        return amount;
    }

    @Override
    protected void onPause() {
        super.onPause();

        timerPayment.cancel();
    }

    
}