package com.ngolamquangtin.appdatvexemphim.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.astritveliu.boom.Boom;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ngolamquangtin.appdatvexemphim.Activity.HistoryActivity;
import com.ngolamquangtin.appdatvexemphim.Activity.HomeActivity;
import com.ngolamquangtin.appdatvexemphim.Activity.LoginActivity;
import com.ngolamquangtin.appdatvexemphim.Activity.PasswordChangeActivity;
import com.ngolamquangtin.appdatvexemphim.Activity.UpdateUserActivity;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentProfile extends Fragment {

    CircleImageView imgProfile;
    ImageView imgChangeImagProfile, imglocation;
    ConstraintLayout contrainUpdateuser, contrainUpdatePass, contrainHistory, contrainchangelanguae;
    TextView txtTennguoidung, txtdangxuatdangnhap, txtCurrentLanguae;
    SharedPreferences sharedPreferences;
    FirebaseStorage storage;
    StorageReference storageReference;
    Dialog dialogSuccess, dialogProcess, dialogError;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        addControls(view);

        updateUILanguae();

        addEvents();

        return view;
    }

    public void setCurrentCodeLanguae(String lang){
//        String currentCodeLanguae = sharedPreferences.getString("currentLanguae", "");
        SharedPreferences.Editor editor = sharedPreferences.edit();

//        if(currentCodeLanguae.equals("")){
//            editor.putString("currentLanguae", Locale.getDefault().toString());
//        }else{
            editor.putString("currentLanguae", lang);
//        }

        editor.apply();
    }

    public String getCurrentCodeLanguae(){
        return sharedPreferences.getString("currentLanguae", "");
    }

    public void updateUILanguae() {
        String currentCodeLanguae = getCurrentCodeLanguae();

        if(!currentCodeLanguae.isEmpty()){
            if(getCurrentCodeLanguae().equals("en")){
                imglocation.setImageResource(R.drawable.ic_america);
                txtCurrentLanguae.setText(getResources().getString(R.string.languageAmerica));
            }else{
                imglocation.setImageResource(R.drawable.ic_vietnam);
                txtCurrentLanguae.setText(getResources().getString(R.string.languageVN));
            }
        }
    }

    private void addEvents() {
        contrainHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sharedPreferences.getString("hoten", "").equals("") == false){
                    Intent intentScreenHistory = new Intent(getActivity(), HistoryActivity.class);

                    startActivity(intentScreenHistory);
                }else{
                    showDialogLogin(getResources().getString(R.string.nologged));
                }

            }
        });

        contrainUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPreferences.getString("hoten", "").equals("") == false){
                    Intent i = new Intent(getActivity(), PasswordChangeActivity.class);

                    startActivity(i);
                }else{
                    showDialogLogin(getResources().getString(R.string.noLogin));
                }

            }
        });

        contrainUpdateuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.getString("hoten", "").equals("") == false) {
                    String hoten = sharedPreferences.getString("hoten", "");
                    String sdt = sharedPreferences.getString("sdt", "");
                    String ngaysinh = sharedPreferences.getString("ngaysinh", "");
                    Intent intentToScreenUpdate = new Intent(getActivity(), UpdateUserActivity.class);

                    intentToScreenUpdate.putExtra("HOTEN", hoten);
                    intentToScreenUpdate.putExtra("SDT", sdt);
                    intentToScreenUpdate.putExtra("NGAYSINH", ngaysinh);

                    startActivity(intentToScreenUpdate);
                } else {
                    showDialogLogin(getResources().getString(R.string.noLogin));
                }

            }
        });

        contrainchangelanguae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getCurrentCodeLanguae().equals("vi")){
                    imglocation.setImageResource(R.drawable.ic_america);
                    txtCurrentLanguae.setText(getResources().getString(R.string.languageAmerica));
                    setCurrentCodeLanguae("en");

                    Util.changeLocale(getActivity(),"en");
                }else{
                    imglocation.setImageResource(R.drawable.ic_vietnam);
                    txtCurrentLanguae.setText(getResources().getString(R.string.languageVN));
                    setCurrentCodeLanguae("vi");

                    Util.changeLocale(getActivity(),"vi");
                }

