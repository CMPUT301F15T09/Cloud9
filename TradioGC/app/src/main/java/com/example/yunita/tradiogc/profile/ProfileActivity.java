package com.example.yunita.tradiogc.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.friends.Friends;
import com.example.yunita.tradiogc.friends.FriendsController;
import com.example.yunita.tradiogc.inventory.FriendsInventoryActivity;
import com.example.yunita.tradiogc.inventory.MyInventoryActivity;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.user.User;
import com.example.yunita.tradiogc.user.UserController;

public class ProfileActivity extends AppCompatActivity {
    private String targetUsername = "";
    private User user;

    private UserController userController;
    private FriendsController friendsController;
    private Context context = this;

    private LinearLayout myprofile_panel;
    private LinearLayout stranger_panel;
    private LinearLayout friend_panel;
    private ImageButton edit_button;
    private Button myinventory_button;
    private Button friendinventory_button;

    private Button addfriend_button;

    private TextView username;
    private TextView location;
    private TextView email;
    private TextView phone;


    private Runnable doUpdateGUIDetails = new Runnable() {
        public void run() {
            username.setText(user.getUsername());
            location.setText(user.getLocation());
            email.setText(user.getEmail());
            phone.setText(user.getPhone());
        }
    };

    public TextView getUsername() {
        return username;
    }

    public TextView getLocation() {
        return location;
    }

    public TextView getEmail() {
        return email;
    }

    public TextView getPhone() {
        return phone;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Runnable getDoUpdateGUIDetails() {
        return doUpdateGUIDetails;
    }

    public ImageButton getEdit_button() {
        return edit_button;
    }

    public Button getAddfriend_button() {
        return addfriend_button;
    }

    public Button getFriendinventory_button() {
        return friendinventory_button;
    }

    public Button getMyinventory_button() {
        return myinventory_button;
    }

    public Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        friendsController = new FriendsController(context);
        myprofile_panel = (LinearLayout) findViewById(R.id.myprofile_button_panel);
        stranger_panel = (LinearLayout) findViewById(R.id.stranger_button_panel);
        friend_panel = (LinearLayout) findViewById(R.id.friend_button_panel);

        edit_button = (ImageButton) findViewById(R.id.edit_button);
        myinventory_button = (Button) findViewById(R.id.my_inventory_button);
        friendinventory_button = (Button) findViewById(R.id.friend_inventory_btn);
        addfriend_button = (Button) findViewById(R.id.add_friend_btn);

        username = (TextView) findViewById(R.id.profileName);
        location = (TextView) findViewById(R.id.profileLocation);
        email = (TextView) findViewById(R.id.profileEmail);
        phone = (TextView) findViewById(R.id.profilePhone);
    }

    /**
     * Gets the username that was passed from the previous activity.
     * <p>In this case, it could be from the Friends, Search User,
     * or Main activity. This activity determines which panel the
     * user will see (ie. my profile, friend's profile, or stranger's profile).
     */
    @Override
    protected void onStart() {
        super.onStart();
        friendsController = new FriendsController(context);
        userController = new UserController(context);
        Intent intent = getIntent();

        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                targetUsername = extras.getString("profileTarget");
                setTitle(targetUsername + "'s Profile");
                Thread thread = new GetThread(targetUsername);
                thread.start();
            }
        }

        // Checks to see if we are getting a username from the intent
        if (LoginActivity.USERLOGIN != null) {
            if (!targetUsername.equals(LoginActivity.USERLOGIN.getUsername())) {

                Friends friends = LoginActivity.USERLOGIN.getFriends();

                // If the username is in the user's friend list, show friend profile view
                if (friends.contains(targetUsername)) {
                    myprofile_panel.setVisibility(View.GONE);
                    edit_button.setVisibility(View.GONE);
                    friend_panel.setVisibility(View.VISIBLE);

                    // If not, then show the stranger's profile view
                } else {
                    myprofile_panel.setVisibility(View.GONE);
                    edit_button.setVisibility(View.GONE);
                    stranger_panel.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    /**
     * Called when the user presses the "Inventory" button on the user's Profile page.
     * <p>This method is used to send the user to their own Inventory page.
     *
     * @param view "Inventory" button on the user's Profile page
     */
    public void goToInventory(View view) {
        Intent intent = new Intent(this, MyInventoryActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the user presses the "Inventory" button on a friend's Profile page.
     * <p>This method is used to send the user to a friend's Inventory page and pass
     * the friend's name to the friend's Inventory activity.
     *
     * @param view "Inventory" button on a friend's Profile page
     */
    public void goToFriendInventory(View view) {
        Intent intent = new Intent(context, FriendsInventoryActivity.class);
        intent.putExtra("friend_uname", targetUsername);
        startActivity(intent);
    }

    /**
     * Called when the user presses the "Add Friend" button.
     * <p>This method is used to add a friend to the user's friend list
     * and run the "Update User Thread".
     *
     * @param view "Add Friend" button
     */
    public void addFriend(View view) {
        friendsController.addFriend(targetUsername);
        finish();
    }

    /**
     * Called when the user presses the "Pencil" icon in on their Profile page.
     * <p>This method is used to send the user to Edit Profile page.
     *
     * @param view "Pencil" icon
     */
    public void editProfile(View view) {
        Intent intent = new Intent(context, EditProfileActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the activity starts.
     * <p>This class creates a thread and runs "Get User".
     * The purpose of this class is to update the user's information on their Profile page.
     */
    class GetThread extends Thread {
        private String username;

        public GetThread(String username) {
            this.username = username;
        }

        @Override
        public void run() {
            user = userController.getUser(username);
            runOnUiThread(doUpdateGUIDetails);
        }
    }
}
