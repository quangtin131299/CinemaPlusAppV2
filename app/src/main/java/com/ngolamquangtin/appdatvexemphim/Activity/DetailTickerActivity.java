package com.ngolamquangtin.appdatvexemphim.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.astritveliu.boom.Boom;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.Ticker;
import com.ngolamquangtin.appdatvexemphim.DTO.TicketV2;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailTickerActivity extends AppCompatActivity {


    Button btnBackto, btnCancelTicker;
    TextView txttenphim,txtmave,txtngaygio, txtcinema, txtghe, txtdiachi, txtthoigian;
    ImageView imgqr, imgphim;
    TicketV2 ticker;
    Dialog dialogProcess, dialogSucess, dialogError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ticker);

        addControls();

        addDataOnView();

        addEvents();
    }

    public void initQRBitmapDialog(ImageView imgQRDialog) {
        if(ticker != null){
            Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.MARGIN, 0);

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

            try {
                Bitmap bitmapQR = barcodeEncoder.encodeBitmap(String.valueOf(ticker.getIdHoaDon()), BarcodeFormat.QR_CODE, 200, 200, hints);

                imgQRDialog.setImageBitmap(bitmapQR);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }

    }

    public void addEvents() {
        btnBackto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomDialogQR = new BottomSheetDialog(DetailTickerActivity.this, R.style.dialogQR);

                View viewDialog = LayoutInflater.from(DetailTickerActivity.this).inflate(R.layout.dialogbottom_qr, null, false);

                bottomDialogQR.setContentView(viewDialog);

                bottomDialogQR.getWindow().setBackgroundDrawableResource(R.color.transparent);

                ImageView imgQR = bottomDialogQR.findViewById(R.id.imgqr);

                initQRBitmapDialog(imgQR);

                bottomDialogQR.show();
            }
        });

        btnCancelTicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String [] dateBooking = ticker.getNgayDat().split("-");
