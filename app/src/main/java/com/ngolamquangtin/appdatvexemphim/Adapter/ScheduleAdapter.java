package com.ngolamquangtin.appdatvexemphim.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astritveliu.boom.Boom;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ngolamquangtin.appdatvexemphim.Activity.LoginActivity;
import com.ngolamquangtin.appdatvexemphim.Activity.SelectSeatActivity;
import com.ngolamquangtin.appdatvexemphim.DTO.Cinema;
import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.DTO.TickerBook;
import com.ngolamquangtin.appdatvexemphim.DTO.TimeV2;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ScheduleAdapter extends BaseAdapter implements TimeAdapter.onClickListenerRecyclerView{

    ArrayList<Movie> movies;
    ArrayList<TimeV2> showTimes;
    Context context;
    ArrayList<TimeAdapter> timeAdapters;
    Cinema cinema;
    Calendar dateBooking;
    SharedPreferences sharedPreferences;


    public ScheduleAdapter(Context context, ArrayList<Movie> movies, Cinema cinema) {
        sharedPreferences = context.getSharedPreferences("datalogin", Context.MODE_PRIVATE);
        this.context = context;
        this.movies = movies;
        this.showTimes = new ArrayList<>();
        this.cinema = cinema;
        timeAdapters = new ArrayList<>();
        
    }
    
    public void initTimeAdapter(int countmovie){
        for(int i = 0; i < countmovie; i++){
            showTimes = new ArrayList<>();
            timeAdapters.add(new TimeAdapter(context,showTimes, ScheduleAdapter.this, 1));
        }
    }

    public Calendar getDateBooking() {
        return dateBooking;
    }

    public void setDateBooking(Calendar dateBooking) {
        this.dateBooking = dateBooking;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if(view ==  null){
            view = LayoutInflater.from(context).inflate(R.layout.item_schedule, null);
            viewHolder = new ViewHolder();
            viewHolder.imgMovie = view.findViewById(R.id.imgmovie);
            viewHolder.txtNameMovie = view.findViewById(R.id.txtnamemovie);
            viewHolder.recyShowTime = view.findViewById(R.id.recytime);
            initTimeAdapter(getCount());

            LinearLayoutManager linearTime = new LinearLayoutManager(context);
            linearTime.setOrientation(RecyclerView.HORIZONTAL);
            viewHolder.recyShowTime.setLayoutManager(linearTime);


            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        Movie movie = movies.get(i);

        TimeAdapter timeAdapter = timeAdapters.get(i);
        timeAdapter.setPostionMovie(i);
        viewHolder.recyShowTime.setAdapter(timeAdapter);

        viewHolder.txtNameMovie.setText(movie.getTenphim());

        if(movie.getHinh() != null && !movie.getHinh().isEmpty()){

            Glide.with(context).asBitmap().load(movie.getHinh()).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull  Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    viewHolder.imgMovie.setImageBitmap(null);
                    viewHolder.imgMovie.setBackground(new BitmapDrawable(context.getResources(), resource));
                }

                @Override
                public void onLoadCleared(@Nullable  Drawable placeholder) {

                }

                @Override
                public void onLoadFailed(@Nullable  Drawable errorDrawable) {
                    String valueColor = Integer.toHexString(context.getResources().getColor(R.color.colorBackround, null));
                    viewHolder.imgMovie.setBackgroundColor(Color.parseColor("#"+valueColor));
                    viewHolder.imgMovie.setImageResource(R.drawable.ic_clapperboard);
                    viewHolder.imgMovie.setScaleType(ImageView.ScaleType.CENTER);
                }
            });

        }else{
            String valueColor = Integer.toHexString(context.getResources().getColor(R.color.colorBackround, null));
            viewHolder.imgMovie.setBackgroundColor(Color.parseColor("#"+valueColor));
            viewHolder.imgMovie.setImageResource(R.drawable.ic_clapperboard);
            viewHolder.imgMovie.setScaleType(ImageView.ScaleType.CENTER);
        }

        timeAdapters.get(i).getShowTimes().clear();

        timeAdapters.get(i).getShowTimes().addAll(movie.getSuatchieus());
        timeAdapter.notifyDataSetChanged();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, movie.getTenphim(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void showDialogLogin(String mess){
        Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.dialog_chitietphim);

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog.setCanceledOnTouchOutside(true);

        TextView txt = dialog.findViewById(R.id.txtmess);
        Button btnlogin = dialog.findViewById(R.id.btnlogin);

        txt.setText(mess);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentlogin = new Intent(context, LoginActivity.class);

                intentlogin.putExtra("CINEMA", cinema);
                intentlogin.putExtra("SCREEN_SCHEDULE", 1);

                context.startActivity(intentlogin);
            }
        });

        new Boom(btnlogin);

        dialog.show();
    }

    public class ViewHolder{
        ImageView imgMovie;
        TextView txtNameMovie;
        RecyclerView recyShowTime;
    }

    @Override
    public void onClicked(int position, int idMovie ,View view, CardView cardTime) {
        TickerBook tickerBook = new TickerBook();
        Movie movie = movies.get(idMovie);

        if(movie != null) {
            Intent intentToScreenSelectSeat = new Intent(context, SelectSeatActivity.class);

            intentToScreenSelectSeat.putExtra("ID_MOVIE", movie.getId());
            intentToScreenSelectSeat.putExtra("ID_CINEMA", cinema.getId());
            intentToScreenSelectSeat.putExtra("TEN_RAP", cinema.getTenrap());
            intentToScreenSelectSeat.putExtra("IMAGE_MOVIE", movie.getHinh());
            intentToScreenSelectSeat.putExtra("TIME_MOVIE", movie.getThoigian());

            TimeV2 time = timeAdapters.get(idMovie).getShowTimes().get(position);

            if (time != null) {

                Date currentDate = Calendar.getInstance().getTime();
                String [] arrTime = time.getGio().split(":");

                dateBooking.set(Calendar.HOUR_OF_DAY, Integer.parseInt(arrTime[0]));
                dateBooking.set(Calendar.MINUTE, Integer.parseInt(arrTime[1]));

                if (dateBooking.getTime().before(currentDate)) {
                    Util.ShowToastErrorMessage(context, "Suất chiếu đã chiếu rồi !");
                    return;
                }

                intentToScreenSelectSeat.putExtra("Id_SHOWTIME", time.getId());
                intentToScreenSelectSeat.putExtra("SHOW_TIME", Util.formatTime(time.getGio()));

            }

            intentToScreenSelectSeat.putExtra("TEN_PHIM", movie.getTenphim());
            intentToScreenSelectSeat.putExtra("NGAYDATHIENTAI", Util.formatDateClientToServer(Util.formatDateByCalendar(getDateBooking())));

            tickerBook.setIdrap(cinema.getId());
            tickerBook.setNgaydat(Util.formatDateClientToServer(Util.formatDateByCalendar(getDateBooking())));
            tickerBook.setIdphim(movie.getId());
            tickerBook.setIdsuat(time.getId());

            String idCustomer = sharedPreferences.getString("id", "");

            if(!idCustomer.isEmpty()){
                tickerBook.setIdkhachhang(Integer.parseInt(idCustomer));
            }else{
                showDialogLogin("Bạn chưa đăng nhập !");
                return;
            }

            intentToScreenSelectSeat.putExtra("TICKERBOOK", tickerBook);

            context.startActivity(intentToScreenSelectSeat);
        }
    }
}
