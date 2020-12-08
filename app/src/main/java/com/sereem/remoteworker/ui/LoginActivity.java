package com.sereem.remoteworker.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.sereem.remoteworker.R;
import com.sereem.remoteworker.databinding.ActivityLoginBinding;
//import com.sereem.remoteworker.model.Database;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEdit, passwordEdit;
    private Button signInButton;
    private TextView incorrectEmailText, incorrectPasswordText;
//    private Database db;
    private FirebaseAuth fAuth;

    private Drawable emailRed, emailBlue, lockRed, lockBlue;

    private ColorPalette colorPalette;

    ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        if(sharedPreferences != null && !sharedPreferences.getString("UID", "").equals("")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        binding = DataBindingUtil.setContentView
                (this, R.layout.activity_login);
        colorPalette = new ColorPalette(this, binding, ColorPalette.TYPE.LOGIN);
        binding.setColorPalette(colorPalette);
        binding.setLifecycleOwner(this);

        emailEdit = findViewById(R.id.emailLogin);
        passwordEdit = findViewById(R.id.passwordLogin);
        incorrectEmailText = findViewById(R.id.incorrectEmailText);
        incorrectPasswordText = findViewById(R.id.incorrectPasswordText);
        signInButton = findViewById(R.id.signInButtonLogin);

//        db = new Database(this);

        emailBlue = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_email_blue_login, null);
        emailRed = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_email_red, null);
        lockBlue = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_lock_blue, null);
        lockRed = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_lock_red, null);


        setCustomTheme();

        setupSingInButton();
        setupSignUpButton();
        setupFocusListener();
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

            if(email == null || email.equals("")) {
                incorrectEmailText.setText(R.string.email_empty);
                setIncorrectEmail();
                return;
            }
            if(password == null || password.equals("")) {
                incorrectPasswordText.setText(R.string.password_empty);
                setIncorrectPassword();
                return;
            }

//            User user = db.getUser(email, password);

//            if(user.getFirstName().equals("NOT_FOUND")) {
//                incorrectEmailText.setText(getText(R.string.user_not_found));
//                setIncorrectEmail();
//            } if(user.getFirstName().equals("INCORRECT_PASSWORD")) {
//                incorrectPasswordText.setText(getText(R.string.incorrect_password));
//                setIncorrectPassword();
//            }

            ProgressBar progressBar = findViewById(R.id.progressBarLogin);
            progressBar.setVisibility(View.VISIBLE);

            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    incorrectEmailText.setText("");

                    saveInSharedPrefs(email, fAuth.getCurrentUser().getUid());

                    Intent intent = MainActivity.makeLaunchIntent(LoginActivity.this, email, password);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        throw Objects.requireNonNull(task.getException());
                    } catch (FirebaseAuthInvalidUserException e) {
                        incorrectEmailText.setText(getString(R.string.user_not_found));
                        setIncorrectEmail();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        incorrectPasswordText.setText(getString(R.string.incorrect_password));
                        setIncorrectPassword();
                    } catch (Exception e) {
                        ErrorDialog.show(this);
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
            });
        });
    }

    private void setIncorrectEmail() {
        emailEdit.setCompoundDrawablesWithIntrinsicBounds(emailRed,
                null, null, null);
        emailEdit.setBackgroundResource(R.drawable.edit_text_incorrect);
        emailEdit.setTextColor(getColor(R.color.red_incorrect));
        emailEdit.requestFocus();
    }

    private void setIncorrectPassword() {
        passwordEdit.setCompoundDrawablesWithIntrinsicBounds(lockRed,
                null, null, null);
        passwordEdit.setBackgroundResource(R.drawable.edit_text_incorrect);
        passwordEdit.setTextColor(getColor(R.color.red_incorrect));
        passwordEdit.requestFocus();
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
                emailEdit.setBackgroundResource(R.drawable.edit_text_light);
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
                passwordEdit.setBackgroundResource(R.drawable.edit_text_light);
                passwordEdit.setTextColor(getColor(R.color.main_blue_color));
                incorrectPasswordText.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void saveInSharedPrefs(String email, String id) {
        SharedPreferences prefs = getSharedPreferences("user", Context.MODE_PRIVATE);
        prefs.edit().putString("email", email).putString("UID", id).apply();
    }

    private void setupSignUpButton() {
        Button button = findViewById(R.id.signUpButtonLogin);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        colorPalette.unregisterListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        colorPalette.registerListener();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }
}