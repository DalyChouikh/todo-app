package dev.daly.todo_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;

import dev.daly.todo_app.db.DB;
import dev.daly.todo_app.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    EditText username, password;
    MaterialButton loginBtn, registerBtn;
    DB db;

    MaterialCheckBox rememberMe;

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
        rememberMe = binding.rememberMe;

        SharedPreferences sharedPreferences = getSharedPreferences("rememberMe", MODE_PRIVATE);
        if(sharedPreferences.getBoolean("rememberMe", false)){
            SharedPreferences sharedPreferences1 = getSharedPreferences("username", MODE_PRIVATE);
            String usernameStr = sharedPreferences1.getString("username", "");
            if(!usernameStr.isEmpty()){
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("username", usernameStr);
                startActivity(intent);
                finish();
            }
        }else {
            SharedPreferences sharedPreferences1 = getSharedPreferences("username", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences1.edit();
            editor.clear();
            editor.apply();
        }

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
                    SharedPreferences sharedPreferences1 = getSharedPreferences("username", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                    editor.putString("username", usernameStr);
                    editor.apply();
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
        rememberMe.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                SharedPreferences sharedPreferences1 = getSharedPreferences("rememberMe", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putBoolean("rememberMe", true);
                editor.apply();
            }else {
                SharedPreferences sharedPreferences1 = getSharedPreferences("rememberMe", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putBoolean("rememberMe", false);
                editor.apply();
                SharedPreferences sharedPreferences2 = getSharedPreferences("username", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedPreferences2.edit();
                editor1.clear();
                editor1.apply();
            }
        });
        String usernameStr = getIntent().getStringExtra("username");
        String passwordStr = getIntent().getStringExtra("password");
        if(usernameStr != null && passwordStr != null){
            username.setText(usernameStr);
            password.setText(passwordStr);
        }
    }

    boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}