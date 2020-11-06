package com.example.project.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.model.Database;
import com.example.project.model.User;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEdit, passwordEdit;
    private Button signInButton;
    private TextView incorrectTextView;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdit = findViewById(R.id.emailLogin);
        passwordEdit = findViewById(R.id.passwordLogin);
        incorrectTextView = findViewById(R.id.incorrectTextView);
        signInButton = findViewById(R.id.signInButtonLogin);

        db = new Database(this);

        setupSingInButton();
        setupSignUpButton();
    }

    private void setupSingInButton() {
        signInButton.setOnClickListener(v -> {
            String email = emailEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            User user = db.getUser(email, password);

            if(user.getFirstName().equals("NOT_FOUND")) {
                incorrectTextView.setText(getText(R.string.user_not_found));
            } else if(user.getFirstName().equals("INCORRECT_PASSWORD")) {
                incorrectTextView.setText(getText(R.string.incorrect_password));
            } else {
                Toast.makeText(this, user.getFirstName() + " is found", Toast.LENGTH_SHORT)
                        .show();
                incorrectTextView.setText("");
                
                saveInSharedPrefs(email, password, user.getId());

                Intent intent = MainActivity.makeLaunchIntent(this, email, password);
                startActivity(intent);
                finish();
            }
        });
    }

    private void saveInSharedPrefs(String email, String password, int id) {
        SharedPreferences prefs = getSharedPreferences("user", Context.MODE_PRIVATE);
        prefs.edit().putString("email", email).putString("password", password)
                .putInt("id", id).apply();
    }

    private void setupSignUpButton() {
        Button button = findViewById(R.id.signUpButtonLogin);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }


}