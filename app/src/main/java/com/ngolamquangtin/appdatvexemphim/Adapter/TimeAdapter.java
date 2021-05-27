package com.ngolamquangtin.appdatvexemphim.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ngolamquangtin.appdatvexemphim.DTO.XuatChieu;
import com.ngolamquangtin.appdatvexemphim.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {


    Context context;
    ArrayList<XuatChieu> xuatChieus;
    onClickListenerRecyclerView onClickListenerRecyclerView;

    public TimeAdapter(Context context, ArrayList<XuatChieu> xuatChieus, onClickListenerRecyclerView onClickListenerRecyclerView) {
        this.context = context;
        this.xuatChieus = xuatChieus;
        this.onClickListenerRecyclerView = onClickListenerRecyclerView;
    }

    @NonNull
    @Override
    public  TimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new TimeAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_time,null));
    }

    @Override
    public int getItemCount() {
        return xuatChieus.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        XuatChieu xuatChieu = xuatChieus.get(position);
        if(xuatChieu != null){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            try {
                Date date = simpleDateFormat.parse(xuatChieu.getThoigian());
                holder.txtthoigian.setText(simpleDateFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtthoigian;
        LinearLayout linearbackground;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtthoigian = itemView.findViewById(R.id.txtthoigian);
            linearbackground = itemView.findViewById(R.id.linearbackground);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListenerRecyclerView.onClicked(getLayoutPosition(), linearbackground);
                }
            });
        }
    }

    public interface onClickListenerRecyclerView{
        void onClicked(int position, View view);
    }
}
