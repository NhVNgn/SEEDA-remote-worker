package com.example.project.ui;

import com.example.project.R;
import com.example.project.model.Database;
import com.example.project.model.User;
import com.example.project.model.siteAttendance.attendanceDatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Register2 extends AppCompatActivity {

    public static final String NEW_EMAIL = "new_email";
    public static final String NEW_PASSWORD = "new_password";
    EditText firstNameEditText, lastNameEditText;
    EditText phoneEditText, birthdayEditText, companyIdEditText;
    String email;
    String password;

    Database db;
    attendanceDatabase attendDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        db = new Database(this);
        attendDb = new attendanceDatabase(this);
        Intent intent = getIntent();
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
        List<String> argsArray = new ArrayList<>();
        argsArray.add(firstNameEditText.getText().toString());
        argsArray.add(lastNameEditText.getText().toString());
        argsArray.add(email);
        argsArray.add(password);
        argsArray.add(phoneEditText.getText().toString());
        argsArray.add(birthdayEditText.getText().toString());
        argsArray.add(companyIdEditText.getText().toString());
        argsArray.add("");
        argsArray.add("");
        argsArray.add("");
        argsArray.add("");
        argsArray.add("");
        argsArray.add("");

        long id = db.insertData(argsArray);
        if (id < 0)
        {
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            attendDb.addSample(email, "burnaby123");
            attendDb.addSample(email, "surrey123");
            attendDb.addSample(email, "portmoody123");
            attendDb.addSample(email, "coquitlam123");
            attendDb.addSample(email, "van123");
            
            User user = db.getUser(email, password);
            saveInSharedPrefs(email, password, user.getId());

            Intent intent = MainActivity.makeLaunchIntent(this, email, password);
            startActivity(intent);
            finish();
        }
    }

    private void saveInSharedPrefs(String email, String password, int id) {
        SharedPreferences prefs = getSharedPreferences("user", Context.MODE_PRIVATE);
        prefs.edit().putString("email", email).putString("password", password)
                .putInt("id", id).apply();
    }
}