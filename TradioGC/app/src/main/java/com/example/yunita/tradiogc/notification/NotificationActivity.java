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
import com.example.yunita.tradiogc.trade.Trade;
import com.example.yunita.tradiogc.trade.Trades;

public class NotificationActivity extends AppCompatActivity {

    private ListView notificationListView;
    private ArrayAdapter<Trade> notificationArrayAdapter;
    private Trades tradesNotif;

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
    }


}
