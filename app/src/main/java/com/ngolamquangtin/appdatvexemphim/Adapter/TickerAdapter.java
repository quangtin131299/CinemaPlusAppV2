package com.ngolamquangtin.appdatvexemphim.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.astritveliu.boom.Boom;
import com.ngolamquangtin.appdatvexemphim.Activity.DetailTickerActivity;
import com.ngolamquangtin.appdatvexemphim.DTO.Cinema;
import com.ngolamquangtin.appdatvexemphim.DTO.Movie;
import com.ngolamquangtin.appdatvexemphim.DTO.RoomV2;
import com.ngolamquangtin.appdatvexemphim.DTO.SeatV2;
import com.ngolamquangtin.appdatvexemphim.DTO.Ticker;
import com.ngolamquangtin.appdatvexemphim.DTO.TicketV2;
import com.ngolamquangtin.appdatvexemphim.DTO.TimeV2;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import java.util.ArrayList;

public class TickerAdapter extends BaseAdapter {

    Context context;
    ArrayList<TicketV2> tickers;

    public TickerAdapter(Context context, ArrayList<TicketV2> tickers) {
        this.context = context;
        this.tickers = tickers;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<TicketV2> getTickers() {
        return tickers;
    }

    public void setTickers(ArrayList<TicketV2> tickers) {
        this.tickers = tickers;
    }

    @Override
    public int getCount() {
        return tickers.size();
    }

    @Override
    public Object getItem(int position) {
        return tickers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_ticker, null);
            viewHolder = new ViewHolder();
            viewHolder.txtday = convertView.findViewById(R.id.txtday);
            viewHolder.txtghe = convertView.findViewById(R.id.txtghe);
            viewHolder.txtNgayDatVaThoiGian = convertView.findViewById(R.id.txtNgayDatVaThoiGian);
            viewHolder.txtTenrap = convertView.findViewById(R.id.txtTenrap);
            viewHolder.txtTenPhim = convertView.findViewById(R.id.txtTenPhim);
            viewHolder.txtTenPhong = convertView.findViewById(R.id.txtTenPhong);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TicketV2 ticket = tickers.get(position);

        if (ticket != null) {
            Cinema cinema = ticket.getRap();
            SeatV2 seat = ticket.getGhe();
            Movie movie = ticket.getPhim();
            RoomV2 room = ticket.getPhong();
            TimeV2 time = ticket.getSuatchieu();

            if (cinema != null) {
                viewHolder.txtTenrap.setText(cinema.getTenrap());
            }

            if (seat != null) {
                viewHolder.txtghe.setText(seat.getTenGhe());
                if (seat.getTenGhe() != null && seat.getTenGhe() != "") {
                    viewHolder.txtday.setText(seat.getTenGhe().charAt(0) + "");
                }
            }

            if (movie != null) {
                viewHolder.txtTenPhim.setText(movie.getTenphim());
            }

            if (room != null) {
                viewHolder.txtTenPhong.setText(room.getTenphong());
            }

            if (time != null) {
                viewHolder.txtNgayDatVaThoiGian.setText(Util.formatDateServerToClient(ticket.getNgayDat()) + " " + time.getGio());
            }
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DetailTickerActivity.class);
                TicketV2 ticker = tickers.get(position);

                if(ticker != null){
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    i.putExtra("TICKER", ticker);

                    context.startActivity(i);
                }

            }
        });

        new Boom(convertView);

        return convertView;
    }

    public class ViewHolder {
        TextView txtTenPhim,
                 txtday,
                 txtghe,
                 txtTenrap,
                 txtNgayDatVaThoiGian,
                 txtTenPhong;
    }
}
