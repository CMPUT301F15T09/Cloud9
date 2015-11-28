package com.example.yunita.tradiogc.friends;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import com.example.yunita.tradiogc.friends.Friends;
import com.example.yunita.tradiogc.user.User;
import com.example.yunita.tradiogc.user.UserController;
import com.example.yunita.tradiogc.user.Users;

public class FriendsTestCase extends ActivityInstrumentationTestCase2 {

    public FriendsTestCase() {
        super(com.example.yunita.tradiogc.MainActivity.class);
    }
    public Context context;

    /**
     * Use Case 10
     * 02.01.01
     * Test for searching a friend's username.
     */
    public void testSearchUsername() {
        // We have 2 users: ann and john
        User doge = new User();
        doge.setUsername("doge");

        UserController search = new UserController(context);
        Users username = search.searchUsers(doge.getUsername());

        assertEquals(username.size(),1);
    }

    /**
     * Use Case 11
     * 02.02.01
     * Test for adding a friend to a user's friend list.
     */
    public void testAddFriend() {
        // We have 2 users: ann and john
        User ann = new User();
        ann.setUsername("ann");
        User john = new User();
        john.setUsername("john");

        // ann adds john to her friend list
        Friends ann_friendlist = ann.getFriends();
        ann_friendlist.add(john.getUsername());

        // Assert that ann has john on her friend list
        assertTrue(ann_friendlist.contains(john.getUsername()));

    }

    /**
     * Use Case 12
     * 02.03.01
     * Test for removing a friend from a user's friend list.
     */
    public void testRemoveFriend() {
        // We have 2 users: ann and john
        User ann = new User();
        ann.setUsername("ann");
        User john = new User();
        john.setUsername("john");

        // ann adds john to her friend list
        Friends ann_friendlist = ann.getFriends();
        ann_friendlist.add(john.getUsername());

        // ann removes john from her friend list
        ann_friendlist.remove(john.getUsername());

        // Assert that ann does not have john on her friend list
        assertFalse(ann_friendlist.contains(john.getUsername()));
    }

    /**
     * Use Case 13
     * 02.04.01
     * Test for viewing a user's personal profile.
     */
    public void testViewPersonalProfile(){
        User me = new User();
        me.setUsername("nathan");
        me.setLocation("Edmonton");
        me.setEmail("nathan@yahoo.com");
        me.setPhone("7809998881");

        assertTrue(me.getUsername().equals("nathan"));
        assertTrue(me.getLocation().equals("Edmonton"));
        assertTrue(me.getEmail().equals("nathan@yahoo.com"));
        assertTrue(me.getPhone().equals("7809998881"));
    }

    /**
     * Use Case 14
     * 02.05.01
     * Test for viewing another user's profile.
     */
    public void testViewOtherProfile() {
        // We have 2 users: ann and john
        User ann = new User();
        ann.setUsername("ann");
        User john = new User();
        john.setUsername("john");
        john.setLocation("edmonton");
        john.setEmail("john@yahoo.com");
        john.setPhone("7803332221");

        // ann adds john to her friend list
        Friends ann_friendlist = ann.getFriends();
        ann_friendlist.add(john.getUsername());

        // Check that john is in ann's friend list
        assertTrue(ann_friendlist.get(0).equals(john.getUsername()));

        // Assert that the friend profile is john's
        User friend_profile = john;
        assertTrue(friend_profile.getUsername().equals("john"));
        assertTrue(friend_profile.getLocation().equals("edmonton"));
        assertTrue(friend_profile.getEmail().equals("john@yahoo.com"));
        assertTrue(friend_profile.getPhone().equals("7803332221"));

    }

    // NEW REQUIREMENTS

    /**
     * Use Case 41
     * 12.01.01
     * Test for viewing the top traders on user's friend list
     */
    public void testViewTopTraders(){

    }

}
