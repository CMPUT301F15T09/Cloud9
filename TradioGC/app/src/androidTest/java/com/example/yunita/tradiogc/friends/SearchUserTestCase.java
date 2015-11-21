package com.example.yunita.tradiogc.friends;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListView;

import com.example.yunita.tradiogc.profile.ProfileActivity;
import com.example.yunita.tradiogc.user.Users;


public class SearchUserTestCase extends ActivityInstrumentationTestCase2 {
    private SearchUserActivity searchUserActivity;

    public SearchUserTestCase() {
        super(com.example.yunita.tradiogc.friends.SearchUserActivity.class);
    }

    /**
     * Use Cases 10 and 14
     * 02.01.01, 02.05.01
     * Test for searching users and viewing their profile.
     */
    public void testSearchUsernameUI() {
        // Start SearchUserActivity
        searchUserActivity = (SearchUserActivity) getActivity();


        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(ProfileActivity.class.getName(), null, false);


        // Search "test1"
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

                // Check username
                Users users = searchUserActivity.getUsers();
                assertTrue(users.get(0).getUsername().contains("test1"));

                // Click the first item
                View v = userList.getChildAt(0);
                userList.performItemClick(v, 0, v.getId());
            }
        });

        getInstrumentation().waitForIdleSync();

        // Check if it opens the ProfileActivity
        // Copy from https://developer.android.com/training/activity-testing/activity-functional-testing.html
        // Validate that ReceiverActivity has started
        ProfileActivity receiverActivity = (ProfileActivity)
                receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", receiverActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                ProfileActivity.class, receiverActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);

        // End of test: makes sure the edit activity is closed
        receiverActivity.finish();
    }
}