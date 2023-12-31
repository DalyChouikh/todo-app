package dev.daly.todo_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


import com.android.volley.NoConnectionError;

import org.json.JSONException;

import java.net.UnknownHostException;

import dev.daly.todo_app.AddTask;
import dev.daly.todo_app.AddressIPSetter;
import dev.daly.todo_app.DialogInterface;
import dev.daly.todo_app.RequestHandler;
import dev.daly.todo_app.databinding.ActivitySplashBinding;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;
    RequestHandler requestHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestHandler = new RequestHandler(this);
        SharedPreferences sharedPreferences = getSharedPreferences("addressIP", MODE_PRIVATE);
        String addressIP = sharedPreferences.getString("addressIP", "");
        if (addressIP.isEmpty()) {
            requestHandler.showAddressIPSetter(this);
        } else {
            Log.d("Try", "Not Here");
            requestHandler.getUsers(addressIP, this);
        }
    }

}