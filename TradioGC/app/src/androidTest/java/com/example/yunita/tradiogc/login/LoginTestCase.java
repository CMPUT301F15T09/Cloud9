package com.example.yunita.tradiogc.login;

import android.test.ActivityInstrumentationTestCase2;

import com.example.yunita.tradiogc.user.User;
import com.example.yunita.tradiogc.user.Users;

public class LoginTestCase extends ActivityInstrumentationTestCase2 {

    public LoginTestCase() {
        super(com.example.yunita.tradiogc.MainActivity.class);
    }

    /**
     * Use Case 1
     * 11.01.01
     * Test for creating a new user account.
     */
    public void testCreateAccount(){
        User ann = new User();
        ann.setUsername("ann");
        ann.setLocation("edmonton");
        ann.setPhone("7809998881");
        ann.setEmail("ann@yahoo.com");

        Users users = new Users();
        users.add(ann);

        assertTrue(users.contains(ann));
    }

    /**
     * Use Case 2
     * 11.02.01
     * Test for logging into a user account.
     */
    public void testLogin(){
        User ann = new User();
        ann.setUsername("ann");
        ann.setLocation("edmonton");
        ann.setPhone("7809998881");
        ann.setEmail("ann@yahoo.com");

        Users users = new Users();
        users.add(ann);

        assertTrue(users.get(0).getUsername().equals(ann.getUsername()));
    }

}

