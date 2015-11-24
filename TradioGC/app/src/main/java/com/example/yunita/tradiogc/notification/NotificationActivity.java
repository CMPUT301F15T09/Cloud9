package com.example.yunita.tradiogc.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.trade.Trade;
import com.example.yunita.tradiogc.trade.TradeDetailActivity;
import com.example.yunita.tradiogc.trade.Trades;

public class NotificationActivity extends AppCompatActivity {

    private ListView notificationListView;
    private ArrayAdapter<Trade> notificationArrayAdapter;
    private Trades tradesNotif = new Trades();
    private NotificationController notificationController;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        notificationListView = (ListView) findViewById(R.id.notification_list_view);
        notificationController = new NotificationController();
    }

    @Override
    protected void onStart() {
        super.onStart();

        notificationArrayAdapter = new ArrayAdapter<>(this, R.layout.inventory_list_item, tradesNotif);
        notificationListView.setAdapter(notificationArrayAdapter);

        notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trade trade = tradesNotif.get(position);
                int index = LoginActivity.USERLOGIN.getTrades().indexOf(trade);

                // call another intent
                Intent intent = new Intent(context, TradeDetailActivity.class);
                intent.putExtra("index_of_trade", index);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        notificationController.updateNotification();

        tradesNotif.clear();
        tradesNotif.addAll(LoginActivity.USERLOGIN.getTrades().getOfferedTrade());
        tradesNotif.addAll(LoginActivity.USERLOGIN.getTrades().getAcceptedTrade());

        notificationArrayAdapter.notifyDataSetChanged();
    }
}
