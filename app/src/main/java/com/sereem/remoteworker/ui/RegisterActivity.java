package com.sereem.remoteworker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.sereem.remoteworker.R;
import com.sereem.remoteworker.databinding.ActivityRegisterBinding;

import java.util.Objects;

/**
 * RegisterActivity class, used for creating account and saving it to the Firebase Authentication.
 */
public class RegisterActivity extends AppCompatActivity {


    EditText emailEditText, passwordEditText;
    public static final String NEW_EMAIL = "new_email";
    public static final String NEW_PASSWORD = "new_password";
    private ColorPalette colorPalette;
    private Snackbar snackbar;
    private FirebaseAuth fAuth;
    private final String TAG = "Register1";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegisterBinding binding = DataBindingUtil.setContentView(this,
                R.layout.activity_register);
        colorPalette = new ColorPalette(this, binding, ColorPalette.TYPE.REGISTER);
        binding.setColorPalette(colorPalette);
        binding.setLifecycleOwner(this);

        fAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        snackbar = CustomSnackbar.create(findViewById(android.R.id.content));
    }

    public void nextStep(View view) {
        Intent intent = new Intent(this, Register2.class);
        String new_email = emailEditText.getText().toString();
        String new_password = passwordEditText.getText().toString();

        ProgressBar progressBar = findViewById(R.id.progressBarRegister1);
        progressBar.setVisibility(View.VISIBLE);
        fAuth.createUserWithEmailAndPassword(new_email, new_password).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                intent.putExtra(NEW_EMAIL, new_email);
                intent.putExtra(NEW_PASSWORD, new_password);
                startActivity(intent);
                finish();
            } else {
                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (FirebaseAuthWeakPasswordException e) {
                    snackbar.setText(e.getMessage()).show();
                    passwordEditText.requestFocus();
                } catch (FirebaseAuthUserCollisionException | FirebaseAuthInvalidCredentialsException e) {
                    snackbar.setText(e.getMessage()).show();
                    emailEditText.requestFocus();
                } catch (Exception e) {
                    ErrorDialog.show(this);
                    Log.e(TAG, e.toString());
                }
            }
            progressBar.setVisibility(View.INVISIBLE);
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
}