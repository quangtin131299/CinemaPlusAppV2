package com.ngolamquangtin.appdatvexemphim.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngolamquangtin.appdatvexemphim.DTO.Ticker;
import com.ngolamquangtin.appdatvexemphim.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailTickerActivity extends AppCompatActivity {

    TextView txttenphim,txtmave,txtngaygio, txtcinema, txtghe, txtdiachi, txtthoigian;
    ImageView imgqr, imgphim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ticker);
        addControls();
        addDataOnView();
    }

    private void addControls() {
        txttenphim = findViewById(R.id.txttenphim);
        txtthoigian= findViewById(R.id.txtthoigian);
        txtmave = findViewById(R.id.txtmave);
        txtngaygio = findViewById(R.id.txtngaygio);
        txtcinema = findViewById(R.id.txtcinema);
        txtghe = findViewById(R.id.txtghe);
        txtdiachi = findViewById(R.id.txtdiachi);
        imgqr = findViewById(R.id.imgqr);
        imgphim = findViewById(R.id.imgphim);
    }

    private void addDataOnView() {
        Intent i = getIntent();
        if(i.hasExtra("TICKER")){
            Ticker ticker  = (Ticker) i.getSerializableExtra("TICKER");
            txttenphim.setText(ticker.getTenphim());
            txtmave.setText("Mã vé đã đặt: " + ticker.getId());
            String data = ticker.getId() + "";
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            try {
                Bitmap bitmap = barcodeEncoder.encodeBitmap(data, BarcodeFormat.QR_CODE, 250, 250);
                imgqr.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            DateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.US);
            try {
                Date date = inputFormat.parse(ticker.getNgaydat());
                txtngaygio.setText(simpleDateFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            };
            txtthoigian.setText(ticker.getThoigianphim());
            txtghe.setText(ticker.getTenghe());
            txtcinema.setText(ticker.getTenrap());
            txtdiachi.setText(ticker.getDiachirap());
            Picasso.with(DetailTickerActivity.this).load(ticker.getHinhphim()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    imgphim.setBackground(new BitmapDrawable(DetailTickerActivity.this.getResources(), bitmap));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });


        }
    }
}