//
//                Calendar cl = Calendar.getInstance();
//
//                cl.set(Calendar.YEAR, Integer.parseInt(dateBooking[0]));
//                cl.set(Calendar.MONTH, Integer.parseInt(dateBooking[1]));
//                cl.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateBooking[2]));
//
//                calulatorTime(Calendar.getInstance(), cl);
                processCancelTicker(ticker);
            }
        });

        new Boom(imgqr);
        new Boom(btnBackto);
        new Boom(btnCancelTicker);
    }

    private void addControls() {
        btnBackto = findViewById(R.id.btnbackto);
        btnCancelTicker = findViewById(R.id.btncancel);
        txttenphim = findViewById(R.id.txttenphim);
        txtthoigian = findViewById(R.id.txttime);
        txtmave = findViewById(R.id.txtmave);
        txtngaygio = findViewById(R.id.txtngaygio);
        txtcinema = findViewById(R.id.txtcinema);
        txtghe = findViewById(R.id.txtghe);
        txtdiachi = findViewById(R.id.txtdiachi);
        imgqr = findViewById(R.id.imgqr);
        imgphim = findViewById(R.id.imgphim);
        dialogSucess = new Dialog(DetailTickerActivity.this);
        dialogError = new Dialog(DetailTickerActivity.this);
        dialogProcess = new Dialog(DetailTickerActivity.this);
    }

    private void addDataOnView() {
        Intent i = getIntent();

        if(i.hasExtra("TICKER")){

            ticker  = (TicketV2) i.getSerializableExtra("TICKER");

            txttenphim.setText(ticker.getPhim().getTenphim());
            txtmave.setText("Mã vé đã đặt: " + ticker.getId());

            String data = ticker.getId() + "";
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

            try {
                Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
                hints.put(EncodeHintType.MARGIN, 0);

                Bitmap bitmapQR = barcodeEncoder.encodeBitmap(String.valueOf(ticker.getIdHoaDon()), BarcodeFormat.QR_CODE, 100, 100, hints);

                imgqr.setBackground(new BitmapDrawable(getResources(),bitmapQR));
            } catch (WriterException e) {
                e.printStackTrace();
            }

            txtngaygio.setText(Util.formatDateServerToClient(ticker.getNgayDat()) + "\t" + Util.formatTime(ticker.getSuatchieu().getGio()));
            txtthoigian.setText(ticker.getPhim().getThoigian() + " phút");
            txtghe.setText(ticker.getGhe().getTenGhe());
            txtcinema.setText(ticker.getRap().getTenrap());
            txtdiachi.setText(ticker.getRap().getDiachi());

            if(ticker.getPhim().getHinh() != null && !ticker.getPhim().getHinh().isEmpty()){

                Glide.with(DetailTickerActivity.this).asBitmap().load(ticker.getPhim().getHinh()).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable  Transition<? super Bitmap> transition) {
                        imgphim.setImageBitmap(null);
                        imgphim.setBackground(new BitmapDrawable(DetailTickerActivity.this.getResources(), resource));
                    }

                    @Override
                    public void onLoadCleared(@Nullable  Drawable placeholder) {
                        return;
                    }

                    @Override
                    public void onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable Drawable errorDrawable) {
                        String valueColor = Integer.toHexString(getApplicationContext().getResources().getColor(R.color.colorBackround, null));
                        imgphim.setBackgroundColor(Color.parseColor("#"+valueColor));
                        imgphim.setImageResource(R.drawable.ic_clapperboard);
                        imgphim.setScaleType(ImageView.ScaleType.CENTER);
                    }
                });

            }
        }
    }

    public void processCancelTicker(TicketV2 ticker){
        showDialogProcess();
        Service service = RetrofitUtil.getService(DetailTickerActivity.this);
        Call<Integer> call = service.updateStatusCancelTicker(ticker);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                dismissDialogProcess();

                if(response.body()!= null && response.body() == 1){
                    showDialogSuccess("Hủy vé thành công !");
                }else{
                    showDialogError("Hủy vé thất bại !");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                dismissDialogProcess();

                showDialogError("Lỗi kết nối");
            }
        });
    }

    public void showDialogProcess(){
        if(dialogProcess != null){
            dialogProcess.setContentView(R.layout.dialog_processing);

            dialogProcess.getWindow().setBackgroundDrawableResource(R.color.transparent);

            dialogProcess.show();
        }
    }

    public void dismissDialogProcess(){
        if(dialogProcess != null && dialogProcess.isShowing()){
            dialogProcess.dismiss();
        }
    }

    public void showDialogSuccess(String mess){
        if(dialogSucess != null){
            dialogSucess.setContentView(R.layout.dialog_sucess);

            dialogSucess.getWindow().setBackgroundDrawableResource(R.color.transparent);

            TextView txtMess = dialogSucess.findViewById(R.id.txtmess);
            Button btnOk = dialogSucess.findViewById(R.id.btnOK);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogProcess.dismiss();
                    finish();
                }
            });

            txtMess.setText(mess);

            new Boom(btnOk);

            dialogSucess.show();
        }
    }

    public void showDialogError(String mess){
        if(dialogError != null){
            dialogError.setContentView(R.layout.dialog_failed);

            dialogError.getWindow().setBackgroundDrawableResource(R.color.transparent);

            TextView txtMess = dialogError.findViewById(R.id.txtmess);
            Button btnOk = dialogError.findViewById(R.id.btnOK);

            txtMess.setText(mess);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogError.dismiss();
                }
            });

            new Boom(btnOk);

            dialogError.show();
        }
    }
    
    public void calulatorTime(Calendar currentTime, Calendar showTime){
        long time = showTime.getTime().getTime() - currentTime.getTime().getTime();

        Calendar calendarResult = Calendar.getInstance();
        calendarResult.getTime().setTime(time);

    }


}
