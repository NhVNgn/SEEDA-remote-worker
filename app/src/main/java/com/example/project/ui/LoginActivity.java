package com.example.project.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.model.Database;
import com.example.project.model.User;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEdit, passwordEdit;
    private Button signInButton;
    private TextView incorrectEmailText, incorrectPasswordText;
    private Database db;
    private Snackbar snackbar;

    private Drawable emailRed, emailBlue, lockRed, lockBlue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdit = findViewById(R.id.emailLogin);
        passwordEdit = findViewById(R.id.passwordLogin);
        incorrectEmailText = findViewById(R.id.incorrectEmailText);
        incorrectPasswordText = findViewById(R.id.incorrectPasswordText);
        signInButton = findViewById(R.id.signInButtonLogin);

        db = new Database(this);

        emailBlue = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_email_blue, null);
        emailRed = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_email_red, null);
        lockBlue = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_lock_blue, null);
        lockRed = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_lock_red, null);

        setCustomTheme();

        setupSingInButton();
        setupSignUpButton();
        setupFocusListener();
        createSnackBar();
    }

    private void createSnackBar() {
        snackbar = Snackbar.make(getWindow().getDecorView(), "", Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.parseColor("#204E75"))
                .setTextColor(Color.WHITE);
    }

    private void setCustomTheme() {
        ColorDrawable color = new ColorDrawable(getColor(R.color.main_blue_color));
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(color);
    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void setupSingInButton() {
        signInButton.setOnClickListener(v -> {
            String email = emailEdit.getText().toString();
            String password = passwordEdit.getText().toString();

            if(!isValidMail(email)) {
                snackbar.setText(getString(R.string.invalid_email)).show();
            }

            User user = db.getUser(email, password);

            if(user.getFirstName().equals("NOT_FOUND")) {
                incorrectEmailText.setText(getText(R.string.user_not_found));
                setIncorrectEmail();
            } else if(user.getFirstName().equals("INCORRECT_PASSWORD")) {
                incorrectPasswordText.setText(getText(R.string.incorrect_password));
                setIncorrectPassword();
            } else {
                Toast.makeText(this, user.getFirstName() + " is found", Toast.LENGTH_SHORT)
                        .show();
                incorrectEmailText.setText("");

                saveInSharedPrefs(email, password, user.getId());

                Intent intent = MainActivity.makeLaunchIntent(this, email, password);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setIncorrectEmail() {
        emailEdit.setCompoundDrawablesWithIntrinsicBounds(emailRed,
                null, null, null);
        emailEdit.setBackgroundResource(R.drawable.edit_text_incorrect);
        emailEdit.setTextColor(getColor(R.color.red_incorrect));
    }

    private void setIncorrectPassword() {
        passwordEdit.setCompoundDrawablesWithIntrinsicBounds(lockRed,
                null, null, null);
        passwordEdit.setBackgroundResource(R.drawable.edit_text_incorrect);
        passwordEdit.setTextColor(getColor(R.color.red_incorrect));
    }

    private void setupFocusListener() {
        emailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailEdit.setCompoundDrawablesWithIntrinsicBounds(emailBlue,
                        null, null, null);
                emailEdit.setBackgroundResource(R.drawable.rounded_corners_edit_text);
                emailEdit.setTextColor(getColor(R.color.main_blue_color));
                incorrectEmailText.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordEdit.setCompoundDrawablesWithIntrinsicBounds(lockBlue,
                        null, null, null);
                passwordEdit.setBackgroundResource(R.drawable.rounded_corners_edit_text);
                passwordEdit.setTextColor(getColor(R.color.main_blue_color));
                incorrectPasswordText.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

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