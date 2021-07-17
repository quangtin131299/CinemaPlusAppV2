package com.ngolamquangtin.appdatvexemphim.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.astritveliu.boom.Boom;
import com.ngolamquangtin.appdatvexemphim.DTO.TimeV2;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import java.util.ArrayList;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {


    Context context;
    ArrayList<TimeV2> showTimes;
    onClickListenerRecyclerView onClickListenerRecyclerView;
    int isScreenSchdule, postionMovie;

    public int getPostionMovie() {
        return postionMovie;
    }

    public void setPostionMovie(int postionMovie) {
        this.postionMovie = postionMovie;
    }

    public TimeAdapter(Context context, ArrayList<TimeV2> showTimes, onClickListenerRecyclerView onClickListenerRecyclerView, int isScreenSchdule) {
        this.context = context;
        this.showTimes = showTimes;
        this.onClickListenerRecyclerView = onClickListenerRecyclerView;
        this.isScreenSchdule = isScreenSchdule;
    }

    public ArrayList<TimeV2> getShowTimes() {
        return showTimes;
    }

    @NonNull
    @Override
    public  TimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new TimeAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_time,null));
    }

    @Override
    public int getItemCount() {
        return showTimes != null ? showTimes.size() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (isScreenSchdule == 1){
            holder.cardTime.setCardBackgroundColor(context.getColor(R.color.colorUnSelect));
        }else{
            holder.cardTime.setCardBackgroundColor(context.getColor(R.color.colorBackround));
        }

        TimeV2 showTime = showTimes.get(position);

        if(showTime != null){
            holder.txtShowTime.setText(Util.formatTime(showTime.getGio()));
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtShowTime;
        LinearLayout linearbackground;
        CardView cardTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtShowTime = itemView.findViewById(R.id.txtthoigian);
            linearbackground = itemView.findViewById(R.id.linearbackground);
            cardTime = itemView.findViewById(R.id.cardtime);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClickListenerRecyclerView != null){
                        if(isScreenSchdule == 1){
                            onClickListenerRecyclerView.onClicked(getPosition(), getPostionMovie(),linearbackground, cardTime);
                        }else{
                            onClickListenerRecyclerView.onClicked(getPosition(), 0,linearbackground, cardTime);
                        }

                    }
                }
            });

            new Boom(itemView);
        }
    }

    public interface onClickListenerRecyclerView{
        void onClicked(int position, int idMovie,View view, CardView cardTime);
    }
}
