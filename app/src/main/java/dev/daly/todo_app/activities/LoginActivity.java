package dev.daly.todo_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import dev.daly.todo_app.db.DB;
import dev.daly.todo_app.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    EditText username, password;
    MaterialButton loginBtn, registerBtn;
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        username = binding.username;
        password = binding.password;
        loginBtn = binding.signInBtn;
        registerBtn = binding.signUpBtn;
        db = new DB(LoginActivity.this);

        loginBtn.setOnClickListener(v -> {
            String usernameStr = username.getText().toString();
            String passwordStr = password.getText().toString();
            if(!isConnected()){
                Toast.makeText(LoginActivity.this, "❌ No Internet Connection", Toast.LENGTH_SHORT).show();
                return;
            }
            if (usernameStr.isEmpty() || passwordStr.isEmpty()) {
                if (usernameStr.isEmpty()) {
                    username.setError("Username cannot be empty");
                }
                if (passwordStr.isEmpty()) {
                    password.setError("Password cannot be empty");
                }
            } else {
                if (db.checkUsernamePassword(usernameStr, passwordStr)) {
                    Toast.makeText(LoginActivity.this, "✅ Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("username", usernameStr);
                    startActivity(intent);
                    finish();
                } else {
                    username.setError("Invalid Credentials");
                    password.setError("Invalid Credentials");
                }
            }
        });
        registerBtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}