package dev.daly.todo_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import dev.daly.todo_app.R;
import dev.daly.todo_app.RequestHandler;
import dev.daly.todo_app.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;
    TextView goBack;
    EditText addressIPEditText;
    Button addressIPSaveButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cancelButton = binding.cancelButton;
        addressIPEditText = binding.addressIPEditText;
        addressIPSaveButton = binding.addressIPSaveButton;
        goBack = binding.addressIPTextView;
        addressIPSaveButton.setOnClickListener(v -> {
            String addressIP = addressIPEditText.getText().toString();
            if (addressIP.split("\\.").length != 4) {
                addressIPEditText.setError("Invalid IP Address");
            } else {
                getSharedPreferences("addressIP", MODE_PRIVATE).edit().putString("addressIP", addressIP).apply();
                RequestHandler.ADDRESS = addressIP;
                Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                intent.putExtra("username", getIntent().getStringExtra("username"));
                startActivity(intent);
                finish();
            }
        });

        cancelButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            startActivity(intent);
            finish();
        });

        addressIPEditText.setText(RequestHandler.ADDRESS);
        addressIPEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                addressIPSaveButton.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addressIPSaveButton.setEnabled(!s.toString().isEmpty());
                if (s.toString().isEmpty()) {
                    addressIPSaveButton.setTextColor(getResources().getColor(R.color.darker_gray));
                } else {
                    addressIPSaveButton.setTextColor(getResources().getColor(R.color.purple));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                addressIPSaveButton.setEnabled(!s.toString().isEmpty());
                if (s.toString().isEmpty()) {
                    addressIPSaveButton.setTextColor(getResources().getColor(R.color.darker_gray));
                } else {
                    addressIPSaveButton.setTextColor(getResources().getColor(R.color.purple));
                }
            }
        });
        goBack.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            startActivity(intent);
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
        finish();

        super.onBackPressed();
    }
}