package com.example.yunita.tradiogc.friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.user.User;
import com.example.yunita.tradiogc.user.Users;

class UserListAdapter extends ArrayAdapter {

    private Context context;
    private int id;
    private Users users;

    public UserListAdapter(Context context, int id, Users users) {
        super(context, id, users);
        this.context = context;
        this.id = id;
        this.users = users;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(id, null);
        }

        TextView username = (TextView) view.findViewById(R.id.username);
        TextView location = (TextView) view.findViewById(R.id.location);
        TextView number = (TextView) view.findViewById(R.id.numberOfTrades);

        User user = users.get(position);
        if (user != null) {
            int numberOfTrades = user.getTrades().getCurrentTrades().size() + user.getTrades().getCompletedTrades().size();

            username.setText(user.getUsername());
            location.setText(user.getLocation());
            number.setText(Integer.toString(numberOfTrades) + " Trades");
        }

        return view;
    }

}
