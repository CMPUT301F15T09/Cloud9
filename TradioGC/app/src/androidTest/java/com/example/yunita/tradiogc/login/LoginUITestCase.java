package com.example.yunita.tradiogc.login;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yunita.tradiogc.user.User;
import com.example.yunita.tradiogc.user.UserController;

public class LoginUITestCase extends ActivityInstrumentationTestCase2 {
    private LoginActivity loginActivity;
    private User user;

    public LoginUITestCase() {
        super(com.example.yunita.tradiogc.login.LoginActivity.class);
    }

    /**
     * Use Case 1
     * 11.01.01
     * Test for creating a new user account.
     */
    // Remember to delete the account after finishing this test
    // curl -XDELETE "<URL/testAccount>"
    public void testCreateAccountUI() {
        // Start a LoginActivity
        loginActivity = (LoginActivity) getActivity();

        // Go to the sign up view, edit the user information, and click the "sign up" button
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

        // Get the user by username
        Thread getUserThread = new GetUserThread("testAccount");
        getUserThread.start();
        synchronized (getUserThread) {
            try {
                getUserThread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Check user info
        assertEquals(user.getUsername(), "testAccount");
        assertEquals(user.getLocation(), "TESTLOCATION");
        assertEquals(user.getPhone(), "testPhone");
        assertEquals(user.getEmail(), "testemail@");
    }

    /**
     * Use Case 2
     * 11.02.01
     * Test for logging into a user account.
     */
    public void testLoginUI() {
        // Start a LoginActivity
        loginActivity = (LoginActivity) getActivity();

        // Input "test" as the username and click the "log in" button
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


        // Check the login user information
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