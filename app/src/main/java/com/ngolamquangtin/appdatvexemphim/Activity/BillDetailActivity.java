package com.ngolamquangtin.appdatvexemphim.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.ngolamquangtin.appdatvexemphim.Adapter.SeatAdapter;
import com.ngolamquangtin.appdatvexemphim.R;

public class BillDetailActivity extends AppCompatActivity {

    ListView lvSeat;
    SeatAdapter seatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);

        addControls();
    }

    private void addControls() {
        lvSeat = findViewById(R.id.lvseate);
        seatAdapter = new SeatAdapter(BillDetailActivity.this);
        lvSeat.setAdapter(seatAdapter);
    }


}