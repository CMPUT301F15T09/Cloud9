package com.example.yunita.tradiogc.trade;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.record.RecordActivity;
import com.example.yunita.tradiogc.record.RecordListAdapter;
import com.example.yunita.tradiogc.user.User;

public class RecordActivityTest extends ActivityInstrumentationTestCase2 {

    private RecordActivity recordActivity;
    private ListView recordListView;
    private RecordListAdapter recordListAdapter;

    public RecordActivityTest(){
        super(com.example.yunita.tradiogc.record.RecordActivity.class);
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    @Override
    public void setUp(){
        final User test_borrower = new User();
        test_borrower.setUsername("test_borrower");
        test_borrower.setLocation("edmonton");
        test_borrower.setPhone("7809998881");
        test_borrower.setEmail("test@yahoo.com");

        Trade pendingTrade = new Trade();
        pendingTrade.setStatus("pending");
        Trade offeredTrade = new Trade();
        offeredTrade.setStatus("offered");
        Trade acceptedTrade = new Trade();
        acceptedTrade.setStatus("accepted");
        Trade declinedTrade = new Trade();
        declinedTrade.setStatus("declined");
        Trade completedTrade = new Trade();
        completedTrade.setStatus("completed");

        Trades test_borrower_trades = new Trades();
        test_borrower_trades.add(pendingTrade);
        test_borrower_trades.add(offeredTrade);
        test_borrower_trades.add(acceptedTrade);
        test_borrower_trades.add(declinedTrade);
        test_borrower_trades.add(completedTrade);

        test_borrower.setTrades(test_borrower_trades);

        LoginActivity.USERLOGIN = test_borrower;
    }

    /**
     * Use Case 26
     * 04.08.01, 04.09.01
     * Test for browsing a user's past trades.
     */

    public void testBrowsePastTrades(){
        recordActivity = (RecordActivity) getActivity();

        recordActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recordListAdapter = new RecordListAdapter(getActivity().getApplicationContext(),
                        R.layout.record_list_item,
                        LoginActivity.USERLOGIN.getTrades().getPastTrades());
                recordListView = recordActivity.getRecordListView();
                recordListView.setAdapter(recordListAdapter);
            }
        });

        getInstrumentation().waitForIdleSync();

        assertEquals(recordListAdapter.getCount(), 2);
    }

    /**
     * Use Case 27
     * 04.08.01, 04.09.01
     * Test for browsing a user's current trades.
     */
    public void testBrowseCurrentTrades(){
        recordActivity = (RecordActivity) getActivity();

        recordActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recordListAdapter = new RecordListAdapter(getActivity().getApplicationContext(),
                        R.layout.record_list_item,
                        LoginActivity.USERLOGIN.getTrades().getCurrentTrades());
                recordListView = recordActivity.getRecordListView();
                recordListView.setAdapter(recordListAdapter);
            }
        });

        getInstrumentation().waitForIdleSync();

        assertEquals(recordListAdapter.getCount(), 3);
    }

    /**
     * Use Case 28
     * 04.08.01, 04.09.01
     * Test for browsing trades that the user had sent.
     */
    public void testBrowseMySentTrades(){
        recordActivity = (RecordActivity) getActivity();

        recordActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recordListAdapter = new RecordListAdapter(getActivity().getApplicationContext(),
                        R.layout.record_list_item,
                        LoginActivity.USERLOGIN.getTrades().getPendingTrades());
                recordListView = recordActivity.getRecordListView();
                recordListView.setAdapter(recordListAdapter);
            }
        });

        getInstrumentation().waitForIdleSync();

        assertEquals(recordListAdapter.getCount(), 1);

    }

    /**
     * Use Case 29
     * 04.08.01, 04.09.01
     * Test for browsing trades that were offered to the user.
     */
    public void testBrowseTradesOfferedToMe(){
        recordActivity = (RecordActivity) getActivity();

        recordActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recordListAdapter = new RecordListAdapter(getActivity().getApplicationContext(),
                        R.layout.record_list_item,
                        LoginActivity.USERLOGIN.getTrades().getOfferedTrades());
                recordListView = recordActivity.getRecordListView();
                recordListView.setAdapter(recordListAdapter);
            }
        });

        getInstrumentation().waitForIdleSync();

        assertEquals(recordListAdapter.getCount(), 1);
    }

    /**
     * Use Case 31
     * 04.12.01
     * Test for browsing a user's completed trades.
     */
    public void testBrowseCompleteTrades(){
        recordActivity = (RecordActivity) getActivity();

        recordActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recordListAdapter = new RecordListAdapter(getActivity().getApplicationContext(),
                        R.layout.record_list_item,
                        LoginActivity.USERLOGIN.getTrades().getCompletedTrades());
                recordListView = recordActivity.getRecordListView();
                recordListView.setAdapter(recordListAdapter);
            }
        });

        getInstrumentation().waitForIdleSync();

        assertEquals(recordListAdapter.getCount(), 1);
    }

}
