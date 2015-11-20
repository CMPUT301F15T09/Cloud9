package com.example.yunita.tradiogc.profile;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.yunita.tradiogc.friends.FriendsController;
import com.example.yunita.tradiogc.inventory.FriendsInventoryActivity;
import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.MyInventoryActivity;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.user.User;


public class ProfileActivityTest extends ActivityInstrumentationTestCase2 {
    private ProfileActivity profileActivity;

    public ProfileActivityTest() {
        super(com.example.yunita.tradiogc.profile.ProfileActivity.class);
    }

    /**
     * Use Case 14, 15
     * test for profile
     */
    public void testProfile() {
        // starts ProfileActivity
        profileActivity = (ProfileActivity) getActivity();

        // set a test user
        User user = new User();
        user.setUsername("Jake");
        user.setLocation("EDMONTON");
        user.setEmail("jake@email");
        user.setPhone("780999jake");
        profileActivity.setUser(user);

        // make the profile show the user info
        TextView username = profileActivity.getUsername();
        TextView location = profileActivity.getLocation();
        TextView email = profileActivity.getEmail();
        TextView phone = profileActivity.getPhone();
        Runnable updateGUIDetails = profileActivity.getDoUpdateGUIDetails();
        profileActivity.runOnUiThread(updateGUIDetails);
        getInstrumentation().waitForIdleSync();

        assertEquals(username.getText(), "Jake");
        assertEquals(location.getText(), "EDMONTON");
        assertEquals(email.getText(), "jake@email");
        assertEquals(phone.getText(), "780999jake");
    }

    /**
     * Use Case 39
     * test for edit button
     */
    public void testEditButton() {
        // starts ProfileActivity
        profileActivity = (ProfileActivity) getActivity();

        LoginActivity.USERLOGIN = null;
        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(EditProfileActivity.class.getName(), null, false);

        profileActivity.runOnUiThread(new Runnable() {
            public void run() {
                ImageButton edit = profileActivity.getEdit_button();
                edit.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        // Copy from https://developer.android.com/training/activity-testing/activity-functional-testing.html
        // Validate that ReceiverActivity is started
        EditProfileActivity receiverActivity = (EditProfileActivity)
                receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", receiverActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                EditProfileActivity.class, receiverActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);
        //end of test: make sure the edit activity is closed
        receiverActivity.finish();
    }

    /**
     * test for my inventory button
     */
    public void testMyInventoryButton() {
        LoginActivity.USERLOGIN = new User();
        LoginActivity.USERLOGIN.setInventory(new Inventory());

        // starts ProfileActivity
        profileActivity = (ProfileActivity) getActivity();
        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(MyInventoryActivity.class.getName(), null, false);

        profileActivity.runOnUiThread(new Runnable() {
            public void run() {
                Button inventory = profileActivity.getMyinventory_button();
                inventory.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        // Copy from https://developer.android.com/training/activity-testing/activity-functional-testing.html
        // Validate that ReceiverActivity is started
        MyInventoryActivity receiverActivity = (MyInventoryActivity)
                receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", receiverActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                MyInventoryActivity.class, receiverActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);
        //end of test: make sure the edit activity is closed
        receiverActivity.finish();

        LoginActivity.USERLOGIN = null;
    }

    /**
     * Use Case 16
     * test for friend inventory button
     */
    public void testFriendInventoryButton() {
        // starts ProfileActivity
        profileActivity = (ProfileActivity) getActivity();

        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(FriendsInventoryActivity.class.getName(), null, false);

        profileActivity.runOnUiThread(new Runnable() {
            public void run() {
                Button inventory = profileActivity.getFriendinventory_button();
                inventory.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        // Copy from https://developer.android.com/training/activity-testing/activity-functional-testing.html
        // Validate that ReceiverActivity is started
        FriendsInventoryActivity receiverActivity = (FriendsInventoryActivity)
                receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", receiverActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                FriendsInventoryActivity.class, receiverActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);
        //end of test: make sure the edit activity is closed
        receiverActivity.finish();
    }

    /**
     * Use Case 12
     * test for add friend button
     */
    public void testAddFriendButton() {
        // starts ProfileActivity
        profileActivity = (ProfileActivity) getActivity();
        // set a default login user
        LoginActivity.USERLOGIN = new User();
        LoginActivity.USERLOGIN.setUsername("test");

        // UI thread for clicking add button
        profileActivity.runOnUiThread(new Runnable() {
            public void run() {
                Button add = profileActivity.getAddfriend_button();
                add.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        assertTrue(LoginActivity.USERLOGIN.getFriends().contains(""));
        // clear the user data
        FriendsController friendsController = new FriendsController(profileActivity.getContext());
        friendsController.deleteFriend("");
        LoginActivity.USERLOGIN = null;
    }
}