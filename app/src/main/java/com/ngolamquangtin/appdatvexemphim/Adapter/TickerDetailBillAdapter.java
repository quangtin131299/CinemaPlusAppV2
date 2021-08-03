package com.ngolamquangtin.appdatvexemphim.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ngolamquangtin.appdatvexemphim.DTO.SeatV2;
import com.ngolamquangtin.appdatvexemphim.DTO.TicketV2;
import com.ngolamquangtin.appdatvexemphim.R;

import java.util.ArrayList;

public class TickerDetailBillAdapter extends RecyclerView.Adapter<TickerDetailBillAdapter.ViewHolder> {

    Context context;
    ArrayList<TicketV2> tickers;

    public TickerDetailBillAdapter(Context context, ArrayList<TicketV2> tickers) {
        this.context = context;
        this.tickers = tickers;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_seat_detail_bill, null));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, int position) {
        TicketV2 ticker = tickers.get(position);

        holder.txtNameSeat.setText("Tên ghê: " + ticker.getGhe().getTenGhe());
    }

    @Override
    public int getItemCount() {
        return tickers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtNameSeat;


        public ViewHolder(@NonNull  View itemView) {
            super(itemView);

            txtNameSeat = itemView.findViewById(R.id.txtnameseat);
        }
    }
}
