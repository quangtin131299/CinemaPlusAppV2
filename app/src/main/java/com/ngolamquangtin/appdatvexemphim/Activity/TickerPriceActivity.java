package com.ngolamquangtin.appdatvexemphim.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.astritveliu.boom.Boom;
import com.ngolamquangtin.appdatvexemphim.R;

public class TickerPriceActivity extends AppCompatActivity {

    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticker_price);

        addControl();

        addEvent();
    }

    private void addEvent() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        new Boom(imgBack);
    }

    private void addControl() {
        imgBack = findViewById(R.id.imgback);
    }
}