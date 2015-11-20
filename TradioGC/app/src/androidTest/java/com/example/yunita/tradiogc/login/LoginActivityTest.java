package com.example.yunita.tradiogc.login;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yunita.tradiogc.profile.ProfileActivity;
import com.example.yunita.tradiogc.user.User;
import com.example.yunita.tradiogc.user.UserController;

public class LoginActivityTest extends ActivityInstrumentationTestCase2 {
    private LoginActivity loginActivity;
    private User user;

    public LoginActivityTest() {
        super(com.example.yunita.tradiogc.login.LoginActivity.class);
    }

    /**
     * Use Case 1
     * test for creating a new account
     */
    // Remember to delete the account after finishing this test every time
    // curl -XDELETE "<URL/testAccount>"
    public void testSignup() {
        // start a LoginActivity
        loginActivity = (LoginActivity) getActivity();

        // go to sign up view, edit user info, and click "sign up" button
        loginActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView createAccount = loginActivity.getCreate_account();
                createAccount.performClick();
                EditText username = loginActivity.getUsername_et();
                username.setText("testAccount");
                EditText location = loginActivity.getLocation_et();
                location.setText("testLocation");
                EditText phone = loginActivity.getPhone_et();
                phone.setText("testPhone");
                EditText email = loginActivity.getEmail_et();
                email.setText("testEmail@");
                Button signup = loginActivity.getSignup();
                signup.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();

        // get user by username
        Thread getUserThread = new GetUserThread("testAccount");
        getUserThread.start();
        synchronized (getUserThread) {
            try {
                getUserThread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // check user info
        assertEquals(user.getUsername(), "testAccount");
        assertEquals(user.getLocation(), "TESTLOCATION");
        assertEquals(user.getPhone(), "testPhone");
        assertEquals(user.getEmail(), "testemail@");
    }

    /**
     * Use Case 2
     * test for login
     */
    public void testLogin() {
        // start a LoginActivity
        loginActivity = (LoginActivity) getActivity();

        // input "test" as username, and click "log in" button
        loginActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditText username = loginActivity.getUsername_et();
                username.setText("test");
                Button login = loginActivity.getLogin();
                login.performClick();
            }
        });
        getInstrumentation().waitForIdleSync();


        // check login user info
        assertEquals(LoginActivity.USERLOGIN.getUsername(), "test");
    }


    public class GetUserThread extends Thread {
        private String username;

        public GetUserThread(String username) {
            this.username = username;
        }

        @Override
        public void run() {
            synchronized (this) {
                UserController userController = new UserController(getActivity());
                user = userController.getUser(username);
                notify();
            }
        }
    }
}