package com.ngolamquangtin.appdatvexemphim.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.astritveliu.boom.Boom;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.CustomerV2;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserActivity extends AppCompatActivity {

    final int STATE_UPDATE = 0;
    final int ENABLE_AGE = 18;

    TextView edtphone, edtname, edtemail, edtdate;
    Button btncapnhat, btnhuy;
    SharedPreferences sharedPreferences;
    Dialog dialogProcess, dialogError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        addControls();

        capnhatThongTinKhachHangUI();

        addEvents();
    }

    private void capnhatThongTinKhachHangUI() {
        Intent i = getIntent();
        if (i.hasExtra("HOTEN") || i.hasExtra("EMAIL") || i.hasExtra("SDT") || i.hasExtra("NGAYSINH")) {
            edtname.setText(i.getStringExtra("HOTEN"));
//            edtemail.setText(i.getStringExtra("EMAIL"));
            edtphone.setText(i.getStringExtra("SDT"));

            edtdate.setText(Util.formatDateServerToClient(i.getStringExtra("NGAYSINH")));
        }
    }

    private void addEvents() {
        btncapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoten = edtname.getText().toString().trim();
                String email = "";
                String phone = edtphone.getText().toString().trim();
                String ngaysinh = edtdate.getText().toString().trim();
                capnhatThongTinKhachHang(hoten, email, ngaysinh, phone);
            }
        });

        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate();
            }
        });

        new Boom(btncapnhat);
        new Boom(btnhuy);
    }

    public void chooseDate(){
        final int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateUserActivity.this, R.style.DatePic, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (validateBirthDay(currentYear, year) >= ENABLE_AGE) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.YEAR, year);
                    edtdate.setText(simpleDateFormat.format(calendar.getTime()));
                } else {
                    Util.ShowToastErrorMessage(UpdateUserActivity.this, getResources().getString(R.string.invailBirthday));
                }

            }
        }, currentYear, month, day);

        datePickerDialog.show();
    }

    private void capnhatThongTinKhachHang(String name, String email, String date, String phone) {
        int id = Integer.parseInt(sharedPreferences.getString("id", ""));
        String oldNumberPhone = sharedPreferences.getString("sdt", "");
        String hoten = name;
        String phonereq = phone;
        CustomerV2 customer = new CustomerV2();
        customer.setId(id);
        customer.setHoTen(hoten);
        customer.setSdt(phonereq);
        customer.setNgaySinh(Util.formatDateClientToServer(date));

        if(!oldNumberPhone.equals(phone)){
            Intent intentToScreenOTP = new Intent(UpdateUserActivity.this, OTPActivity.class);

            intentToScreenOTP.putExtra("CUSTOMER", customer);
            intentToScreenOTP.putExtra("STATE", STATE_UPDATE);

            startActivity(intentToScreenOTP);
        }else{
            updateInforUser(customer);
        }
    }

    private void addControls() {
        dialogProcess = new Dialog(UpdateUserActivity.this);
        dialogError = new Dialog(UpdateUserActivity.this);
        edtphone = findViewById(R.id.edtphone);
        edtname = findViewById(R.id.edtname);
        edtdate = findViewById(R.id.edtdate);
        btncapnhat = findViewById(R.id.btncapnhat);
        btnhuy = findViewById(R.id.btnhuy);
        sharedPreferences = getSharedPreferences("datalogin", Context.MODE_PRIVATE);
    }

    public int validateBirthDay(int yearCurrent, int yearSelected) {
        return yearCurrent - yearSelected;
    }

    public void showDialogProcess() {
        if (dialogProcess != null) {
            dialogProcess.setContentView(R.layout.dialog_processing);

            dialogProcess.getWindow().setBackgroundDrawableResource(R.color.transparent);

            dialogProcess.show();
        }
    }

    public void showDialogError(String mess) {
        if (dialogError != null) {
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

            dialogError.show();
        }
    }

    public void dismissDialogProcess() {
        if (dialogProcess != null && dialogProcess.isShowing()) {
            dialogProcess.dismiss();
        }
    }

    public void updateInforUser(CustomerV2 customer){
        Service service = RetrofitUtil.getService(UpdateUserActivity.this);
        Call<CustomerV2> customerCall = service.updateTTUser(customer);
        customerCall.enqueue(new Callback<CustomerV2>() {
            @Override
            public void onResponse(Call<CustomerV2> call, Response<CustomerV2> response) {
                dismissDialogProcess();
                CustomerV2 newCustomer = response.body();

                if (newCustomer != null) {
                    showDialogProcess();
                    updateSharePreference(newCustomer);
                    finish();
                } else {
                    showDialogFails();
                }
            }

            @Override
            public void onFailure(Call<CustomerV2> call, Throwable t) {
                showDialogFails();
            }
        });
    }

    public void showDialogFails(){
        if(dialogProcess != null){
            dialogError.getWindow().setBackgroundDrawableResource(R.color.transparent);

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

    public void dismissDialogError() {
        if (dialogError != null && dialogError.isShowing()) {
            dialogError.dismiss();
        }
    }

    public void updateSharePreference(CustomerV2 newCustomer) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("hoten", newCustomer.getHoTen());
        editor.putString("ngaysinh", newCustomer.getNgaySinh());
        editor.apply();
        finish();
    }
}