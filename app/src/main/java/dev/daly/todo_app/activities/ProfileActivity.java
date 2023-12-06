package dev.daly.todo_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

import dev.daly.todo_app.R;
import dev.daly.todo_app.RequestHandler;
import dev.daly.todo_app.databinding.ActivityProfileBinding;
import dev.daly.todo_app.db.DB;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    EditText usernameEditText;
    Button saveButton;
    Button cancelButton;
    Button deleteButton;
    RequestHandler requestHandler;
    TextView goBack;

    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        usernameEditText = binding.usernameEditText;
        saveButton = binding.saveButton;
        cancelButton = binding.cancelButton;
        deleteButton = binding.deleteButton;
        goBack = binding.usernameTextView;
        db = new DB(ProfileActivity.this);
        requestHandler = new RequestHandler(ProfileActivity.this);
        usernameEditText.setText(getIntent().getStringExtra("username"));

        cancelButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            startActivity(intent);
            finish();
        });

        deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete account");
            builder.setMessage("Are you sure you want to delete this account?");
            builder.setPositiveButton(R.string.confirm, (dialog, which) -> {
                db.deleteUser(getIntent().getStringExtra("username"));
                requestHandler.deleteUser(getIntent().getStringExtra("username"));
                SharedPreferences sharedPreferences = getSharedPreferences("rememberMe", MODE_PRIVATE);
                SharedPreferences sharedPreferences1 = getSharedPreferences("username", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                editor.clear();
                editor.apply();
                editor1.clear();
                editor1.apply();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            });
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                dialog.dismiss();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        saveButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            if (username.isEmpty()) {
                usernameEditText.setError("Username cannot be empty");
            } else {
                String oldUsername = getIntent().getStringExtra("username");
                if (!oldUsername.equals(username) && db.checkUsername(username)) {
                    usernameEditText.setError("Username already exists");
                    return;
                }
                db.updateUser(getIntent().getStringExtra("username"), username);
                try {
                    requestHandler.changeUsername(getIntent().getStringExtra("username"), username);
                    SharedPreferences sharedPreferences = getSharedPreferences("username", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username);
                    editor.apply();
                    Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveButton.setEnabled(!s.toString().isEmpty());
                if (s.toString().isEmpty()) {
                    saveButton.setTextColor(getResources().getColor(R.color.darker_gray));
                } else {
                    saveButton.setTextColor(getResources().getColor(R.color.purple));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                saveButton.setEnabled(!s.toString().isEmpty());
                if (s.toString().isEmpty()) {
                    saveButton.setTextColor(getResources().getColor(R.color.darker_gray));
                } else {
                    saveButton.setTextColor(getResources().getColor(R.color.purple));
                }
            }
        });
        db.close();
        goBack.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}