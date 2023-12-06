package dev.daly.todo_app.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import org.json.JSONException;

import dev.daly.todo_app.R;
import dev.daly.todo_app.db.DB;
import dev.daly.todo_app.RequestHandler;
import dev.daly.todo_app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    EditText username, password, confirmPassword;
    MaterialButton registerBtn, loginBtn;
    DB db;
    RequestHandler requestHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestHandler = new RequestHandler(MainActivity.this);
        username = binding.username;
        password = binding.password;
        confirmPassword = binding.cPassword;
        registerBtn = binding.signUpBtn;
        loginBtn = binding.signInBtn;
        db = new DB(MainActivity.this);
        registerBtn.setOnClickListener(v -> {
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
                            Toast.makeText(MainActivity.this, "✅ Account registred Successfully", Toast.LENGTH_SHORT).show();
                            username.setText("");
                            password.setText("");
                            confirmPassword.setText("");
                            Log.d("Request", "Sending request");
                            try {
                                requestHandler.createUser(usernameStr, passwordStr);
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.putExtra("username", usernameStr);
                                intent.putExtra("password", passwordStr);
                                startActivity(intent);
                                finish();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "❌ Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    confirmPassword.setError("Passwords do not match");
                }
            }

        }
        );
        loginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();

        });
    }
    boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}