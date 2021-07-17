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

import com.astritveliu.boom.Boom;
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


        new Boom(btnhuy);
        new Boom(btnupdate);
    }

    private void processPassChange( String newpass, String idkhachhang) {

    }

    private void addControls() {
        sharedPreferences = getSharedPreferences("datalogin", Context.MODE_PRIVATE);
        btnupdate = findViewById(R.id.btnupdate);
        btnhuy = findViewById(R.id.btnhuy);
        edtnewpass = findViewById(R.id.edtnewpass);
        edtxacnhan = findViewById(R.id.txtmatkhau);
    }


}