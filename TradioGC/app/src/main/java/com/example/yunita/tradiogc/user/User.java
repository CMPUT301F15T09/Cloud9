package com.example.yunita.tradiogc.user;

import com.example.yunita.tradiogc.friends.Friends;
import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.notification.Notifications;
import com.example.yunita.tradiogc.trade.Trades;

/**
 * This class defines a user.
 */
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
     * Class constructor for a user.
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
     * @param username name of the user
     */
    public User(String username) {
        this.username = username;
    }

    /**
     * Gets the name of the user.
     *
     * @return username username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Changes the name of the user.
     *
     * @param username user's new name
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the friend list of the user.
     *
     * @return friends friends of the user
     */
    public Friends getFriends() {
        return friends;
    }

    /**
     * Changes the friend list of the user.
     *
     * @param friends user's new list of friends
     */
    public void setFriends(Friends friends) {
        this.friends = friends;
    }

    /**
     * Gets the location (city) where the user lives in.
     *
     * @return location location that the user lives in
     */
    public String getLocation() {
        return location;
    }

    /**
     * Changes the location (city) of the user.
     *
     * @param location user's new location (city)
     */
    public void setLocation(String location) {
        this.location = getLocationFormat(location);
    }

    /**
     * Gets the e-mail address of the user.
     *
     * @return email e-mail address of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Changes the e-mail address of the user.
     *
     * @param email user's new e-mail address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the phone number of the user.
     *
     * @return phone phone number of the user
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Changes the phone number of the user.
     *
     * @param phone user's new phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets the inventory of the user.
     *
     * @return inventory inventory of the user
     */
    public Inventory getInventory() {
        return inventory;
    }
    /**
     * Changes the inventory of the user.
     *
     * @param inventory user's new inventory
     */
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Gets the trades involving the user.
     *
     * @return trades trades involving the user
     */
    public Trades getTrades() {
        return trades;
    }

    /**
     * Changes the trades involving the user.
     *
     * @param trades user's new list of trades involving the user
     */
    public void setTrades(Trades trades) {
        this.trades = trades;
    }

    /**
     * Changes the user's notifications.
     *
     * @return notifications user's notifications
     */
    public Notifications getNotifications() {
        return notifications;
    }

    /**
     * Converts a string to a location-format string
     *
     * @param location  input location string from user
     * @return String   a string with location format
     */
    public String getLocationFormat(String location) {
        String string = "";
        String[] words = location.split(" ");

        for (String word : words) {
            if (word.length() != 0) {
                string += " " + String.valueOf(word.charAt(0)).toUpperCase();
                if (word.length() != 1) {
                    string += word.substring(1);
                }
            }
        }
        if (location.length() != 0) {
            string = string.substring(1);
        }
        return string;
    }

    /**
     * Return the new printing format of the user.
     * <p>The new format of user is: [username].
     *
     * @return String user's username
     */
    @Override
    public String toString() {
        return username;
    }

}
