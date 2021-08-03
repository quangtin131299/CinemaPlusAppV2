package com.ngolamquangtin.appdatvexemphim.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astritveliu.boom.Boom;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.ngolamquangtin.appdatvexemphim.Adapter.PopCornPayMentAdapter;
import com.ngolamquangtin.appdatvexemphim.Adapter.TickerDetailBillAdapter;
import com.ngolamquangtin.appdatvexemphim.DTO.BillV2;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import java.util.EnumMap;
import java.util.Map;

public class BillDetailActivity extends AppCompatActivity {

    ImageButton imgBtnQr;
    Button btnBackTo;
    PopCornPayMentAdapter popCornAdapter;
    TickerDetailBillAdapter seatAdapter;
    RecyclerView rySeats, ryPopcorn;
    TextView txtIdBill, txtDate, txtMethodPay, txtAmoutSeat,txtAmoutPopcorn,txtTotalAmount;
    BillV2 bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);

        addControls();

        addEvents();

        setBill();

        updateUI();

        loadSeats();

        loadPopCorn();
    }

    public void addEvents() {
        btnBackTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgBtnQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog btnSheetDialog = new BottomSheetDialog(BillDetailActivity.this, R.style.dialogQR);

                btnSheetDialog.setContentView(LayoutInflater.from(BillDetailActivity.this).inflate(R.layout.dialogbottom_qr, null));

                ImageView imgQr = btnSheetDialog.findViewById(R.id.imgqr);

                Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
                hints.put(EncodeHintType.MARGIN, 0);

                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

                try {
                    Bitmap bitmapQR = barcodeEncoder.encodeBitmap(String.valueOf(bill.getId()), BarcodeFormat.QR_CODE, 200, 200, hints);

                    imgQr.setImageBitmap(bitmapQR);
                } catch (WriterException e) {
                    e.printStackTrace();
                }


                btnSheetDialog.show();
            }
        });

        new Boom(imgBtnQr);
        new Boom(btnBackTo);
    }

    public void loadPopCorn() {
        popCornAdapter = new PopCornPayMentAdapter(BillDetailActivity.this, bill.getPopCorns());

        LinearLayoutManager layoutManager = new LinearLayoutManager(BillDetailActivity.this);

        layoutManager.setOrientation(RecyclerView.HORIZONTAL);

        ryPopcorn.setLayoutManager(layoutManager);
        ryPopcorn.setAdapter(popCornAdapter);
    }

    public void loadSeats() {
        seatAdapter = new TickerDetailBillAdapter(BillDetailActivity.this, bill.getTicketV2s());

        LinearLayoutManager layoutManager = new LinearLayoutManager(BillDetailActivity.this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);

        rySeats.setLayoutManager(layoutManager);
        rySeats.setAdapter(seatAdapter);

    }

    public void updateUI() {
        txtIdBill.setText("Mã hóa đơn: " + bill.getId());
        txtDate.setText("Ngày lập hóa đơn: "  + Util.formatDateServerToClient(bill.getNgaylaphoadon()));

        if(bill.getPtThanhToan().equals("1")){
            txtMethodPay.setText("Thanh toán bằng momo");
        }

        int amountPopcorn = calulatorAmountPopcorn();

        txtAmoutPopcorn.setText(Util.formatAmount(amountPopcorn) + " đ");
        txtAmoutSeat.setText(Util.formatAmount(bill.getThanhtien()) + " đ");
        txtTotalAmount.setText(Util.formatAmount(bill.getThanhtien() + amountPopcorn)+ " đ");
    }

    public int calulatorAmountPopcorn(){
        int amountPopcorn = 0;

        if(bill.getPopCorns().size() != 0){
            int countAmount = bill.getPopCorns().size();

            for (int i = 0; i < countAmount; i++) {
                amountPopcorn += bill.getPopCorns().get(i).calulatorAmount();
            }
        }

        return amountPopcorn;
    }

    private void addControls() {
        imgBtnQr = findViewById(R.id.imgbtnqr);
        btnBackTo = findViewById(R.id.btnbackto);
        rySeats = findViewById(R.id.ryseats);
        ryPopcorn = findViewById(R.id.rypopcorndetailbill);
        txtIdBill = findViewById(R.id.txtidbill);
        txtDate = findViewById(R.id.txtdate);
        txtMethodPay = findViewById(R.id.txtmethodpay);
        txtAmoutSeat = findViewById(R.id.txtamoutseat);
        txtAmoutPopcorn = findViewById(R.id.txtamoutpopcorn);
        txtTotalAmount = findViewById(R.id.txttotalamount);
    }

    public void setBill(){
        Intent intentScreenBill = getIntent();

        if(intentScreenBill.hasExtra("BILL")){
            bill = (BillV2) intentScreenBill.getSerializableExtra("BILL");
        }
    }


}