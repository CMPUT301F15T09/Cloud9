package com.example.yunita.tradiogc.trade;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ListView;

import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.user.User;

public class TradeActivityTest extends ActivityInstrumentationTestCase2 {

    private TradeActivity tradeActivity;
    private Item item_for_trade;

    public TradeActivityTest(){
        super(com.example.yunita.tradiogc.trade.TradeActivity.class);
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    @Override
    public void setUp(){
        // login as "test_borrower"
        final User test_borrower = new User();
        test_borrower.setUsername("test_borrower");
        test_borrower.setLocation("edmonton");
        test_borrower.setPhone("7809998881");
        test_borrower.setEmail("test@yahoo.com");
        Item item = new Item("Chapters", 0, 50.00, "chapters gc", true, 1, 0);
        Inventory ann_inventory = new Inventory();
        ann_inventory.add(item);
        test_borrower.setInventory(ann_inventory);

        LoginActivity.USERLOGIN = test_borrower;

        Intent itemIntent = new Intent();
        item_for_trade = new Item("Bestbuy", 1, 150.00, "bestbuy gc", true, 1, 0);
        itemIntent.putExtra("item_for_trade", item_for_trade);
        itemIntent.putExtra("owner_name", "test_owner");
        setActivityIntent(itemIntent);
    }

    /**
     * Use Case 18
     * 04.01.01
     * Test for offering a trade to a friend.
     */
    public void testOfferTradeWithFriends(){
        // starts Trade Detail Activity
        tradeActivity = (TradeActivity) getActivity();

        // offer a trade
        // borrower offers 0 item
        tradeActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button offerTrade = tradeActivity.getOfferTradeButton();
                offerTrade.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();

        ListView borrowerOfferListView = tradeActivity.getBorrowerOfferListView();
        int size = borrowerOfferListView.getAdapter().getCount();
        assertEquals(size, 0);
    }

    /**
     * Use Case 23
     * 04.05.01
     * Test for editing a trade while it is getting composed.
     */
    public void testEditTrade(){
        // starts Trade Detail Activity
        tradeActivity = (TradeActivity) getActivity();

        // offer a trade
        // borrower offers 0 item, then edit his offer by adding an item into his offer
        tradeActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListView borrowerInventoryListView = tradeActivity.getBorrowerInventoryListView();
                borrowerInventoryListView.performItemClick(
                        borrowerInventoryListView.getAdapter().getView(0, null, null),
                        0, borrowerInventoryListView.getAdapter().getItemId(0));
            }
        });

        getInstrumentation().waitForIdleSync();

        ListView borrowerOfferListView = tradeActivity.getBorrowerOfferListView();
        int size = borrowerOfferListView.getAdapter().getCount();
        assertEquals(size, 1);
    }

    /**
     * Use Case 24
     * 04.06.01
     * Test for deleting a trade while it is getting composed.
     */
    public void testDeleteTrade(){
        // starts Trade Detail Activity
        tradeActivity = (TradeActivity) getActivity();

        // cancel trade
        tradeActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button cancelTrade = tradeActivity.getCancelButton();
                cancelTrade.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();

        // cancel trade will just close the activity right
        // away and won't save the trade to webserver
        assertTrue(tradeActivity.isFinishing());
    }

}
