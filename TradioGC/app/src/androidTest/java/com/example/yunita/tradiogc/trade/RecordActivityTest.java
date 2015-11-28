package com.example.yunita.tradiogc.trade;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.example.yunita.tradiogc.record.RecordActivity;

public class RecordActivityTest extends ActivityInstrumentationTestCase2 {

    private RecordActivity recordActivity;

    public RecordActivityTest(){
        super(com.example.yunita.tradiogc.record.RecordActivity.class);
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

    /**
     * Use Case 27
     * 04.08.01, 04.09.01
     * Test for browsing a user's current trades.
     */
    public void testBrowseCurrentTrades(){

    }

    /**
     * Use Case 28
     * 04.08.01, 04.09.01
     * Test for browsing trades that the user had sent.
     */
    public void testBrowseMySentTrades(){

    }

    /**
     * Use Case 29
     * 04.08.01, 04.09.01
     * Test for browsing trades that were offered to the user.
     */
    public void testBrowseTradesOfferedToMe(){

    }

    /**
     * Use Case 31
     * 04.12.01
     * Test for browsing a user's completed trades.
     */
    public void testBrowseCompleteTrades(){

    }

}
