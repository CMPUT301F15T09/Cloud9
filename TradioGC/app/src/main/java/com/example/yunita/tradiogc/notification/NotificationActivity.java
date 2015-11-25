package com.example.yunita.tradiogc.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.trade.Trade;
import com.example.yunita.tradiogc.trade.TradeDetailActivity;
import com.example.yunita.tradiogc.trade.Trades;

public class NotificationActivity extends AppCompatActivity {

    private ListView notificationListView;
    private ArrayAdapter<Notification> notificationArrayAdapter;
    private Notifications notifications = new Notifications();
    private NotificationController notificationController;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        notificationListView = (ListView) findViewById(R.id.notification_list_view);
        notificationController = new NotificationController(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        notificationArrayAdapter = new ArrayAdapter<>(this, R.layout.inventory_list_item, notifications);
        notificationListView.setAdapter(notificationArrayAdapter);

        notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notification notification = notifications.get(position);
                int tradeId = notification.getTrade().getId();
                notification.setRead(true);
                // call another intent
                Intent intent = new Intent(context, TradeDetailActivity.class);
                intent.putExtra("trade_id", tradeId);
                startActivity(intent);
            }
        });

        // Delete friends on long click
        notificationListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Notification notification = notifications.get(position);
                // delete it in webserver
                LoginActivity.USERLOGIN.getNotifications().remove(notification);
                notificationController.updateToWebServer();
                // delete it in list
                notifications.remove(notification);
                notificationArrayAdapter.notifyDataSetChanged();
                // show a toast
                Toast.makeText(context, "Deleting Success", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        notificationController.updateNotification();

        notifications.clear();
        notifications.addAll(LoginActivity.USERLOGIN.getNotifications());

        notificationArrayAdapter.notifyDataSetChanged();
    }

    public void update(View view) {
        onResume();
    }
}
