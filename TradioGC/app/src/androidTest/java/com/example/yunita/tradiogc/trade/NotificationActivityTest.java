package com.example.yunita.tradiogc.trade;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.notification.Notification;
import com.example.yunita.tradiogc.notification.NotificationActivity;
import com.example.yunita.tradiogc.user.User;

import java.util.ArrayList;

public class NotificationActivityTest extends ActivityInstrumentationTestCase2 {

    private NotificationActivity notificationActivity;
    private ArrayAdapter<Notification> notificationArrayAdapter;
    private ListView notificationListView;

    public NotificationActivityTest(){
        super(com.example.yunita.tradiogc.notification.NotificationActivity.class);
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    @Override
    public void setUp(){
        // login as "test"
        User test = new User();
        test.setUsername("test");
        test.setLocation("edmonton");
        test.setPhone("7809998881");
        test.setEmail("tradiogctest@yopmail.com");
        test.getNotifications().add(new Notification(new Trade()));
        Item item = new Item(1,"Chapters", 0, 50.00, "chapters gc", true, 1, 0);
        Inventory ann_inventory = new Inventory();
        ann_inventory.add(item);
        test.setInventory(ann_inventory);

        LoginActivity.USERLOGIN = test;
    }

    /**
     * Use Case 19
     * 04.02.01
     * Test for getting trade notifications when a trade is offered.
     */
    public void testGetTradeNotifications(){
        notificationActivity = (NotificationActivity) getActivity();
        notificationActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notificationListView = notificationActivity.getNotificationListView();
                notificationArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                        R.layout.inventory_list_item, LoginActivity.USERLOGIN.getNotifications());
                notificationListView.setAdapter(notificationArrayAdapter);
            }
        });

        assertEquals(notificationListView.getAdapter().getCount(), 1);
    }
}
