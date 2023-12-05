package dev.daly.todo_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import dev.daly.todo_app.R;
import dev.daly.todo_app.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}