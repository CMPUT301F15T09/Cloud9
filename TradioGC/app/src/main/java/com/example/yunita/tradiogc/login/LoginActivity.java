package com.example.yunita.tradiogc.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yunita.tradiogc.MainActivity;
import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.user.User;
import com.example.yunita.tradiogc.user.UserController;

public class LoginActivity extends Activity {

    public static boolean STATUS = false;
    public static User USERLOGIN = new User(); // it is not final, so that the value can be altered

    private Context mContext = this;
    private LoginController loginController;
    private UserController userController;

    private LinearLayout login_view;
    private LinearLayout signup_view;
    private EditText username_et;
    private EditText location_et;
    private EditText phone_et;
    private EditText email_et;
    private TextView create_account;
    private Button login;
    private Button signup;

    public TextView getCreate_account() {
        return create_account;
    }

    public EditText getPhone_et() {
        return phone_et;
    }

    public EditText getEmail_et() {
        return email_et;
    }

    public EditText getLocation_et() {
        return location_et;
    }

    public EditText getUsername_et() {
        return username_et;
    }

    public Button getLogin() {
        return login;
    }

    public Button getSignup() {
        return signup;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        overridePendingTransition(0, 0);

        loginController = new LoginController(mContext);
        userController = new UserController(mContext);

        login_view = (LinearLayout) findViewById(R.id.login_view);
        signup_view = (LinearLayout) findViewById(R.id.signUp_view);

        create_account = (TextView) findViewById(R.id.create_account);
        username_et = (EditText) findViewById(R.id.usernameEditText);
        location_et = (EditText) findViewById(R.id.locationEditText);
        phone_et = (EditText) findViewById(R.id.phoneEditText);
        email_et = (EditText) findViewById(R.id.emailEditText);
        login = (Button) findViewById(R.id.loginButton);
        signup = (Button) findViewById(R.id.signUpButton);

        if (STATUS) {
            goToMain();
            finish();
        }
    }

    /**
     * Called when the user presses on "Create Account".
     * <p>This method is used to show the sign up panel on the Login page.
     *
     * @param view "Create Account" text view
     */
    public void goToSignUp(View view) {
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.pull_down);
        login_view.setVisibility(View.GONE);
        signup_view.setVisibility(View.VISIBLE);
        signup_view.startAnimation(anim);
    }

    /**
     * Called when the user presses on "Login".
     * <p>This method is used to show the login panel on the Login page.
     *
     * @param view "Login" text view
     */
    public void goToLogin(View view) {
        signup_view.setVisibility(View.GONE);
        login_view.setVisibility(View.VISIBLE);
    }

    /**
     * Called when the user presses the "Login" or "Sign Up" button.
     * <p>This method is used to send the user to the Main page.
     */
    public void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Called when the user presses the "Login" button.
     * <p>This method is used to check if the username exists in the webserver.
     * If it exists, it stores the user information into USERLOGIN and directs
     * the user to the Main page.
     * Otherwise, it shows an error message.
     *
     * @param view "Login" button
     */
    public void login(View view) {

        final String username = username_et.getText().toString();

        // Execute the thread
        if (!username.equals("")) {
            Thread thread = userController.new GetUserLoginThread(username);
            thread.start();

            synchronized (thread) {
                try {
                    thread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (USERLOGIN == null) {
                    Toast toast = Toast.makeText(mContext, "This username does not exist.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    goToMain();
                }
            }
        }
    }

    /**
     * Called when the user presses the "Sign Up" button.
     * <p>This method is used to check if the username is unique.
     * If the username is unique, it will create a new user.
     * Otherwise, it shows an error message.
     *
     * @param view "Sign Up" button
     */
    public void signUp(View view) {
        String username = username_et.getText().toString();
        String location = location_et.getText().toString();
        String email = email_et.getText().toString();
        String phone = phone_et.getText().toString();

        // Execute the thread
        if (username.equals("")) {
            username_et.setError("Username cannot be empty.");
        } else if (username.contains(" ")) {
            username_et.setError("Username cannot include empty space.");
        } else if (email.equals("")) {
            email_et.setError("Email cannot be empty.");
        } else if (!email.contains("@")) {
            email_et.setError("Email must include \"@\"");
        } else {
            Thread thread = userController.new GetUserLoginThread(username);
            thread.start();

            synchronized (thread) {
                try {
                    thread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (USERLOGIN != null) {
                    Toast toast = Toast.makeText(mContext, "This username already exists.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    try {
                        User newUser = new User();
                        newUser.setUsername(username);
                        newUser.setLocation(location.toUpperCase());
                        newUser.setEmail(email.toLowerCase());
                        newUser.setPhone(phone);
                        newUser.setInventory(new Inventory());

                        USERLOGIN = new User();
                        USERLOGIN.setUsername(username);
                        USERLOGIN.setLocation(location.toUpperCase());
                        USERLOGIN.setEmail(email.toLowerCase());
                        USERLOGIN.setPhone(phone);
                        USERLOGIN.setInventory(new Inventory());

                        // Execute the thread
                        Thread thread2 = loginController.new SignUpThread(newUser);
                        thread2.start();
                        synchronized (thread2) {
                            try {
                                thread2.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        Toast toast = Toast.makeText(mContext, "User account has been created", Toast.LENGTH_SHORT);
                        toast.show();

                        goToMain();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }


    }
}
