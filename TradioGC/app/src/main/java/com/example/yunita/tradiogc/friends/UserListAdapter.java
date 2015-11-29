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

/**
 * This adapter takes the position of the username displayed in the user list and
 * returns the user list item containing the user's username, location, and
 * number of trades that the user has.
 */
class UserListAdapter extends ArrayAdapter {

    private Context context;
    private int id;
    private Users users;

    /**
     * Class constructor specifying that this adapter class uses a context, id, and
     * list of users.
     *
     * @param context
     * @param id
     * @param users
     */
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