//                Intent intentReset = new Intent(getActivity(), HomeActivity.class);
//
//                startActivity(intentReset);
//
//                getActivity().finish();
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
        });

        txtdangxuatdangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                if (textView.getText().toString().equals(getResources().getString(R.string.login))) {
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivity(i);
                } else {
                    sharedPreferences.edit().clear().commit();
                    txtTennguoidung.setText("Chưa đăng nhập");
                    txtdangxuatdangnhap.setText(getResources().getString(R.string.login));
                    imgProfile.setImageResource(R.drawable.ic_hinhdaidien);

                    Util.ShowToastInforMessage(getActivity(), getResources().getString(R.string.youLogout));
                }
            }
        });

        imgChangeImagProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int status = checkLogin();

                if(status == 1) {
                    BottomSheetDialog btnChooseImage = new BottomSheetDialog(getActivity(), R.style.dialogQR);
                    btnChooseImage.setContentView(LayoutInflater.from(getActivity()).inflate(R.layout.dialogbottom_upload_image, null));

                    ConstraintLayout contrainUploadImageCamera = btnChooseImage.findViewById(R.id.contraincamera);
                    ConstraintLayout contrainUploadImageFile = btnChooseImage.findViewById(R.id.contrainfile);

                    contrainUploadImageCamera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

                    contrainUploadImageFile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            chooseImageFile();
                            btnChooseImage.dismiss();
                        }
                    });

                    new Boom(contrainUploadImageCamera);
                    new Boom(contrainUploadImageFile);
                    btnChooseImage.show();
                }else {
                    Util.ShowToastErrorMessage(getActivity(), "Bạn chưa đăng nhập");
                }

            }
        });

        new Boom(contrainchangelanguae);
        new Boom(imgChangeImagProfile);
        new Boom(txtdangxuatdangnhap);
        new Boom(contrainUpdateuser);
        new Boom(contrainHistory);
        new Boom(contrainUpdatePass);
    }



    private void loadUser() {
        String hoten = sharedPreferences.getString("hoten", getResources().getString(R.string.nologged));
        String imagProfilUri = sharedPreferences.getString("imagProfile", "");

        if (hoten.equals(getResources().getString(R.string.nologged))) {
            txtdangxuatdangnhap.setText(getResources().getString(R.string.login));
        } else {
            txtdangxuatdangnhap.setText(getResources().getString(R.string.logout));
            txtTennguoidung.setText(hoten);
        }

        if(!imagProfilUri.isEmpty()){
            Glide.with(getActivity()).asBitmap().load(imagProfilUri).into(imgProfile);
        }
    }

    private void addControls(View view) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        contrainchangelanguae = view.findViewById(R.id.contrainchangelanguae);
        txtTennguoidung = view.findViewById(R.id.txtTennguoidung);
        txtdangxuatdangnhap = view.findViewById(R.id.txtdangxuatdangnhap);
        sharedPreferences = getActivity().getSharedPreferences("datalogin", Context.MODE_PRIVATE);
        contrainUpdateuser = view.findViewById(R.id.contrainupdate);
        contrainUpdatePass = view.findViewById(R.id.contrainupdatepass);
        contrainHistory = view.findViewById(R.id.contrainhistory);
        imgChangeImagProfile = view.findViewById(R.id.imgchangeimg);
        imglocation = view.findViewById(R.id.imglocation);
        txtCurrentLanguae = view.findViewById(R.id.txtlanguae);
        imgProfile = view.findViewById(R.id.idimgapersonal);
        dialogSuccess = new Dialog(getActivity());
        dialogProcess = new Dialog(getActivity());
        dialogError = new Dialog(getActivity());
    }

    public void chooseImageFile(){
        Intent intentToStore = new Intent(Intent.ACTION_GET_CONTENT);

        intentToStore.setType("image/*");

        getActivity().startActivityFromFragment(FragmentProfile.this,intentToStore, 1);
    }

    public void updateUIImageProfile(Uri imagUri){
        if(imagUri != null){
            imgProfile.setImageURI(imagUri);
        }
    }

    public void uploadImageProfile(Uri imagUri){
        String idCustomer = sharedPreferences.getString("id", "");

        if(!idCustomer.equals("")){
            StorageReference riverRef = storageReference.child("profiles/" + idCustomer);
            riverRef.putFile(imagUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    riverRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                             dismissDialogProcess();

                             SharedPreferences.Editor editor =  sharedPreferences.edit();

                             editor.putString("imagProfile", uri.toString());

                             editor.apply();

                             loadUser();

                             int idCustomer = Integer.parseInt(sharedPreferences.getString("id", ""));

                             updateImageProfile(idCustomer, uri.toString());
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                    showDialogProcess();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }

    }

    public int checkLogin(){
        String trangthai = sharedPreferences.getString("trangthai", "");
        if (trangthai.equals("") == false) {
            if (trangthai.equals("1")) {
                return 1;
            } else {
                return 0;
            }
        }
        return 0;
    }

    public void updateImageProfile(int idCustomer, String imgProfileUrl){
        Service service = RetrofitUtil.getService(getActivity());
        Call<Integer> call = service.updateImageProfile(idCustomer, imgProfileUrl);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.body() != null && response.body() == 1){
                    showDialogSuccess("Cập nhật thành công");
                }else{
                    showDialogError("Cập nhật thất bại");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

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

    public void dismissDialogProcess(){
        if(dialogProcess != null && dialogProcess.isShowing()){
            dialogProcess.dismiss();
        }
    }

    public void showDialogSuccess(String mess){
        if(dialogSuccess != null){
            dialogSuccess.setContentView(R.layout.dialog_sucess);
            dialogSuccess.getWindow().setBackgroundDrawableResource(R.color.transparent);

            TextView txtMess = dialogSuccess.findViewById(R.id.txtmess);
            Button btnOk = dialogSuccess.findViewById(R.id.btnOK);

            txtMess.setText(mess);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogSuccess.dismiss();
                }
            });

            new Boom(btnOk);

            dialogSuccess.show();
        }
    }

    public void showDialogError(String mess){
        if(dialogError != null){
            dialogError.setContentView(R.layout.dialog_failed);

            TextView txtMess = dialogError.findViewById(R.id.txtmess);
            Button btnOk = dialogError.findViewById(R.id.btnOK);

            txtMess.setText(mess);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogError.dismiss();
                }
            });

            new Boom(btnOk);

            dialogError.show();
        }
    }

    public void showDialogLogin(String mess) {
        Dialog dialog = new Dialog(getActivity());
        View view = getLayoutInflater().inflate(R.layout.dialog_chitietphim, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        TextView txt = dialog.findViewById(R.id.txtmess);
        Button btnlogin = dialog.findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(getActivity(), LoginActivity.class);

                intentLogin.putExtra("SCREEN_HOME", 1);

                startActivity(intentLogin);
            }
        });

        new Boom(btnlogin);

        txt.setText(mess);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public void onResume() {
        super.onResume();

        loadUser();
    }


}
