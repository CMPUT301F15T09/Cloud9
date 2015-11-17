package com.example.yunita.tradiogc.friends;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.profile.ProfileActivity;
import com.example.yunita.tradiogc.user.User;


public class SearchUserActivityTest extends ActivityInstrumentationTestCase2 {
    private SearchUserActivity searchUserActivity;

    public SearchUserActivityTest() {
        super(com.example.yunita.tradiogc.friends.SearchUserActivity.class);
    }

    /**
     * test for searching user
     */
    public void testEditText() {
        // starts SearchUserActivity
        searchUserActivity = (SearchUserActivity) getActivity();

        final EditText search = searchUserActivity.getQuery_et();

        // edit the text
        searchUserActivity.runOnUiThread(new Runnable() {
            public void run() {
                search.setText("test");
            }
        });
        getInstrumentation().waitForIdleSync();

        // check the edit text
        assertTrue(search.getText().toString().contains("test"));
    }

    public void testUserList() {


        /*
        // set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(ProfileActivity.class.getName(), null, false);

        // click the first item
        friendsActivity.runOnUiThread(new Runnable() {
            public void run() {
                View v = friendList.getChildAt(0);
                friendList.performItemClick(v, 0, v.getId());
            }
        });
        getInstrumentation().waitForIdleSync();

        // Copy from https://developer.android.com/training/activity-testing/activity-functional-testing.html
        // Validate that ReceiverActivity is started
        ProfileActivity receiverActivity = (ProfileActivity)
                receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", receiverActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                ProfileActivity.class, receiverActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);
        //end of test: make sure the edit activity is closed
        receiverActivity.finish();
        */
    }

}