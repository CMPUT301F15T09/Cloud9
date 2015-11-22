package com.example.yunita.tradiogc.notification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.trade.OfferedTrade;
import com.example.yunita.tradiogc.trade.Trade;
import com.example.yunita.tradiogc.trade.Trades;
import com.example.yunita.tradiogc.user.User;

import java.util.ArrayList;

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
        Trades temp = new Trades(owner.getTrades());
        tradesNotif = temp.getOfferedTrade();

        System.out.println("================> " + tradesNotif.size());

        notificationArrayAdapter = new ArrayAdapter<>(this, R.layout.inventory_list_item, tradesNotif);
        notificationListView.setAdapter(notificationArrayAdapter);

    }


}
