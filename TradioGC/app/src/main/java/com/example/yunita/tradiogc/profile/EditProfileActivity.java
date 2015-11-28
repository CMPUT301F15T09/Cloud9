package com.example.yunita.tradiogc.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.apache.commons.lang3.text.WordUtils;
import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.user.UserController;

public class EditProfileActivity extends AppCompatActivity {
    private EditText location_et;
    private EditText email_et;
    private EditText phone_et;
    private TextView username;
    private Button save;
    private Context context = this;

    public EditText getPhone_et() {
        return phone_et;
    }

    public EditText getLocation_et() {
        return location_et;
    }

    public EditText getEmail_et() {
        return email_et;
    }

    public TextView getUsername() {
        return username;
    }

    public Button getSave() {
        return save;
    }

    public Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        username = (TextView) findViewById(R.id.profileName);
        location_et = (EditText) findViewById(R.id.location_et);
        email_et = (EditText) findViewById(R.id.email_et);
        phone_et = (EditText) findViewById(R.id.phone_et);
        save = (Button) findViewById(R.id.save_profile_button);
    }

    /**
     * Sets the view with the current user profile.
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (LoginActivity.USERLOGIN != null) {
            username.setText(LoginActivity.USERLOGIN.getUsername());
            location_et.setText(LoginActivity.USERLOGIN.getLocation());
            location_et.setSelection(LoginActivity.USERLOGIN.getLocation().length());
            email_et.setText(LoginActivity.USERLOGIN.getEmail());
            phone_et.setText(LoginActivity.USERLOGIN.getPhone());
        }
    }

    /**
     * Called when the user clicks the "Save" button in the Edit Profile page.
     * This method is used to run the "Update User Thread", and closes
     * the activity after the thread is done updating the user information
     * into the webserver.
     *
     * @param view "Save" button
     */
    public void save(View view) {
        String location = location_et.getText().toString();
        String email = email_et.getText().toString();
        String phone = phone_et.getText().toString();

        //TODO: remove wordutil
        LoginActivity.USERLOGIN.setLocation(WordUtils.capitalizeFully(location));
        LoginActivity.USERLOGIN.setEmail(email.toLowerCase());
        LoginActivity.USERLOGIN.setPhone(phone);

        UserController userController = new UserController(context);
        Thread updateUserThread = userController.new UpdateUserThread(LoginActivity.USERLOGIN);
        updateUserThread.start();
        synchronized (updateUserThread) {
            try {
                updateUserThread.wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        finish();
    }
}
