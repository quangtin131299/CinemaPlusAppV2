package com.ngolamquangtin.appdatvexemphim.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.astritveliu.boom.Boom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthCredential;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.CustomerV2;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    FirebaseAuth auth;
    Button btnCancel,btnAuthen;
    Dialog dialog;
    EditText edtauthen;

    String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpactivity);

        addControls();

        addEvents();

        sendOTPToPhone();
    }

    private void sendOTPToPhone() {

        CustomerV2 customer = getCustomer();

        String phoneNumber = fomatNumberPhone(customer.getSdt());

        auth.setLanguageCode("vi");

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(OTPActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                                return;
                            }

                            @Override
                            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                                Log.d("/////", e.getMessage());
                            }

                            @Override
                            public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);

                                verificationId = s;
                            }
                        })
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private String fomatNumberPhone(String numberPhone){
        String countryCode = "+84";
        String outputNumberPhone = numberPhone.replaceFirst("0", countryCode);

        return outputNumberPhone;
    }

    private boolean CredentialUser(String verificationId, String code){
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    CustomerV2 customer = getCustomer();

                    //Update equals 0
                    if(getState() == 0){
                        updateInforUser(customer);
                    }else{
                        xulyCreateAccount(customer);
                    }

                }else{
                    showDialogFails();
                }
            }
        });

        return false;
    }

    private void addControls() {
        auth = FirebaseAuth.getInstance();
        edtauthen = findViewById(R.id.edtauth);
        btnAuthen = findViewById(R.id.btnauthen);
        btnCancel = findViewById(R.id.btncancel);
        sharedPreferences = getSharedPreferences("datalogin", Context.MODE_PRIVATE);
        dialog = new Dialog(OTPActivity.this);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void addEvents() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnAuthen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogProcessing();
                String code = edtauthen.getText().toString();
                CredentialUser(verificationId, code);
            }
        });

        new Boom(btnCancel);
        new Boom(btnAuthen);
    }

    private void xulyCreateAccount(CustomerV2 customer) {
        Service service = RetrofitUtil.getService(OTPActivity.this);
        Call<CustomerV2> customerCall = service.Register(customer);
        customerCall.enqueue(new Callback<CustomerV2>() {
            @Override
            public void onResponse(Call<CustomerV2> call, retrofit2.Response<CustomerV2> response) {
                CustomerV2 temp = response.body();
                if (temp != null) {
                    showDialogSuccess();
                    Button button = dialog.findViewById(R.id.btnOK);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(OTPActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });
                } else {
                    showDialogFails();
                }
            }

            @Override
            public void onFailure(Call<CustomerV2> call, Throwable t) {

            }
        });
    }

    private CustomerV2 getCustomer(){
        Intent i = getIntent();
        CustomerV2 customer = (CustomerV2) i.getSerializableExtra("CUSTOMER");

        if(customer == null){
            return null;
        }

        return customer;
    }

    private void showDialogProcessing(){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }

        dialog.setContentView(R.layout.dialog_processing);
        dialog.show();
    }

    public void dismissDialogProcessing(){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    private void showDialogSuccess(){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }

        dialog.setContentView(R.layout.dialog_sucess);
        dialog.show();
    }

    private void showDialogFails(){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }

        dialog.setContentView(R.layout.dialog_failed);
        dialog.show();
    }

    public void updateInforUser(CustomerV2 customer){
        Service service = RetrofitUtil.getService(OTPActivity.this);
        Call<CustomerV2> customerCall = service.updateTTUser(customer);
        customerCall.enqueue(new Callback<CustomerV2>() {
            @Override
            public void onResponse(Call<CustomerV2> call, Response<CustomerV2> response) {
                dismissDialogProcessing();
                CustomerV2 newCustomer = response.body();

                if (newCustomer != null) {
                    showDialogSuccess();
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

    public int getState(){
        Intent intent = getIntent();

        if(intent.hasExtra("STATE")){
            return  intent.getIntExtra("STATE", 0);
        }

        return 1;
    }

    public void updateSharePreference(CustomerV2 newCustomer) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("hoten", newCustomer.getHoTen());
        editor.putString("sdt", newCustomer.getSdt());
        editor.putString("ngaysinh", newCustomer.getNgaySinh());
        editor.apply();
        finish();
    }

//    public void showDialog(int layout){
//
//        if(dialog != null && dialog.isShowing()){
//            dialog.dismiss();
//        }
//
//        dialog.setContentView(layout);
//        dialog.show();
//    }
}