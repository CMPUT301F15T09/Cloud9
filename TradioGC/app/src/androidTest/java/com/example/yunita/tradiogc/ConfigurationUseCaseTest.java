package com.example.yunita.tradiogc;

import android.test.ActivityInstrumentationTestCase2;

import com.example.yunita.tradiogc.user.User;

public class ConfigurationUseCaseTest extends ActivityInstrumentationTestCase2 {

    public ConfigurationUseCaseTest() {
        super(com.example.yunita.tradiogc.MainActivity.class);
    }

    // 10.02.01
    public void testEditProfile(){
        User ann = new User();
        ann.setUsername("ann");
        ann.setLocation("edmonton");
        ann.setPhone("7809998881");
        ann.setEmail("ann@yahoo.com");

        // modify location
        ann.setLocation("calgary");

        assertFalse(ann.getLocation().equals("edmonton"));
    }

}
