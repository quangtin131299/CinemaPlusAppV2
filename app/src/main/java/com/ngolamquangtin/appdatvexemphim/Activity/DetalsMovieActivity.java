package com.ngolamquangtin.appdatvexemphim.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astritveliu.boom.Boom;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ngolamquangtin.appdatvexemphim.Adapter.CinemaOfMovieAdapter;
import com.ngolamquangtin.appdatvexemphim.Adapter.CommentAdapter;
import com.ngolamquangtin.appdatvexemphim.Config.RetrofitUtil;
import com.ngolamquangtin.appdatvexemphim.DTO.Cinema;
import com.ngolamquangtin.appdatvexemphim.DTO.Comment;
import com.ngolamquangtin.appdatvexemphim.DTO.CustomerV2;
import com.ngolamquangtin.appdatvexemphim.DTO.MessageResponse;
import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Service.Service;
import com.ngolamquangtin.appdatvexemphim.Util.Util;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalsMovieActivity extends AppCompatActivity {


    CinemaOfMovieAdapter cinemaAdapter;
    RecyclerView ryCinemaOfMovie;
    ImageView imghinhphim;
    ImageButton imgShare;
    TextView txttenphim, txtmota, txtngaykhoichieu, txtthoigian, txttheloai;
    Button btnhuy, btndatve;
    ImageButton btnComment;
    SharedPreferences sharedPreferences;
    YouTubePlayerView youTubePlayerView;
    CommentAdapter commentAdapter;
    ListView lvComments;
    Movie movie;
    ArrayList<Cinema> cinemas;
    ArrayList<Comment> comments;
    CustomerV2 customer;
    Dialog dialogProcessComment, dialogErrorComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detals_movie);

        addControls();

        setCustomer();

        getLifecycle().addObserver(youTubePlayerView);

        loadCinemaOfMovie(getIdMovie());

        addEvents();
    }



    private void isMovieNowShowing() {
        if(movie != null){
            if (movie.getStatus().equals("Đang chiếu") == false){
                btndatve.setBackgroundResource(R.drawable.background_btndatve_disable);
                btndatve.setText(getResources().getString(R.string.movieNotBooking));
            }
        }
    }

    private void loadDetailMovie() {
        Intent i = getIntent();
        movie = (Movie) i.getSerializableExtra("MOVIE");
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
                if (checklogin() == 1) {
                    if(movie != null && movie.getStatus().equals("Đang chiếu")){
                        Intent intentToScreenChooseSession = new Intent(DetalsMovieActivity.this, ChooseSessionActivity.class);

                        intentToScreenChooseSession.putExtra("ID_MOVIE", movie.getId());
                        intentToScreenChooseSession.putExtra("TEN_PHIM", movie.getTenphim());
                        intentToScreenChooseSession.putExtra("IMAGE_MOVIE", movie.getHinh());
                        intentToScreenChooseSession.putExtra("TIME_MOVIE", movie.getThoigian());

                        finish();
                        startActivity(intentToScreenChooseSession);
                    }else{
                        Util.ShowToastInforMessage(DetalsMovieActivity.this, getResources().getString(R.string.notBookingMovie));
                    }
                } else {
                    createDialog(getResources().getString(R.string.noLogin));
                }
            }
        });

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT,"http://codepath.com");

                startActivity(Intent.createChooser(share, "Share Image"));
            }
        });

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog dialogComment = new BottomSheetDialog(DetalsMovieActivity.this
                                                                        , R.style.BottomSheetDialogTheme);

                View btsCommentView = LayoutInflater.from(DetalsMovieActivity.this).inflate(R.layout.dialogbottom_comment, null);

                dialogComment.setContentView(btsCommentView);

                Button btnSubmitComent = dialogComment.findViewById(R.id.btncomment);
                EditText edtComment = dialogComment.findViewById(R.id.txtcomment);
                ImageView imgAvatar = dialogComment.findViewById(R.id.idimgapersonal);

                if(customer.getAnhDaiDien() != null && !customer.getAnhDaiDien().isEmpty()){
                    Glide.with(DetalsMovieActivity.this).load(customer.getAnhDaiDien()).into(imgAvatar);
                }

                btnSubmitComent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(checklogin() == 1){
                            String comment = edtComment.getText().toString().trim();

                            if(comment.isEmpty()){
                                Util.ShowToastErrorMessage(DetalsMovieActivity.this, getResources().getString(R.string.commentEmpty));
                                return;
                            }

                            Comment newComment = new Comment();

                            if(customer != null){
                                newComment.setIdCustomer(customer.getId());
                                newComment.setNameCustomer(customer.getHoTen());
                                newComment.setAvatar(customer.getAnhDaiDien());
                            }

                            Calendar calendar = Calendar.getInstance();
                            int daydefault = calendar.get(Calendar.DAY_OF_MONTH);
                            int monthdefault = calendar.get(Calendar.MONTH)+1;
                            int yeardefault = calendar.get(Calendar.YEAR);

                            newComment.setDatePost(Util.formatDateToServerFoCalendar(yeardefault, monthdefault, daydefault));
                            newComment.setIdMovie(movie.getId());

                            newComment.setContent(comment);

                            addNewComment(newComment);

                        }else{
                            createDialog(getResources().getString(R.string.noLogin));
                        }

                    }
                });

                commentAdapter = new CommentAdapter(dialogComment.getContext(), comments, imgAvatar);
                lvComments = dialogComment.findViewById(R.id.lvcomment);

                lvComments.setAdapter(commentAdapter);

                dialogComment.show();

                if(movie.getId() != null){
                    loadCommentOfMovieId(movie.getId());
                }

                new Boom(btnSubmitComent);

                Util.dissableBottomDialogDragging(dialogComment);
            }

        });

        new Boom(btnComment);
        new Boom(imgShare);
        new Boom(btndatve);
        new Boom(btnhuy);

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
        cinemas = new ArrayList<>();
        cinemaAdapter = new CinemaOfMovieAdapter(DetalsMovieActivity.this, cinemas);
        ryCinemaOfMovie = findViewById(R.id.rycinemaofmovie);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DetalsMovieActivity.this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        ryCinemaOfMovie.setLayoutManager(layoutManager);
        ryCinemaOfMovie.setAdapter(cinemaAdapter);
        btnComment = findViewById(R.id.iccomment);
        sharedPreferences = getSharedPreferences("datalogin", Context.MODE_PRIVATE);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        comments = new ArrayList<>();
        dialogProcessComment = new Dialog(DetalsMovieActivity.this);
        dialogErrorComment = new Dialog(DetalsMovieActivity.this);
    }

    public int checklogin() {
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

    public void addCommentToListView(Comment newComment){
        comments.add(0,newComment);
        commentAdapter.notifyDataSetChanged();

        lvComments.post(new Runnable() {
            @Override
            public void run() {
                lvComments.smoothScrollToPosition(0);
            }
        });
    }

    public void addNewComment(Comment newComment){
        showDialogProcessComment();

        Service service = RetrofitUtil.getService(DetalsMovieActivity.this);
        Call<MessageResponse> call = service.addNewComment(newComment);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                dismissDialogProcessComment();

                MessageResponse messRes = response.body();

                if(messRes != null && messRes.getStatusCode() == 1){
                    addCommentToListView(newComment);
                }else{
                    showDialogErrorComment();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {

                dismissDialogProcessComment();
            }
        });

    }

    public void loadCommentOfMovieId(int idMovie){
        Service service = RetrofitUtil.getService(DetalsMovieActivity.this);
        Call<List<Comment>> listCall = service.getAllCommentOfMovieId(idMovie);
        listCall.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(response.body() != null && response.body().size() != 0){
                    comments.clear();
                    comments.addAll(response.body());
                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });

    }

    public void createDialog(String mess) {
        Dialog dialog = new Dialog(DetalsMovieActivity.this);
        View view = getLayoutInflater().inflate(R.layout.dialog_chitietphim, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        TextView txt = dialog.findViewById(R.id.txtmess);
        Button btnlogin = dialog.findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(DetalsMovieActivity.this, LoginActivity.class);

                intentLogin.putExtra("SCREEN_MOVIE_DETAIL", 1);
                intentLogin.putExtra("MOVIE", movie);

                startActivity(intentLogin);
            }
        });

        new Boom(btnlogin);

        txt.setText(mess);
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void setCustomer(){
//        SharedPreferences spInfoCustomer = getSharedPreferences("datalogin", MODE_PRIVATE);

        if(checklogin() == 1){
            customer = new CustomerV2();
            customer.setHoTen(sharedPreferences.getString("hoten", ""));
            customer.setAnhDaiDien(sharedPreferences.getString("imagProfile", ""));
            customer.setEmail(sharedPreferences.getString("email", ""));
            customer.setId(Integer.valueOf(sharedPreferences.getString("id", "")));
        }

    }

    public void loadCinemaOfMovie(int idMovie){
        Service service = RetrofitUtil.getService(DetalsMovieActivity.this);
        Call<List<Cinema>> call = service.getCinemaByMovieId(idMovie);

        call.enqueue(new Callback<List<Cinema>>() {
            @Override
            public void onResponse(Call<List<Cinema>> call, Response<List<Cinema>> response) {
                cinemas.clear();

                if(response.body() != null && response.body().size() != 0){
                    cinemas.addAll(response.body());
                }

                cinemaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Cinema>> call, Throwable t) {

            }
        });
    }

    public int getIdMovie(){
        Intent i = getIntent();
        movie = (Movie) i.getSerializableExtra("MOVIE");

        if(movie != null){
            return  movie.getId();
        }

        return 0;
    }

    public void showDialogProcessComment(){
        if(dialogProcessComment != null){
            dialogProcessComment.setContentView(R.layout.dialog_processing);

            dialogProcessComment.getWindow().setBackgroundDrawableResource(R.color.transparent);

            dialogProcessComment.show();
        }
    }

    public void dismissDialogProcessComment(){
        if(dialogProcessComment != null && dialogProcessComment.isShowing()){
            dialogProcessComment.dismiss();
        }
    }

    public void showDialogErrorComment(){
        if(dialogErrorComment != null){
            dialogErrorComment.setContentView(R.layout.dialog_failed);

            dialogErrorComment.getWindow().setBackgroundDrawableResource(R.color.transparent);

            Button btnOk = dialogErrorComment.findViewById(R.id.btnOK);
            TextView txtMess = dialogErrorComment.findViewById(R.id.txtmess);

            txtMess.setText("Bình luận của bạn hông hợp lệ");

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogErrorComment.dismiss();
                }
            });

            new Boom(btnOk);

            dialogErrorComment.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadDetailMovie();

        isMovieNowShowing();
    }
}

