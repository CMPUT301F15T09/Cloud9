package com.example.yunita.tradiogc;

import android.test.ActivityInstrumentationTestCase2;

import com.example.yunita.tradiogc.friends.Friends;
import com.example.yunita.tradiogc.user.User;

public class FriendsUseCaseTest extends ActivityInstrumentationTestCase2 {

    public FriendsUseCaseTest() {
        super(com.example.yunita.tradiogc.MainActivity.class);
    }

    // 02.01.01
    public void testSearchUsername() {
        // we have 2 users: ann and john
        User ann = new User();
        ann.setUsername("ann");
        User john = new User();
        john.setUsername("john");

        // ann adds john to her friendlist
        Friends ann_friendlist = ann.getFriends();
        ann_friendlist.add(john.getUsername());
        // then, automatically john adds anna to his friendlist
        Friends john_friendlist = john.getFriends();
        john_friendlist.add(ann.getUsername());

        assertTrue(ann_friendlist.contains("john"));
    }

    // 02.02.01
    public void testAddFriend() {
        // we have 2 users: ann and john
        User ann = new User();
        ann.setUsername("ann");
        User john = new User();
        john.setUsername("john");

        // ann adds john to her friendlist
        Friends ann_friendlist = ann.getFriends();
        ann_friendlist.add(john.getUsername());
        // then, automatically john adds anna to his friendlist
        Friends john_friendlist = john.getFriends();
        john_friendlist.add(ann.getUsername());

        // Assert that both users have each other on their friend lists
        assertTrue(ann_friendlist.contains(john.getUsername()));
        assertTrue(john_friendlist.contains(ann.getUsername()));

    }

    // 02.03.01
    public void testRemoveFriend() {
        // we have 2 users: ann and john
        User ann = new User();
        ann.setUsername("ann");
        User john = new User();
        john.setUsername("john");

        // ann adds john to her friendlist
        Friends ann_friendlist = ann.getFriends();
        ann_friendlist.add(john.getUsername());
        // then, automatically john adds anna to his friendlist
        Friends john_friendlist = john.getFriends();
        john_friendlist.add(ann.getUsername());

        // ann removes john from her friendlist
        ann_friendlist.remove(john.getUsername());
        john_friendlist.remove(ann.getUsername());

        // Assert that both users doen't have each other on their friend lists
        assertFalse(ann_friendlist.contains(john.getUsername()));
        assertFalse(john_friendlist.contains(ann.getUsername()));
    }

    // 02.04.01
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

    // 02.05.01
    public void testViewOtherProfile() {
        // we have 2 users: ann and john
        User ann = new User();
        ann.setUsername("ann");
        User john = new User();
        john.setUsername("john");
        john.setLocation("edmonton");
        john.setEmail("john@yahoo.com");
        john.setPhone("7803332221");

        // ann adds john to her friendlist
        Friends ann_friendlist = ann.getFriends();
        ann_friendlist.add(john.getUsername());
        // then, automatically john adds anna to his friendlist
        Friends john_friendlist = john.getFriends();
        john_friendlist.add(ann.getUsername());

        // check whether john is ann's friend now
        assertTrue(ann_friendlist.get(0).equals(john.getUsername()));
        User friend_profile = john;
        assertTrue(friend_profile.getUsername().equals("john"));
        assertTrue(friend_profile.getLocation().equals("edmonton"));
        assertTrue(friend_profile.getEmail().equals("john@yahoo.com"));
        assertTrue(friend_profile.getPhone().equals("7803332221"));

    }

}
