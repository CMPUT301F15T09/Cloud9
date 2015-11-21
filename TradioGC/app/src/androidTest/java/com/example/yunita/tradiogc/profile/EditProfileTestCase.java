package com.example.yunita.tradiogc.profile;

import android.test.ActivityInstrumentationTestCase2;

import com.example.yunita.tradiogc.user.User;

public class EditProfileTestCase extends ActivityInstrumentationTestCase2 {

    public EditProfileTestCase() {
        super(com.example.yunita.tradiogc.MainActivity.class);
    }

    /**
     * Use Case 38
     * 10.02.01
     * Test for editing a user's profile.
     */
    public void testEditProfile(){
        User ann = new User();
        ann.setUsername("ann");
        ann.setLocation("edmonton");
        ann.setPhone("7809998881");
        ann.setEmail("ann@yahoo.com");

        // Modify the location
        ann.setLocation("calgary");

        assertFalse(ann.getLocation().equals("edmonton"));
    }

}
