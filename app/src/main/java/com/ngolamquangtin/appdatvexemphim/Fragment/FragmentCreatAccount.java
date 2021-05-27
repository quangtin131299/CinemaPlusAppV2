package com.ngolamquangtin.appdatvexemphim.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.ngolamquangtin.appdatvexemphim.Activity.LoginActivity;
import com.ngolamquangtin.appdatvexemphim.Activity.OTPActivity;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.Customer;
import com.ngolamquangtin.appdatvexemphim.DTO.CustomerV2;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCreatAccount extends Fragment {

    final int VALIDATE_SUCCESS = 1;
    final int VALIDATE_EMAIL_AND_PHONE = -2;
    final int VALIDATE_EMAIL_FAIL = -1;
    final int VALIDATE_PHONE_FAIL = 0;
    final String REGEX_PHONE = "^\\d{10}$";
    final String REGEX_EMAIL = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    CustomerV2 customer;
    Pattern patternEmail, patternPhone;
    TextInputLayout edlayoutname, edlayouttk, edlayoutpass, edlayoutconfirmpass, edlayoutemail, edlayoutbirthday, edlayoutphone;
    EditText txtname, txtaccount, txtpass, txtconfimpass, txtemail, txtdate, txtphone;
    Button btnCreate, btnHuy;
    Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_createaccount, container, false);
        addControls(view);
        addEvents();
        return view;
    }

    private void addEvents() {
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        txtname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    edlayoutname.setError("Không được bỏ trống");
                } else {
                    edlayoutname.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtaccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    edlayouttk.setError("Khong được để trống");
                } else {
                    edlayouttk.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    edlayoutpass.setError("Khong được bỏ trống !");
                } else {
                    edlayoutpass.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtconfimpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    edlayoutconfirmpass.setError("Không được để trống");
                } else {
                    edlayoutconfirmpass.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    edlayoutemail.setError("Không được để trống !");
                } else {
                    edlayoutemail.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (patternEmail.matcher(s.toString()).matches()) {
                    edlayoutemail.setError(null);
                } else {
                    edlayoutemail.setError("Email không hợp lệ");
                }
            }
        });

        txtdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    edlayoutbirthday.setError("Không được để trông !");
                } else {
                    edlayoutbirthday.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    edlayoutphone.setError("Không được để trống !");
                } else {
                    edlayoutphone.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(patternPhone.matcher(s.toString()).matches()){
                    edlayoutphone.setError(null);
                }else{
                    edlayoutphone.setError("Số điện thoại không hợp lệ");
                }
            }
        });

        txtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar temp = calendar;
                        temp.set(year, month, dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        txtdate.setText(simpleDateFormat.format(temp.getTime()));

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(R.layout.dialog_processing);

                String name = txtname.getText().toString().trim();
                String account = txtaccount.getText().toString().trim();
                String pass = txtpass.getText().toString().trim();
                String confimpass = txtconfimpass.getText().toString().trim();
                String email = txtemail.getText().toString().trim();
                String date = txtdate.getText().toString().trim();
                String phone = txtphone.getText().toString().trim();

                SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date1 = simpleDateFormat.parse(date);
                    date = output.format(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (name.equals("") && account.equals("") && pass.equals("") && confimpass.equals("") && email.equals("") && date.equals("") && phone.equals("")) {
                    dialog.dismiss();
                    dialog.setContentView(R.layout.dialog_failed);
                    dialog.show();
                    Button btn = dialog.findViewById(R.id.btnOK);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Không được để trống thông tin", Toast.LENGTH_LONG).show();
                        }
                    });
                    return;
                }
                if (confimpass.equals(pass) == false) {
                    dialog.dismiss();
                    dialog.setContentView(R.layout.dialog_failed);
                    dialog.show();
                    Button btn = dialog.findViewById(R.id.btnOK);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(), "Xác nhận pass ko với pass không giống nhau", Toast.LENGTH_LONG).show();
                        }
                    });
                    return;
                }

               customer = new CustomerV2();

                customer.setHoTen(name);
                customer.setEmail(email);
                customer.setSdt(phone);
                customer.setNgaySinh(date);
                customer.setTaiKhoan(account);
                customer.setMatKhau(Util.getMd5(pass));

                validateEmailAndPhone(customer.getEmail(),customer.getSdt());

            }
        });
    }

    private void addControls(View view) {
        btnHuy = view.findViewById(R.id.btnhuy);
        edlayoutphone = view.findViewById(R.id.edlayoutphone);
        edlayoutbirthday = view.findViewById(R.id.edlayoutbirthday);
        edlayoutemail = view.findViewById(R.id.edlayoutemail);
        edlayoutconfirmpass = view.findViewById(R.id.edlayoutconfirmpass);
        edlayoutpass = view.findViewById(R.id.edlayoutpass);
        edlayouttk = view.findViewById(R.id.edlayouttk);
        edlayoutname = view.findViewById(R.id.edlayoutname);
        txtname = view.findViewById(R.id.txtname);
        txtaccount = view.findViewById(R.id.txtaccount);
        txtpass = view.findViewById(R.id.txtpass);
        txtconfimpass = view.findViewById(R.id.txtconfimpass);
        txtemail = view.findViewById(R.id.txtemail);
        txtdate = view.findViewById(R.id.txtdate);
        txtphone = view.findViewById(R.id.txtsdt);
        btnCreate = view.findViewById(R.id.btncreate);
        patternPhone = Pattern.compile(REGEX_PHONE, Pattern.CASE_INSENSITIVE);
        patternEmail = Pattern.compile(REGEX_EMAIL, Pattern.CASE_INSENSITIVE);
    }

    public void validateEmailAndPhone(String email, String numberPhone){
        final int[] result = {1};
        Service service = RetrofitUtil.getService(getActivity());
        Call<Integer> call = service.validateEmailAndNumberPhone(email, numberPhone);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                result[0] = response.body();

                if(result[0] ==  VALIDATE_SUCCESS){
                    Intent i = new Intent(getActivity(), OTPActivity.class);
                    i.putExtra("CUSTOMER", customer);
                    startActivity(i);
                }

                showErroOnEditText(result[0]);
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    public void showDialog(int layout){
        if(dialog == null){
            dialog = new Dialog(getActivity());
        }

        if(dialog.isShowing()){
            dissmissDialog();
        }

        dialog.setContentView(layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    public void dissmissDialog(){
        if(dialog != null){
            dialog.dismiss();
        }
    }

    public void showErroOnEditText(int erroCode){
        switch (erroCode){
            case VALIDATE_EMAIL_FAIL:
                edlayoutemail.setError("Email da duoc su dung ");
                showDialog(R.layout.dialog_failed);
                break;
            case VALIDATE_PHONE_FAIL:
                edlayoutphone.setError("So dien thoai da duoc su dung");
                showDialog(R.layout.dialog_failed);
                break;
            case VALIDATE_EMAIL_AND_PHONE:
                edlayoutemail.setError("Email da duoc su dung ");
                edlayoutphone.setError("So dien thoai da duoc su dung");
                showDialog(R.layout.dialog_failed);
                break;
            default:
                return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        dissmissDialog();
    }


}
