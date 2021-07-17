package com.ngolamquangtin.appdatvexemphim.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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

            viewHolder.txtDate = view.findViewById(R.id.txtdate);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        BillV2 bill = bills.get(i);

        viewHolder.txtDate.setText(Util.formatDateServerToClient(bill.getNgaylaphoadon()));

        return view;
    }


    public class ViewHolder{
        TextView txtDate;
    }


}
