package com.example.yunita.tradiogc.trade;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.user.User;

public class TradeDetailActivityTest extends ActivityInstrumentationTestCase2 {

    private User test;
    private TradeDetailActivity tradeDetailActivity;
    private Item item_for_trade;
    private Trade trade;
    private EditText comments_et;

    public TradeDetailActivityTest(){
        super(com.example.yunita.tradiogc.trade.TradeDetailActivity.class);
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
     * Use Case 20
     * 04.03.01
     * Test for accepting a trade.
     */
    public void testAcceptTrade() {
        // starts Trade Detail Activity
        tradeDetailActivity = (TradeDetailActivity) getActivity();

        // accept the trade
        tradeDetailActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button acceptTrade = tradeDetailActivity.getAcceptTradeButton();
                acceptTrade.performClick();

                // email the borrower
                android.support.v7.app.AlertDialog alertDialog = tradeDetailActivity.getAcceptBuilder();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();

            }
        });

        getInstrumentation().waitForIdleSync();

        assertTrue(LoginActivity.USERLOGIN.getTrades().get(0).getStatus().equals("accepted"));
    }

    /**
     * Use Case 21
     * 04.03.01
     * Test for declining a trade.
     */
    public void testDeclineTrade(){
        // starts Trade Detail Activity
        tradeDetailActivity = (TradeDetailActivity) getActivity();

        // decline trade
        tradeDetailActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button declineTrade = tradeDetailActivity.getDeclineTradeButton();
                declineTrade.performClick();

                // no counter trade
            }
        });

        getInstrumentation().waitForIdleSync();

        // this trade is removed from user's trade list
        assertEquals(LoginActivity.USERLOGIN.getTrades().size(), 0);
    }

    /**
     * Use Case 25
     * 04.07.01
     * Test for sending emails with a trade's information.
     */
    public void testEmailTradeInfo(){
        // starts Trade Detail Activity
        tradeDetailActivity = (TradeDetailActivity) getActivity();

        // accept the trade
        tradeDetailActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button acceptTrade = tradeDetailActivity.getAcceptTradeButton();
                acceptTrade.performClick();

                // email the borrower
                android.support.v7.app.AlertDialog alertDialog = tradeDetailActivity.getAcceptBuilder();
                comments_et = tradeDetailActivity.getComments_et();
                comments_et.setText("ACCEPT TRADE TEST");
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();

            }
        });

        getInstrumentation().waitForIdleSync();

        assertTrue(comments_et.getText().toString().equals("ACCEPT TRADE TEST"));

    }

}
