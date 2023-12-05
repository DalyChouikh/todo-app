package dev.daly.todo_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;


import dev.daly.todo_app.AddTask;
import dev.daly.todo_app.AddressIPSetter;
import dev.daly.todo_app.DialogInterface;
import dev.daly.todo_app.RequestHandler;
import dev.daly.todo_app.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity implements DialogInterface {

    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences sharedPreferences = getSharedPreferences("addressIP", MODE_PRIVATE);
        String addressIP = sharedPreferences.getString("addressIP", "");
        if(addressIP.isEmpty()){
            showAddressIPSetter();
        }else {
            RequestHandler.ADDRESS = addressIP;
            final Intent intent = new Intent(this, LoginActivity.class);
            new Handler().postDelayed(() -> {
                startActivity(intent);
                finish();
            }, 1000);
        }
    }

    private void showAddressIPSetter() {
        AddressIPSetter addressIPSetter = AddressIPSetter.newInstance();
        addressIPSetter.show(getSupportFragmentManager(), AddressIPSetter.TAG);
    }

    @Override
    public void handleDialogClose(android.content.DialogInterface dialogInterface) {
        final Intent intent = new Intent(this, LoginActivity.class);
        new Handler().postDelayed(() -> {
            startActivity(intent);
            finish();
        }, 1000);
    }
}