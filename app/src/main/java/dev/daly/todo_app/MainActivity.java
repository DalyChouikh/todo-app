package dev.daly.todo_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;

import dev.daly.todo_app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    EditText username, password, confirmPassword;
    MaterialButton registerBtn, loginBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        username = binding.username;
        password = binding.password;
        confirmPassword = binding.cPassword;
        registerBtn = binding.signUpBtn;
        loginBtn = binding.signInBtn;

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();
                String confirmPasswordStr = confirmPassword.getText().toString();
                if (usernameStr.isEmpty() || passwordStr.isEmpty() || confirmPasswordStr.isEmpty()) {
                    if (usernameStr.isEmpty()) {
                        username.setError("Username cannot be empty");
                    }
                    if (passwordStr.isEmpty()) {
                        password.setError("Password cannot be empty");
                    }
                    if (confirmPasswordStr.isEmpty()) {
                        confirmPassword.setError("Confirm Password cannot be empty");
                    }
                } else {
                    if (passwordStr.equals(confirmPasswordStr)) {
                        DB db = new DB(MainActivity.this);
                        if (!db.checkUsername(usernameStr)) {
                            db.insertUser(usernameStr, passwordStr);
                            username.setText("");
                            password.setText("");
                            confirmPassword.setText("");
                        } else {
                            username.setError("Username already exists");
                        }
                    } else {
                        confirmPassword.setError("Passwords do not match");
                    }
                }
            }
            }
        );
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}