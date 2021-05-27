package com.ngolamquangtin.appdatvexemphim.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.ngolamquangtin.appdatvexemphim.Activity.LoginActivity;
import com.ngolamquangtin.appdatvexemphim.Activity.PasswordChangeActivity;
import com.ngolamquangtin.appdatvexemphim.Activity.UpdateUserActivity;
import com.ngolamquangtin.appdatvexemphim.R;

public class FragmentProfile extends Fragment {

    ConstraintLayout contrainUpdateuser, contrainUpdatePass;
    TextView txtTennguoidung, txtdangxuatdangnhap;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        addControls(view);
        addEvents();
        return view;
    }

    private void addEvents() {
        contrainUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PasswordChangeActivity.class);
                startActivity(i);
            }
        });

        contrainUpdateuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.getString("hoten", "").equals("") == false) {
                    String hoten = sharedPreferences.getString("hoten", "");
                    String email = sharedPreferences.getString("email", "");
                    String sdt = sharedPreferences.getString("sdt", "");
                    String ngaysinh = sharedPreferences.getString("ngaysinh", "");
                    Intent i = new Intent(getActivity(), UpdateUserActivity.class);
                    i.putExtra("HOTEN", hoten);
                    i.putExtra("EMAIL", email);
                    i.putExtra("SDT", sdt);
                    i.putExtra("NGAYSINH", ngaysinh);
                    startActivity(i);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage("Bạn chưa đăng nhập");
                    builder.show();
                }

            }
        });

        txtdangxuatdangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                if (textView.getText().toString().equals("Đăng nhập")) {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                } else {
                    sharedPreferences.edit().clear().commit();
                    txtTennguoidung.setText("Chưa đăng nhập");
                    txtdangxuatdangnhap.setText("Đăng nhập");
                    Toast.makeText(getActivity(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void loadUser() {
        String hoten = sharedPreferences.getString("hoten", "Chưa đăng nhập");
        if (hoten.equals("Chưa đăng nhập")) {
            txtdangxuatdangnhap.setText("Đăng nhập");
        } else {
            txtdangxuatdangnhap.setText("Đăng xuất");
            txtTennguoidung.setText(hoten);
        }
    }

    private void addControls(View view) {
        txtTennguoidung = view.findViewById(R.id.txtTennguoidung);
        txtdangxuatdangnhap = view.findViewById(R.id.txtdangxuatdangnhap);
        sharedPreferences = getActivity().getSharedPreferences("datalogin", Context.MODE_PRIVATE);
        contrainUpdateuser = view.findViewById(R.id.contrainupdate);
        contrainUpdatePass = view.findViewById(R.id.contrainupdatepdatepass);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUser();
    }
}
