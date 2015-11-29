package com.example.yunita.tradiogc.friends;

import android.content.Context;

import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.user.UserController;

import java.util.Collections;

/**
 * This controller handles adding and deleting friends from a user's friend list.
 */
public class FriendsController {

    private static final String TAG = "FriendsController";
    private UserController userController;
    private Context context;

    /**
     * Class constructor specifying that this controller class is a subclass of Context.
     *
     * @param context
     */
    public FriendsController(Context context) {
        super();
        this.context = context;
        userController = new UserController(context);
    }

    /**
     * Called when the user clicks the "Add Friend" button.
     * <p>This method is used to add a friend to the current login user's friend list
     * and update the user's new friend list to the webserver.
     *
     * @param friendname new friend's name
     */
    public void addFriend(String friendname) {
        LoginActivity.USERLOGIN.getFriends().add(friendname);
        Collections.sort(LoginActivity.USERLOGIN.getFriends(), String.CASE_INSENSITIVE_ORDER);
        Thread updateUserThread = userController.new UpdateUserThread(LoginActivity.USERLOGIN);
        updateUserThread.start();
    }

    /**
     * Called when "Delete Friend Thread" is running
     * <p>This method is used to remove a friend from the current login user's friend list
     * and update the user's new friend list to the webserver.
     *
     * @param friendname friend with this name in the user's friend list
     */
    public void deleteFriend(String friendname) {
        LoginActivity.USERLOGIN.getFriends().remove(friendname);
        System.out.println("login friends = " + LoginActivity.USERLOGIN.getFriends().size());
        Thread updateUserThread = userController.new UpdateUserThread(LoginActivity.USERLOGIN);
        updateUserThread.start();
    }


}
