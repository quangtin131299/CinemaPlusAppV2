package com.ngolamquangtin.appdatvexemphim.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetalsMovieActivity extends AppCompatActivity {

    ImageView imghinhphim;
    ImageButton imgShare;
    TextView txttenphim, txtmota, txtngaykhoichieu, txtthoigian, txttheloai;
    Button btnhuy, btndatve;
    SharedPreferences sharedPreferences;
    YouTubePlayerView youTubePlayerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detals_movie);
        addControls();
        getLifecycle().addObserver(youTubePlayerView);
        addEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDetailMovie();
    }

    private void loadDetailMovie() {
        Intent i = getIntent();
        Movie movie = (Movie) i.getSerializableExtra("MOVIE");
        if (movie != null) {

            txttenphim.setText(movie.getTenphim());
            txtmota.setText(movie.getMota());
            DateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            try {
                Date date = inputFormat.parse(movie.getNgayKhoiChieu());
                txtngaykhoichieu.setText(simpleDateFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            txtthoigian.setText(movie.getThoigian() + " phút");
            int sltheloai = movie.getLoaiphims().size();
            String resulttheloai = "";
            for (int index = 0; index < sltheloai; index++) {
                resulttheloai += movie.getLoaiphims().get(index).getTenLoai() + ", ";
            }
            txttheloai.setText(resulttheloai);
            if(movie.getAnhbia().equals("") == false) {
                Picasso.with(DetalsMovieActivity.this).load(movie.getAnhbia()).into(imghinhphim);
            }
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    String idmovie = movie.getTrailer();
                    youTubePlayer.loadVideo(idmovie, 0);
                }
            });

        }
    }

    private void addEvents() {
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btndatve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ktdangnhap() == 1) {
                    Intent i1 = getIntent();
                    Intent i = new Intent(DetalsMovieActivity.this, ChooseSessionActivity.class);
                    i.putExtra("ID_MOVIE", i1.getIntExtra("ID_MOVIE", 0));
                    i.putExtra("TEN_PHIM", txttenphim.getText());
                    finish();
                    startActivity(i);
                } else {
                    createDialog("Bạn chưa đăng nhập !");
                }
            }
        });

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("application/vnd.android.package-archive");

                share.putExtra(Intent.EXTRA_STREAM,
                        Uri.parse("http://datvephimstu.herokuapp.com/"));

                startActivity(Intent.createChooser(share, "Share Image"));
            }
        });

    }

    private void addControls() {
        imgShare = findViewById(R.id.icshare);
        imghinhphim = findViewById(R.id.imghinhphim);
        txtngaykhoichieu = findViewById(R.id.txtnoidungngaykhoichieu);
        txttheloai = findViewById(R.id.txtnoidungtheloai);
        txtthoigian = findViewById(R.id.txtnoidungthoigian);
        txttenphim = findViewById(R.id.txttenphim);
        txtmota = findViewById(R.id.txtmota);
        btnhuy = findViewById(R.id.btnhuy);
        btndatve = findViewById(R.id.btndatve);
        sharedPreferences = getSharedPreferences("datalogin", Context.MODE_PRIVATE);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
    }

    public int ktdangnhap() {
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


    public void createDialog(String mess) {
        Dialog dialog = new Dialog(DetalsMovieActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_chitietphim, null);
        dialog.setContentView(view);
        TextView txt = dialog.findViewById(R.id.txtmess);
        Button btnlogin = dialog.findViewById(R.id.btnlogin);
        Button btnhuy = dialog.findViewById(R.id.btnhuy);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentlogin = new Intent(DetalsMovieActivity.this, LoginActivity.class);
                startActivity(intentlogin);
            }
        });
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt.setText(mess);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
}

