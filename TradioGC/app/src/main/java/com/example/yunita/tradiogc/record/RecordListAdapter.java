package com.example.yunita.tradiogc.record;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.trade.Trade;
import com.example.yunita.tradiogc.trade.Trades;
import com.example.yunita.tradiogc.user.User;
import com.example.yunita.tradiogc.user.Users;


public class RecordListAdapter extends ArrayAdapter{

    private Context context;
    private int id;
    private Trades trades;

    public RecordListAdapter(Context context, int id, Trades trades) {
        super(context, id, trades);
        this.context = context;
        this.id = id;
        this.trades = trades;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(id, null);
        }

        TextView username = (TextView) view.findViewById(R.id.username);
        TextView ownerItemName = (TextView) view.findViewById(R.id.ownerItemName);
        TextView status = (TextView) view.findViewById(R.id.status);

        Trade trade = trades.get(position);

        if (trade != null) {
            if (LoginActivity.USERLOGIN.getUsername().equals(trade.getBorrower())) {
                username.setText(trade.getOwner());
            } else {
                username.setText(trade.getBorrower());
            }

            ownerItemName.setText(trade.getOwnerItem().getName());

            String status_str = trade.getStatus();
            status.setText(String.valueOf(status_str.charAt(0)).toUpperCase() + status_str.substring(1));


            switch (status_str) {
                case "accepted":
                    status.setTextColor(Color.parseColor("#009688"));
                    break;
                case "declined":
                    status.setTextColor(Color.parseColor("#BF360C"));
                    break;
                case "completed":
                    status.setTextColor(Color.parseColor("#212121"));
                    break;
                case "offered":
                    status.setTextColor(Color.parseColor("#FFC107"));
                    break;
            }

        }

        return view;
    }

}
