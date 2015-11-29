package com.example.yunita.tradiogc.offline;

import android.content.Context;

import com.example.yunita.tradiogc.inventory.Item;
import com.example.yunita.tradiogc.user.User;
import com.example.yunita.tradiogc.user.Users;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by User on 2015-11-29.
 */
public class UserstobeAdded {
    ArrayList<User> newUsers;
    private Context context;
    private Gson gson;

    public UserstobeAdded(Context context) {
        super();
        this.context = context;

        this.newUsers = loadUsersFromFile();
    }

    private void saveUsersInFile(UserstobeAdded users) {
        try {
            FileOutputStream fos = context.openFileOutput("newofflineusers.sav", 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            gson.toJson(users, writer);
            writer.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Users loadUsersFromFile(){
        Users users;
        try{
            FileInputStream fis = context.openFileInput("newofflineusers.sav");
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Item>>() {}.getType();
            users = gson.fromJson(in, listType);
            return users;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
