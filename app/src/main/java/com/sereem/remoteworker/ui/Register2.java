package com.sereem.remoteworker.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sereem.remoteworker.R;
import com.sereem.remoteworker.databinding.ActivityRegister2Binding;
import com.sereem.remoteworker.model.User;

import java.util.Objects;

/**
 * Register2 class, used for creating information about the user and saving it into the Firebase
 * Cloudstore
 */

public class Register2 extends AppCompatActivity {

    public static final String NEW_EMAIL = "new_email";
    public static final String NEW_PASSWORD = "new_password";
    private final String TAG = "Register2";
    EditText firstNameEditText, lastNameEditText;
    EditText phoneEditText, birthdayEditText, companyIdEditText;
    String email;
    String password;
    private ColorPalette colorPalette;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private boolean isUserCreated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegister2Binding binding = DataBindingUtil.setContentView(this,
                R.layout.activity_register2);
        colorPalette = new ColorPalette(this, binding, ColorPalette.TYPE.REGISTER2);
        binding.setColorPalette(colorPalette);
        binding.setLifecycleOwner(this);
        isUserCreated = false;
        Intent intent = getIntent();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        email = intent.getStringExtra(NEW_EMAIL);
        password = intent.getStringExtra(NEW_PASSWORD);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);

        phoneEditText = findViewById(R.id.phoneEditText);
        birthdayEditText = findViewById(R.id.birthdayEditText);
        companyIdEditText = findViewById(R.id.idEditText);

    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

    public void addWorker(View view){
        saveUserToFirebase();
    }

    private void saveUserToFirebase() {
        ProgressBar progressBar = findViewById(R.id.progressBarRegister2);
        progressBar.setVisibility(View.VISIBLE);
        String userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);

        User user = User.createUserForSaving(
                fAuth.getCurrentUser().getUid(),
                companyIdEditText.getText().toString(),
                firstNameEditText.getText().toString(),
                lastNameEditText.getText().toString(),
                email,
                phoneEditText.getText().toString(),
                birthdayEditText.getText().toString(),
                null, null, null,
                null, null, null, null);

        documentReference.set(user).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                isUserCreated = true;
                saveInSharedPrefs(email, fAuth.getCurrentUser().getUid());
                Intent intent = MainActivity.makeLaunchIntent(Register2.this, email, password);
                startActivity(intent);
                finish();
            } else {
                fAuth.getCurrentUser().delete();
                ErrorDialog.show(this);
                Log.e(TAG, Objects.requireNonNull(task.getException()).getMessage());
            }
            progressBar.setVisibility(View.INVISIBLE);
        });
    }

    private void saveInSharedPrefs(String email, String id) {
        SharedPreferences prefs = getSharedPreferences("user", Context.MODE_PRIVATE);
        prefs.edit().putString("email", email).putString("UID", id).apply();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isUserCreated) {
            fAuth.getCurrentUser().delete();
            if(fAuth.getCurrentUser() != null)
                fAuth.signOut();
        }
    }
}