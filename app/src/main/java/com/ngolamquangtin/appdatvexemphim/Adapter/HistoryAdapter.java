package com.ngolamquangtin.appdatvexemphim.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.astritveliu.boom.Boom;
import com.ngolamquangtin.appdatvexemphim.Activity.BillDetailActivity;
import com.ngolamquangtin.appdatvexemphim.DTO.BillV2;
import com.ngolamquangtin.appdatvexemphim.R;
import com.ngolamquangtin.appdatvexemphim.Util.Util;

import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter {

    Context context;
    ArrayList<BillV2> bills;

    public HistoryAdapter(Context context, ArrayList<BillV2> bills) {
        this.context = context;
        this.bills = bills;
    }

    @Override
    public int getCount() {
        return bills.size();
    }

    @Override
    public Object getItem(int i) {
        return bills.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_history, null);
            viewHolder = new ViewHolder();

            viewHolder.txtNameCinema = view.findViewById(R.id.txtcinema);
            viewHolder.txtDate = view.findViewById(R.id.txtdate);
            viewHolder.txtShowTime = view.findViewById(R.id.txtshowtime);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        BillV2 bill = bills.get(i);

        viewHolder.txtNameCinema.setText(bill.getTicketV2s().get(0).getRap().getTenrap());
        viewHolder.txtDate.setText("Ngày lập: " + Util.formatDateServerToClient(bill.getNgaylaphoadon()));
        viewHolder.txtShowTime.setText("Suất chiếu: " + Util.formatTime(bill.getTicketV2s().get(0).getSuatchieu().getGio()));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToScreenDetailBill = new Intent(context, BillDetailActivity.class);

                intentToScreenDetailBill.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentToScreenDetailBill.putExtra("BILL", bill);

                context.startActivity(intentToScreenDetailBill);
            }
        });

        new Boom(view);

        return view;
    }


    public class ViewHolder{
        TextView txtNameCinema, txtDate, txtShowTime;
    }


}
