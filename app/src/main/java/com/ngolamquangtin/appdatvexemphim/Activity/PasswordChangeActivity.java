package com.ngolamquangtin.appdatvexemphim.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.astritveliu.boom.Boom;
import com.google.android.material.textfield.TextInputLayout;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.MessageResponse;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordChangeActivity extends AppCompatActivity {

    TextInputLayout txtlayoutPassOld, txtLayoutNewPass,txtLayoutconfirmpass;
    SharedPreferences sharedPreferences;
    EditText edtnewpass, edtxacnhan, edtOldPass;
    Button btnhuy, btnupdate;
    Dialog dialogProcess, dialogError, dialogSucess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        addControls();

        addEvents();
    }

    private void addEvents() {
        edtnewpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    txtLayoutNewPass.setError("Mật khẩu mới không được để trống");
                }else{
                    txtLayoutNewPass.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtOldPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    txtlayoutPassOld.setError("Mật khẩu củ không được bỏ trống");
                }else{
                    txtlayoutPassOld.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtxacnhan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")){
                    txtLayoutconfirmpass.setError("Mật khẩu xác nhận không được bỏ trống");
                }else{
                    txtLayoutconfirmpass.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String passConfirm = edtxacnhan.getText().toString();
                String newPass = edtnewpass.getText().toString();

                if(passConfirm.equals(newPass)){
                    txtLayoutconfirmpass.setError(null);
                }else{
                    txtLayoutconfirmpass.setError("Mật khẩu xác nhận không khớp");
                }
            }
        });

        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = Util.getMd5(edtOldPass.getText().toString());
                String passLogin = sharedPreferences.getString("pass", "");
                String newpass = edtnewpass.getText().toString().trim();
                String passxacnhan = edtxacnhan.getText().toString().trim();

                if(txtLayoutconfirmpass.getError() != null && txtLayoutNewPass.getError() != null && txtlayoutPassOld.getError() != null){
                    if(oldPass.equals(passLogin)){
                        if(newpass.equals(passxacnhan)){
                            String idCustomer = sharedPreferences.getString("id", "");
                            newpass = Util.getMd5(newpass);
                            processPassChange(newpass, idCustomer);
                        }else{
                            Util.ShowToastErrorMessage(PasswordChangeActivity.this, "Mật khẩu không trùng khớp");
                        }
                    }else{
                        showDialogError();
                    }
                }else{
                    showDialogError();
                }
            }
        });

        new Boom(btnhuy);
        new Boom(btnupdate);
    }

    private void processPassChange( String newPass, String idCustomer) {
        showDialogProcess();

        Service service = RetrofitUtil.getService(PasswordChangeActivity.this);
        Call<MessageResponse> call = service.updatePassWord(Integer.valueOf(idCustomer), newPass);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                dismissDialogProcess();

                MessageResponse msg = response.body();

                if(msg != null && msg.getStatusCode() == 1){
                    showDialogSuccess();
                }else{
                    showDialogError();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                dismissDialogProcess();
            }
        });
    }

    public void showDialogProcess(){
        if(dialogProcess != null){
            dialogProcess.setContentView(R.layout.dialog_processing);
            dialogProcess.getWindow().setBackgroundDrawableResource(R.color.transparent);

            dialogProcess.show();
        }

    }

    public void showDialogError(){
        if(dialogError != null){
            dialogError.setContentView(R.layout.dialog_failed);
            dialogError.getWindow().setBackgroundDrawableResource(R.color.transparent);

            Button btnOK = dialogError.findViewById(R.id.btnOK);

            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogError.dismiss();
                }
            });

            new Boom(btnOK);

            dialogError.show();
        }
    }

    public void showDialogSuccess(){
        if(dialogSucess != null){
            dialogSucess.setContentView(R.layout.dialog_sucess);
            dialogSucess.getWindow().setBackgroundDrawableResource(R.color.transparent);

            Button btnOk = dialogSucess.findViewById(R.id.btnOK);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogSucess.dismiss();
                    finish();
                }
            });

            new Boom(btnOk);

            dialogSucess.show();
        }
    }

    public void dismissDialogProcess(){
        if(dialogProcess != null && dialogProcess.isShowing()){
            dialogProcess.dismiss();
        }
    }

    private void addControls() {
        txtlayoutPassOld = findViewById(R.id.txtlayoutpassold);
        txtLayoutNewPass = findViewById(R.id.txtLayoutnewpass);
        txtLayoutconfirmpass = findViewById(R.id.txtLayoutconfirmpass);
        sharedPreferences = getSharedPreferences("datalogin", Context.MODE_PRIVATE);
        btnupdate = findViewById(R.id.btnupdate);
        btnhuy = findViewById(R.id.btnhuy);
        edtOldPass = findViewById(R.id.edtoldpass);
        edtnewpass = findViewById(R.id.edtnewpass);
        edtxacnhan = findViewById(R.id.edtConfirmNewPassWord);
        dialogProcess = new Dialog(PasswordChangeActivity.this);
        dialogSucess = new Dialog(PasswordChangeActivity.this);
        dialogError = new Dialog(PasswordChangeActivity.this);
    }


}