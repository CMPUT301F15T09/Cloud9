package com.example.yunita.tradiogc.user;

import com.example.yunita.tradiogc.friends.Friends;
import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.notification.Notifications;
import com.example.yunita.tradiogc.trade.Trades;


public class User {

    private String username;
    private String location;
    private String email;
    private String phone;
    private Friends friends;
    private Inventory inventory;
    private Trades trades;
    private Notifications notifications;

    /**
     * Class constructor.
     */
    public User() {
        username = "";
        location = "";
        email = "";
        phone = "";
        friends = new Friends();
        inventory = new Inventory();
        trades = new Trades();
        notifications = new Notifications();

    }

    /**
     * Class constructor specifying the name of the object.
     *
     * @param username contains the name of user.
     */
    public User(String username) {
        this.username = username;
    }

    /**
     * Gets the name of this user.
     *
     * @return username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Changes the name of this user.
     *
     * @param username this user's new name.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the friend list of this user.
     *
     * @return friends.
     */
    public Friends getFriends() {
        return friends;
    }

    /**
     * Changes the friend list of this user.
     *
     * @param friends this user's new friend list.
     */
    public void setFriends(Friends friends) {
        this.friends = friends;
    }

    /**
     * Gets the location (city) where this user lives in.
     *
     * @return location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Changes the location (city) of this user.
     *
     * @param location this user's new location(city).
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the email of this user.
     *
     * @return email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Changes the email of this user.
     *
     * @param email this user's new email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the phone number of this user.
     *
     * @return phone.
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Changes the phone number of this user.
     *
     * @param phone this user's new phone number.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the inventory of this user.
     *
     * @return inventory.
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Changes the inventory of this user.
     *
     * @param inventory this user's new inventory.
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Trades getTrades() {
        return trades;
    }

    public void setTrades(Trades trades) {
        this.trades = trades;
    }

    public Notifications getNotifications() {
        return notifications;
    }

    public void setNotifications(Notifications notifications) {
        this.notifications = notifications;
    }

    /**
     * Return the new printing format of user.
     * <p>The new format of user is [username].
     *
     * @return String user's name.
     */
    @Override
    public String toString() {
        return username;
    }

}
