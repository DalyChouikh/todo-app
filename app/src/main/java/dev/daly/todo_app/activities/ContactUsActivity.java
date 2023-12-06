package dev.daly.todo_app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import dev.daly.todo_app.databinding.ActivityContactUsBinding;

public class ContactUsActivity extends AppCompatActivity {

    private final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private final String EMAIL = "chouikhdaly215@gmail.com";

    ActivityContactUsBinding binding;
    EditText email;
    EditText message;
    TextView goBack;
    Button sendEmail;
    ImageView facebook;
    ImageView instagram;
    ImageView github;
    ImageView linkedin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactUsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        facebook = binding.facebookImageView;
        instagram = binding.instagramImageView;
        github = binding.githubImageView;
        linkedin = binding.linkedinImageView;
        goBack = binding.contactUsTitle;
        sendEmail = binding.sendButton;
        email = binding.emailEditText;
        message = binding.messageEditText;

        sendEmail.setOnClickListener(v -> {
            if (message.getText().toString().isEmpty()) {
                message.setError("Message is required");
            } else if (email.getText().toString().matches(emailPattern)) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{EMAIL});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Task Flow - Support");
                intent.putExtra(Intent.EXTRA_TEXT, message.getText().toString());
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent, "Choose an email client"));
            } else {
                email.setError("Invalid email address");
            }

        });


        facebook.setOnClickListener(v -> {
            String url = "https://www.facebook.com/chouikh.daly";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        instagram.setOnClickListener(v -> {
            String url = "https://www.instagram.com/dali.chouikh/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        github.setOnClickListener(v -> {
            String url = "https://www.github.com/DalyChouikh";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        linkedin.setOnClickListener(v -> {
            String url = "https://www.linkedin.com/in/mohamedali-chouikh/";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        goBack.setOnClickListener(v -> {
            Intent intent = new Intent(ContactUsActivity.this, HomeActivity.class);
            intent.putExtra("username", getIntent().getStringExtra("username"));
            startActivity(intent);
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ContactUsActivity.this, HomeActivity.class);
        intent.putExtra("username", getIntent().getStringExtra("username"));
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

}
