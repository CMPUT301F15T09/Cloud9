package com.example.yunita.tradiogc.friends;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.profile.ProfileActivity;
import com.example.yunita.tradiogc.user.User;


public class FriendsUITestCase extends ActivityInstrumentationTestCase2 {
    private FriendsActivity friendsActivity;

    public FriendsUITestCase() {
        super(com.example.yunita.tradiogc.friends.FriendsActivity.class);
    }

    /**
     * Test for "ADD NEW FRIEND" button.
     */
    public void testSearchUserButton() {
        // Set a login user
        LoginActivity.USERLOGIN = new User();

        // Start FriendsActivity
        friendsActivity = (FriendsActivity) getActivity();

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(SearchUserActivity.class.getName(), null, false);

        // Click the button
        friendsActivity.runOnUiThread(new Runnable() {
            public void run() {
                Button search = friendsActivity.getSearch_user_button();
                search.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();

        // Check if it opens the SearchUserActivity
        // Copy from https://developer.android.com/training/activity-testing/activity-functional-testing.html
        // Validate that ReceiverActivity has started
        SearchUserActivity receiverActivity = (SearchUserActivity)
                receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", receiverActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                SearchUserActivity.class, receiverActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);

        // End of test: make sure the edit activity is closed
        receiverActivity.finish();

        LoginActivity.USERLOGIN = null;
    }

    /**
     * Use Cases 12 and 14
     * 02.01.01, 02.03.01
     * UI test for opening a friend's profile and deleting a friend.
     */
    public void testRemoveFriendUI() {
        // Set a login user
        LoginActivity.USERLOGIN = new User();
        LoginActivity.USERLOGIN.setUsername("test");
        Friends friends = LoginActivity.USERLOGIN.getFriends();
        friends.add("test1");

        // Start FriendsActivity
        friendsActivity = (FriendsActivity) getActivity();

        // Check the first item in the list view
        final ListView friendList = friendsActivity.getFriendList();
        String friend = (String) friendList.getItemAtPosition(0);
        assertEquals(friend, "test1");

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(ProfileActivity.class.getName(), null, false);

        // Click the first friend
        friendsActivity.runOnUiThread(new Runnable() {
            public void run() {
                View v = friendList.getChildAt(0);
                friendList.performItemClick(v, 0, v.getId());
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

        // End of test: make sure the edit activity is closed
        receiverActivity.finish();

        // Long click the first friend
        friendsActivity.runOnUiThread(new Runnable() {
            public void run() {
                View v = friendList.getChildAt(0);
                v.performLongClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        // Check that the friend list is empty
        assertTrue(LoginActivity.USERLOGIN.getFriends().isEmpty());

        LoginActivity.USERLOGIN = null;
    }
}