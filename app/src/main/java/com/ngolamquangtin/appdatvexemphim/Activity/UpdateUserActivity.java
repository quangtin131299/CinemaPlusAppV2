package com.ngolamquangtin.appdatvexemphim.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.Customer;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserActivity extends AppCompatActivity {

    TextView edtphone, edtname, edtemail, edtdate;
    Button btnngay, btncapnhat, btnhuy;
    SharedPreferences sharedPreferences;

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
            edtemail.setText(i.getStringExtra("EMAIL"));
            edtphone.setText(i.getStringExtra("SDT"));
            edtdate.setText(i.getStringExtra("NGAYSINH"));
        }
    }

    private void addEvents() {
        btncapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hoten = edtname.getText().toString().trim();
                String email = edtemail.getText().toString().trim();
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
        btnngay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                int month = Calendar.getInstance().get(Calendar.MONTH);
                int year = Calendar.getInstance().get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateUserActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.YEAR, year);
                        edtdate.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    private void capnhatThongTinKhachHang(String name, String email, String date, String phone) {
//        RequestQueue requestQueue = Volley.newRequestQueue(UpdateUserActivity.this);
//        String url = Util.LINK_UPDATEUSER;
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Toast.makeText(UpdateUserActivity.this, response, Toast.LENGTH_LONG).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                String hoten = edtname.getText().toString().trim();
//                String email = edtemail.getText().toString().trim();
//                String phone = edtphone.getText().toString().trim();
//                String ngaysinh = edtdate.getText().toString().trim();
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                SimpleDateFormat ouput = new SimpleDateFormat("yyyy-MM-dd");
//                Calendar calendar = Calendar.getInstance();
//                try {
//                    calendar.setTime(simpleDateFormat.parse(ngaysinh));
//                    ngaysinh = ouput.format(calendar.getTime());
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//
//                Map<String, String> maps = new HashMap<>();
//                maps.put("hoten", hoten);
//                maps.put("email", email);
//                maps.put("ngaysinh", ngaysinh);
//                maps.put("sodienthoai", phone);
//                maps.put("idkhachhang", sharedPreferences.getString("id", ""));
//                return maps;
//            }
//        };
//        requestQueue.add(stringRequest);
        int id = Integer.parseInt(sharedPreferences.getString("id", ""));
        String hoten = name;
        String emailreq = email;
        String phonereq = phone;
        String ngaysinh = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat ouput = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date temp = simpleDateFormat.parse(date);
            ngaysinh = ouput.format(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String finalNgaysinh = ngaysinh;
        Customer customer = new Customer();
        customer.setID(id);
        customer.setHoTen(hoten);
        customer.setEmail(emailreq);
        customer.setSDT(phonereq);
        customer.setNgaySinh(ngaysinh);
        Service service = RetrofitUtil.getService(UpdateUserActivity.this);
        Call<Customer> customerCall = service.updateTTUser(customer);
        customerCall.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                Customer custemp = response.body();
                if (custemp != null) {
                    Toast.makeText(UpdateUserActivity.this, "Cập nhật thông tin thành công !", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("hoten", hoten);
                    editor.putString("email", emailreq);
                    editor.putString("sdt", phonereq);
                    editor.putString("ngaysinh", finalNgaysinh);
                    editor.apply();
                    finish();
                } else {
                    Toast.makeText(UpdateUserActivity.this, "Cập nhật thông tin thất bại !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {

            }
        });

    }

    private void addControls() {
        edtphone = findViewById(R.id.edtphone);
        edtname = findViewById(R.id.edtname);
        edtemail = findViewById(R.id.edtemail);
        edtdate = findViewById(R.id.edtdate);
        btnngay = findViewById(R.id.btnngay);
        btncapnhat = findViewById(R.id.btncapnhat);
        btnhuy = findViewById(R.id.btnhuy);
        sharedPreferences = getSharedPreferences("datalogin", Context.MODE_PRIVATE);
    }

}