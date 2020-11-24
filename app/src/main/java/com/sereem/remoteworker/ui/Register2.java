package com.sereem.remoteworker.ui;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sereem.remoteworker.R;
import com.sereem.remoteworker.databinding.ActivityRegister2Binding;
//import com.sereem.remoteworker.model.Database;
import com.sereem.remoteworker.model.User;
import com.sereem.remoteworker.model.siteAttendance.attendanceDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
//    Database db;
    attendanceDatabase attendDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegister2Binding binding = DataBindingUtil.setContentView(this,
                R.layout.activity_register2);
        colorPalette = new ColorPalette(this, binding, ColorPalette.TYPE.REGISTER2);
        binding.setColorPalette(colorPalette);
        binding.setLifecycleOwner(this);
//        db = new Database(this);
        attendDb = new attendanceDatabase(this);
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
//        List<String> argsArray = new ArrayList<>();
//        argsArray.add(firstNameEditText.getText().toString());
//        argsArray.add(lastNameEditText.getText().toString());
//        argsArray.add(email);
//        argsArray.add(password);
//        argsArray.add(phoneEditText.getText().toString());
//        argsArray.add(birthdayEditText.getText().toString());
//        argsArray.add(companyIdEditText.getText().toString());
//        argsArray.add(null);
//        argsArray.add(null);
//        argsArray.add(null);
//        argsArray.add(null);
//        argsArray.add(null);
//        argsArray.add(null);
//
//
//
//        long id = db.insertData(argsArray);
//        if (id < 0)
//        {
//            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            attendDb.addSample(email, "burnaby123");
//            attendDb.addSample(email, "surrey123");
//            attendDb.addSample(email, "portmoody123");
//            attendDb.addSample(email, "coquitlam123");
//            attendDb.addSample(email, "van123");
//
//
//        }
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
                null, null, null);

        documentReference.set(user).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Intent intent = MainActivity.makeLaunchIntent(Register2.this, email, password);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(Register2.this, "success", Toast.LENGTH_SHORT).show();
                Log.e(TAG, Objects.requireNonNull(task.getException()).getMessage());
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