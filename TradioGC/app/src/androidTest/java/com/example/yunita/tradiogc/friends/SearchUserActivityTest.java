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
import com.example.yunita.tradiogc.user.Users;


public class SearchUserActivityTest extends ActivityInstrumentationTestCase2 {
    private SearchUserActivity searchUserActivity;

    public SearchUserActivityTest() {
        super(com.example.yunita.tradiogc.friends.SearchUserActivity.class);
    }

    /**
     * Use Case 11, 15
     * test for searching users and viewing their profile
     */
    public void testSearchUser() {
        // starts SearchUserActivity
        searchUserActivity = (SearchUserActivity) getActivity();


        // set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(ProfileActivity.class.getName(), null, false);


        // search "test1"
        searchUserActivity.runOnUiThread(new Runnable() {
            public void run() {
                Thread searchThread = searchUserActivity.new SearchThread("test1");
                searchThread.start();
                synchronized (searchThread) {
                    try {
                        searchThread.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ListView userList = searchUserActivity.getUserList();

                // check username
                Users users = searchUserActivity.getUsers();
                assertTrue(users.get(0).getUsername().contains("test1"));

                // click the first item
                View v = userList.getChildAt(0);
                userList.performItemClick(v, 0, v.getId());
            }
        });
        getInstrumentation().waitForIdleSync();

        // check if it opens the ProfileActivity
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
    }
}