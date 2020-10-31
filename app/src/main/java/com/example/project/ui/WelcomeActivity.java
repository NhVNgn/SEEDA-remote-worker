package com.example.project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        setupSignUpButton();
        setupSignInButton();
    }

    private void setupSignUpButton() {
        Button button = findViewById(R.id.signUpButtonWelcome);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void setupSignInButton() {
        Button button = findViewById(R.id.signInButtonWelcome);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }
}