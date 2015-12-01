package com.example.yunita.tradiogc.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.yunita.tradiogc.CheckNetwork;
import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.trade.TradeDetailActivity;

/**
 * This activity handles notifications received by the user.
 */
public class NotificationActivity extends AppCompatActivity {
    private ListView notificationListView;

    private ArrayAdapter<Notification> notificationArrayAdapter;
    private Notifications notifications = new Notifications();

    private NotificationController notificationController;
    private Context context = this;

    private CheckNetwork checkNetwork = new CheckNetwork(context);

    private ProgressBar progressBar;

    /**
     * Gets notification list view.
     *
     * @return notification list view.
     */
    public ListView getNotificationListView() {
        return notificationListView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        notificationListView = (ListView) findViewById(R.id.notification_list_view);

        notificationController = new NotificationController(this);

        progressBar = (ProgressBar) findViewById(R.id.notif_progressBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notification_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Sets up the list view of notifications received by the user.
     * When the user presses on a notification, then the user is sent to
     * view the trade's Trade Detail page.
     * When the user long presses on a notification, the notification is temporarily removed
     * from view until the user refreshes the notification tab.
     */
    @Override
    protected void onStart() {
        super.onStart();
        if(checkNetwork.isOnline()) {
            notificationArrayAdapter = new ArrayAdapter<>(this, R.layout.inventory_list_item, notifications);
            notificationListView.setAdapter(notificationArrayAdapter);

            notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Notification notification = notifications.get(position);
                    int tradeId = notification.getTrade().getId();

                    notification.setRead(true);
                    if (!LoginActivity.USERLOGIN.getTrades().findTradeById(tradeId).getStatus().equals("offered")) {
                        LoginActivity.USERLOGIN.getNotifications().remove(notification);
                    }
                    notificationController.updateToWebServer();

                    // Calls another intent
                    Intent intent = new Intent(context, TradeDetailActivity.class);
                    intent.putExtra("trade_id", tradeId);
                    startActivity(intent);
                }
            });

            // Delete notifications on long click
            notificationListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Notification notification = notifications.get(position);
                    // Deletes it in webserver

                    LoginActivity.USERLOGIN.getNotifications().remove(notification);
                    notificationController.updateToWebServer();
                    // Deletes it from the list
                    notifications.remove(notification);
                    notificationArrayAdapter.notifyDataSetChanged();
                    // Shows a toast
                    Toast.makeText(context, "Deleting Success", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
        }else{
            notificationArrayAdapter = new ArrayAdapter<>(this, R.layout.inventory_list_item, notifications);
            notificationListView.setAdapter(notificationArrayAdapter);
        }
    }

    /**
     * Loads the user's notifications.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (checkNetwork.isOnline()) {
            progressBar.setVisibility(View.VISIBLE);
            Thread updateNotificationThread = new UpdateNotificationThread();
            updateNotificationThread.start();
        }
        else{
            progressBar.setVisibility((View.GONE));
        }
    }

    /**
     * Called when the refresh button is pressed.
     * <p>Refreshes the notification list view.
     *
     * @param item refresh button
     */
    public void updateNotification(MenuItem item) {
        onResume();
    }

    /**
     * Notifies the list view to be refreshed.
     */
    public void notifyUpdated() {
        Runnable doUpdateGUIList = new Runnable() {
            public void run() {
                notifications.clear();
                notifications.addAll(LoginActivity.USERLOGIN.getNotifications());
                notificationArrayAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        };
        runOnUiThread(doUpdateGUIList);
    }

    /**
     * This thread updates the notification list.
     */
    class UpdateNotificationThread extends Thread {
        public UpdateNotificationThread() {}

        @Override
        public void run() {
            notificationController.updateNotification();
            notifyUpdated();
        }

    }
}