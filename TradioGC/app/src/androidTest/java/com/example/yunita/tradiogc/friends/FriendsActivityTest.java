package com.example.yunita.tradiogc.friends;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.profile.ProfileActivity;
import com.example.yunita.tradiogc.user.User;


public class FriendsActivityTest extends ActivityInstrumentationTestCase2 {
    private FriendsActivity friendsActivity;

    public FriendsActivityTest() {
        super(com.example.yunita.tradiogc.friends.FriendsActivity.class);
    }

    /**
     * test for "ADD NEW FRIEND" button
     */
    public void testSearchUserButton() {
        // set a login user
        LoginActivity.USERLOGIN = new User();

        // starts FriendsActivity
        friendsActivity = (FriendsActivity) getActivity();

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(SearchUserActivity.class.getName(), null, false);

        // click the button
        friendsActivity.runOnUiThread(new Runnable() {
            public void run() {
                Button search = friendsActivity.getSearch_user_button();
                search.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        // check if it opens the SearchUserActivity
        // Copy from https://developer.android.com/training/activity-testing/activity-functional-testing.html
        // Validate that ReceiverActivity is started
        SearchUserActivity receiverActivity = (SearchUserActivity)
                receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", receiverActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                SearchUserActivity.class, receiverActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);
        //end of test: make sure the edit activity is closed
        receiverActivity.finish();

        LoginActivity.USERLOGIN = null;
    }

    /**
     * Use Case 13, 15
     * test for opening friends' profile and deleting friend
     */
    public void testFriendsList() {
        // set a login user
        LoginActivity.USERLOGIN = new User();
        LoginActivity.USERLOGIN.setUsername("test");
        Friends friends = LoginActivity.USERLOGIN.getFriends();
        friends.add("test1");

        // starts FriendsActivity
        friendsActivity = (FriendsActivity) getActivity();

        // check the first item in list view
        final ListView friendList = friendsActivity.getFriendList();
        String friend = (String) friendList.getItemAtPosition(0);
        assertEquals(friend, "test1");

        // set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(ProfileActivity.class.getName(), null, false);

        // click the first friend
        friendsActivity.runOnUiThread(new Runnable() {
            public void run() {
                View v = friendList.getChildAt(0);
                friendList.performItemClick(v, 0, v.getId());
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

        // long click the first friend
        friendsActivity.runOnUiThread(new Runnable() {
            public void run() {
                View v = friendList.getChildAt(0);
                v.performLongClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        // check friendlist
        assertTrue(LoginActivity.USERLOGIN.getFriends().isEmpty());


        LoginActivity.USERLOGIN = null;
    }
}