package com.ngolamquangtin.appdatvexemphim.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.ngolamquangtin.appdatvexemphim.Activity.CreateAccountActivity;
import com.ngolamquangtin.appdatvexemphim.Activity.HomeActivity;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.Customer;
import com.ngolamquangtin.appdatvexemphim.DTO.CustomerV2;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentLoginInfo extends Fragment {

    TextInputLayout editlayouttk, editlayoutmk;
    TextView txttendangnhap, txtmakhau, txttaotk;
    Button btnlogin, btnhuy;

    SharedPreferences sharedPreferences;
    Dialog dialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logininfo, container, false);
        addControls(view);
        addEvents();
        return view;
    }

    private void addEvents() {
        txttendangnhap.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    editlayouttk.setError("Bắt buộc phải nhập");
                } else {
                    editlayouttk.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txtmakhau.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    editlayoutmk.setError("Bắt buộc phải nhập pass");
                } else {
                    editlayoutmk.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_processing);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
                xulyLogin();
            }
        });
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

    }

    private void xulyLogin() {
        final String tendangnhap = txttendangnhap.getText().toString().trim();
        String password = txtmakhau.getText().toString().trim();
        final String md5pass = Util.getMd5(password);

        Service service = RetrofitUtil.getService(getActivity());
        Call<CustomerV2> listCall = service.Login(tendangnhap, md5pass);
        listCall.enqueue(new Callback<CustomerV2>() {
            @Override
            public void onResponse(Call<CustomerV2> call, Response<CustomerV2> response) {
                CustomerV2 customer = response.body();
                if (customer != null) {
                    dialog.dismiss();
                    saveDataSharePreferences(customer);
                    Intent i = new Intent(getActivity(), HomeActivity.class);
                    startActivity(i);
                } else {
                    dialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Tên đăng nhập hoặc tài khoản không chính xác !");
                    builder.setTitle("Thông báo !");
                    builder.show();
                }
            }

            @Override
            public void onFailure(Call<CustomerV2> call, Throwable t) {
                call.clone().enqueue(this);
            }
        });

    }

    private void addControls(View view) {
        editlayouttk = view.findViewById(R.id.edlayouttk);
        editlayoutmk = view.findViewById(R.id.edlayoupass);
        txttendangnhap = view.findViewById(R.id.txttendangnhap);
        txtmakhau = view.findViewById(R.id.txtmatkhau);
        txttaotk = view.findViewById(R.id.txttaotk);
        btnlogin = view.findViewById(R.id.btnlogin);
        txttaotk = view.findViewById(R.id.txttaotk);
        txttaotk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CreateAccountActivity.class);
                startActivity(i);
            }
        });
        btnhuy = view.findViewById(R.id.btnhuy);

        sharedPreferences = getActivity().getSharedPreferences("datalogin", Context.MODE_PRIVATE);

    }
    
    private void saveDataSharePreferences(CustomerV2 customer){
        Editor editor = sharedPreferences.edit();
        editor.putString("id", String.valueOf(customer.getId()));
        editor.putString("hoten", customer.getHoTen());
        editor.putString("email", customer.getEmail());
        editor.putString("sdt", customer.getSdt());
        editor.putString("ngaysinh", customer.getNgaySinh());
        editor.putString("taikhoan", customer.getTaiKhoan());
        editor.putString("pass", customer.getMatKhau());
        editor.putString("trangthai", "1");
        editor.apply();
    }


}
