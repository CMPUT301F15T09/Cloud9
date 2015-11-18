package com.example.yunita.tradiogc;

import android.test.ActivityInstrumentationTestCase2;

import com.example.yunita.tradiogc.user.User;
import com.example.yunita.tradiogc.user.Users;

public class UserUseCaseTest extends ActivityInstrumentationTestCase2 {

    public UserUseCaseTest() {
        super(com.example.yunita.tradiogc.MainActivity.class);
    }

    // UC 1
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

    // UC 2
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

