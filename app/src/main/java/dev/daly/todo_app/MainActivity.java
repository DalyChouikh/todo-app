package dev.daly.todo_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import dev.daly.todo_app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    EditText username, password, confirmPassword;
    MaterialButton registerBtn, loginBtn;
    DB db;



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
        db = new DB(MainActivity.this);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();
                String confirmPasswordStr = confirmPassword.getText().toString();
                if(!isConnected()){
                    Toast.makeText(MainActivity.this, "❌ No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
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
                        if (db.checkUsername(usernameStr)) {
                            username.setError("Username already exists");
                        } else {
                            if (db.insertUser(usernameStr, passwordStr)) {
                                Toast.makeText(MainActivity.this, "✅ User registered Successfully", Toast.LENGTH_SHORT).show();
                                username.setText("");
                                password.setText("");
                                confirmPassword.setText("");
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            } else {
                                Toast.makeText(MainActivity.this, "❌ Registration Failed", Toast.LENGTH_SHORT).show();
                            }
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
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
    }
    boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}