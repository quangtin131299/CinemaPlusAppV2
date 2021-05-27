package com.ngolamquangtin.appdatvexemphim.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PasswordChangeActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    EditText edtnewpass, edtxacnhan;
    Button btnhuy, btnupdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newpass = edtnewpass.getText().toString().trim();
                String passxacnhan = edtxacnhan.getText().toString().trim();
                if(newpass.equals(passxacnhan)){
                    String idkhachhang = sharedPreferences.getString("id", "");
                    newpass = Util.getMd5(newpass);
                    processPassChange(newpass, idkhachhang);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(PasswordChangeActivity.this);
                    builder.setTitle("Thông Báo !");
                    builder.setMessage("Mật khẩu không trùng khớp");
                    builder.show();
                }

            }
        });

    }

    private void processPassChange(final String newpass, final String idkhachhang) {
        RequestQueue requestQueue = Volley.newRequestQueue(PasswordChangeActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Util.LINK_UPDATEPASSWORDUSER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getString("status").equals("Thanh Cong!")){
                            Toast.makeText(PasswordChangeActivity.this, "Đổi pass thành công !", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(PasswordChangeActivity.this, "Đổi pass thất bại !", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("makhachhang",idkhachhang);
                map.put("newpass",newpass);
                return map;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    private void addControls() {
        sharedPreferences = getSharedPreferences("datalogin", Context.MODE_PRIVATE);
        btnupdate = findViewById(R.id.btnupdate);
        btnhuy = findViewById(R.id.btnhuy);
        edtnewpass = findViewById(R.id.edtnewpass);
        edtxacnhan = findViewById(R.id.txtmatkhau);
    }


}