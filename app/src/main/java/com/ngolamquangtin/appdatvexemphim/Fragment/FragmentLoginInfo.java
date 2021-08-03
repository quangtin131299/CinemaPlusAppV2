package com.ngolamquangtin.appdatvexemphim.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.astritveliu.boom.Boom;
import com.google.android.material.textfield.TextInputLayout;
import com.ngolamquangtin.appdatvexemphim.Activity.CreateAccountActivity;
import com.ngolamquangtin.appdatvexemphim.Activity.DetalsMovieActivity;
import com.ngolamquangtin.appdatvexemphim.Activity.HomeActivity;
import com.ngolamquangtin.appdatvexemphim.Activity.ScheduleActivity;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
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
    Dialog dialogProcess, dialogErrorMessage;


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
                    editlayouttk.setError(getResources().getString(R.string.errorEmptyAccount));
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
                    editlayoutmk.setError(getResources().getString(R.string.errorPassEmpty));
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
                showDialogProcess();
                xulyLogin();
            }
        });
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        txttaotk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CreateAccountActivity.class);
                startActivity(i);
            }
        });

        new Boom(txttaotk);
        new Boom(btnhuy);
        new Boom(btnlogin);
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
                if (customer != null && customer.getId() != 0) {
                    dismissDialogProcess();
                    saveDataSharePreferences(customer);
                    redirectToScreen();
                } else {
                    dismissDialogProcess();
                    showDialogMessageError("Sai tên tài khoản hoặc mật khẩu !");
                }
            }

            @Override
            public void onFailure(Call<CustomerV2> call, Throwable t) {
                dismissDialogProcess();
                call.clone().enqueue(this);
            }
        });
    }

    public void redirectToScreen(){
        Intent intentRedirect = getActivity().getIntent();

        if(intentRedirect.hasExtra("SCREEN_SCHEDULE")){
            Intent intentToScreenSchedule = new Intent(getActivity(), ScheduleActivity.class);

            if(intentRedirect.hasExtra("CINEMA")){
                intentToScreenSchedule.putExtra("CINEMA", intentRedirect.getSerializableExtra("CINEMA"));
                startActivity(intentToScreenSchedule);
            }
        }else if(intentRedirect.hasExtra("SCREEN_MOVIE_DETAIL")){
            Intent intentToScreenDetailMovie = new Intent(getActivity(), DetalsMovieActivity.class);

            if(intentRedirect.hasExtra("MOVIE")){
                intentToScreenDetailMovie.putExtra("MOVIE", intentRedirect.getSerializableExtra("MOVIE"));
                startActivity(intentToScreenDetailMovie);
            }

        }else{
            Intent intentToHome = new Intent(getActivity(), HomeActivity.class);
            startActivity(intentToHome);
        }
    }

    private void addControls(View view) {
        editlayouttk = view.findViewById(R.id.edlayouttk);
        editlayoutmk = view.findViewById(R.id.edlayoupass);
        txttendangnhap = view.findViewById(R.id.txttendangnhap);
        txtmakhau = view.findViewById(R.id.txtmatkhau);
        txttaotk = view.findViewById(R.id.txttaotk);
        btnlogin = view.findViewById(R.id.btnlogin);
        txttaotk = view.findViewById(R.id.txttaotk);
        dialogProcess = new Dialog(getActivity());
        dialogErrorMessage = new Dialog(getActivity());

        btnhuy = view.findViewById(R.id.btnhuy);

        sharedPreferences = getActivity().getSharedPreferences("datalogin", Context.MODE_PRIVATE);

    }

    private void saveDataSharePreferences(CustomerV2 customer){
        Editor editor = sharedPreferences.edit();
        editor.putString("id", String.valueOf(customer.getId()));
        editor.putString("hoten", customer.getHoTen());
        editor.putString("email", customer.getEmail());
        editor.putString("sdt", customer.getSdt());
        editor.putString("imagProfile", customer.getAnhDaiDien());

        if(customer.getNgaySinh() != null && !customer.getNgaySinh().isEmpty()){
            editor.putString("ngaysinh",  Util.formatDateServerToClient(customer.getNgaySinh()));
        }

        editor.putString("taikhoan", customer.getTaiKhoan());
        editor.putString("pass", customer.getMatKhau());
        editor.putString("trangthai", "1");
        editor.apply();
    }

    private void showDialogProcess(){
        if(dialogProcess != null){
            dialogProcess.setContentView(R.layout.dialog_processing);
            dialogProcess.setCanceledOnTouchOutside(false);
            dialogProcess.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialogProcess.show();
        }
    }

    private void dismissDialogProcess(){
        if(dialogProcess != null && dialogProcess.isShowing()){
            dialogProcess.dismiss();
        }
    }

    private void showDialogMessageError(String messError){
        if(dialogErrorMessage != null){
            dialogErrorMessage.setContentView(R.layout.dialog_failed);
            dialogErrorMessage.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            TextView txtMessError =  dialogErrorMessage.findViewById(R.id.txtmess);
            Button btnOK = dialogErrorMessage.findViewById(R.id.btnOK);

            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogErrorMessage.dismiss();
                }
            });
            txtMessError.setText(messError);

            new Boom(btnOK);
            dialogErrorMessage.show();
        }
    }

    private void dismissDialogMessageError(){
        if(dialogErrorMessage != null && dialogErrorMessage.isShowing()){
            dialogErrorMessage.dismiss();
        }
    }

}
