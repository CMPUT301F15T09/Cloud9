package com.example.yunita.tradiogc.notification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.trade.Trade;
import com.example.yunita.tradiogc.trade.Trades;
import com.example.yunita.tradiogc.user.User;

public class NotificationActivity extends AppCompatActivity {

    private ListView notificationListView;
    private ArrayAdapter<Trade> notificationArrayAdapter;
    private Trades tradesNotif;

    private User owner = LoginActivity.USERLOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        notificationListView = (ListView) findViewById(R.id.notification_list_view);
    }

    @Override
    protected void onStart(){
        super.onStart();
        Trades offers = new Trades(owner.getTrades());
        tradesNotif = offers.getOfferedTrade();

        notificationArrayAdapter = new ArrayAdapter<>(this, R.layout.inventory_list_item, tradesNotif);
        notificationListView.setAdapter(notificationArrayAdapter);

    }


}
