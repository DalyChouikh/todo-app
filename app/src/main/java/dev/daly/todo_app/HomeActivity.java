package dev.daly.todo_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import dev.daly.todo_app.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.signUp.setText("Welcome to Home Activity");
    }
}