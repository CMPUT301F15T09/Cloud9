package com.example.yunita.tradiogc.trade;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.user.User;

public class CounterTradeActivityTest extends ActivityInstrumentationTestCase2 {

    private User test;
    private CounterTradeActivity counterTradeActivity;
    private Item item_for_trade;
    private Trade trade;
    private EditText comments_et;

    public CounterTradeActivityTest(){
        super(com.example.yunita.tradiogc.trade.CounterTradeActivity.class);
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    @Override
    public void setUp(){
        // login as "test"
        test = new User();
        test.setUsername("test");
        test.setLocation("edmonton");
        test.setPhone("7809998881");
        test.setEmail("tradiogctest@yopmail.com");
        Item item = new Item(1,"Chapters", 0, 50.00, "chapters gc", true, 1, 0);
        Inventory ann_inventory = new Inventory();
        ann_inventory.add(item);
        test.setInventory(ann_inventory);

        LoginActivity.USERLOGIN = test;

        Intent itemIntent = new Intent();
        item_for_trade = new Item(2,"Bestbuy", 1, 150.00, "bestbuy gc", true, 1, 0);
        trade = new Trade("test", "test", item_for_trade, ann_inventory);
        trade.setId(1);
        test.getTrades().add(trade);
        itemIntent.putExtra("item_for_trade", item_for_trade);
        itemIntent.putExtra("trade_id", trade.getId());
        itemIntent.putExtra("owner_name", "test");
        itemIntent.putExtra("borrower_name", "test");
        setActivityIntent(itemIntent);
    }

    /**
     * Use Case 22
     * 04.04.01
     * Test for offering a counter trade.
     */
    public void testOfferCounterTrade(){
        // starts Trade Detail Activity
        counterTradeActivity = (CounterTradeActivity) getActivity();

        // offer a counter trade
        // owner sends counter trade to borrower
        counterTradeActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button offerTrade = counterTradeActivity.getCounterTradeButton();
                offerTrade.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();

        // owner will have a "pending" counter trade
        assertTrue(LoginActivity.USERLOGIN.getTrades().get(0).getStatus().equals("pending"));
    }

}
