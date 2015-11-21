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


public class ProfileUITestCase extends ActivityInstrumentationTestCase2 {
    private ProfileActivity profileActivity;

    public ProfileUITestCase() {
        super(com.example.yunita.tradiogc.profile.ProfileActivity.class);
    }

    /**
     * Use Case 13
     * Test for viewing a user's personal profile.
     */
    public void testViewPersonalProfileUI() {
        // Start ProfileActivity
        profileActivity = (ProfileActivity) getActivity();

        // Set a test user
        User user = new User();
        user.setUsername("Jake");
        user.setLocation("EDMONTON");
        user.setEmail("jake@email");
        user.setPhone("780999jake");
        profileActivity.setUser(user);

        // Make the profile show the user information
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
     * Test for the "edit profile" button.
     */
    public void testEditButton() {
        // Start ProfileActivity
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
        // Validate that ReceiverActivity has started
        EditProfileActivity receiverActivity = (EditProfileActivity)
                receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", receiverActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                EditProfileActivity.class, receiverActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);
        // End of test: make sure the edit activity is closed
        receiverActivity.finish();
    }

    /**
     * Test for the user's "inventory" button on their profile page.
     */
    public void testMyInventoryButton() {
        LoginActivity.USERLOGIN = new User();
        LoginActivity.USERLOGIN.setInventory(new Inventory());

        // Start ProfileActivity
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
        // Validate that ReceiverActivity has started
        MyInventoryActivity receiverActivity = (MyInventoryActivity)
                receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", receiverActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                MyInventoryActivity.class, receiverActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);
        // End of test: make sure the edit activity is closed
        receiverActivity.finish();

        LoginActivity.USERLOGIN = null;
    }

    /**
     * Test for the "inventory" button on the user's friend's profile page.
     */
    public void testFriendInventoryButton() {
        // Start ProfileActivity
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
        // Validate that ReceiverActivity has started
        FriendsInventoryActivity receiverActivity = (FriendsInventoryActivity)
                receiverActivityMonitor.waitForActivityWithTimeout(1000);
        assertNotNull("ReceiverActivity is null", receiverActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                FriendsInventoryActivity.class, receiverActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(receiverActivityMonitor);
        // End of test: make sure the edit activity is closed
        receiverActivity.finish();
    }

    /**
     * Use Case 11
     * 02.02.01
     * Test for adding a friend to a user's friend list.
     */
    public void testAddFriendUI() {
        // Start ProfileActivity
        profileActivity = (ProfileActivity) getActivity();
        // Set a default login user
        LoginActivity.USERLOGIN = new User();
        LoginActivity.USERLOGIN.setUsername("test");

        // UI thread for clicking the "add friend" button
        profileActivity.runOnUiThread(new Runnable() {
            public void run() {
                Button add = profileActivity.getAddfriend_button();
                add.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        assertTrue(LoginActivity.USERLOGIN.getFriends().contains(""));
        // Clear the user data
        FriendsController friendsController = new FriendsController(profileActivity.getContext());
        friendsController.deleteFriend("");
        LoginActivity.USERLOGIN = null;
    }
}
