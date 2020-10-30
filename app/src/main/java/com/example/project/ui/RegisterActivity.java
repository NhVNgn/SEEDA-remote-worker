package com.example.project.ui;
import com.example.project.R;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {


    EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText;
    EditText phoneEditText, birthdayEditText, companyIdEditText;
    EditText emFirstNameEditText, emLastNameEditText, emPhoneEditText, relationEditText;
    Database db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        phoneEditText = findViewById(R.id.phoneEditText);
        birthdayEditText = findViewById(R.id.birthdayEditText);
        companyIdEditText = findViewById(R.id.idEditText);

        emFirstNameEditText = findViewById(R.id.eFirstNameEditText);
        emLastNameEditText = findViewById(R.id.eLastNameEditText);
        emPhoneEditText = findViewById(R.id.ePhoneEditText);
        relationEditText = findViewById(R.id.relationEdittext);


        db = new Database(this);
    }

    public void addWorker(View view){
        List<String> argsArray = new ArrayList<>();
        argsArray.add(firstNameEditText.getText().toString());
        argsArray.add(lastNameEditText.getText().toString());
        argsArray.add(emailEditText.getText().toString());
        argsArray.add(passwordEditText.getText().toString());
        argsArray.add(phoneEditText.getText().toString());
        argsArray.add(birthdayEditText.getText().toString());
        argsArray.add(companyIdEditText.getText().toString());
        argsArray.add(emFirstNameEditText.getText().toString());
        argsArray.add(emLastNameEditText.getText().toString());
        argsArray.add(emPhoneEditText.getText().toString());
        argsArray.add(relationEditText.getText().toString());

        long id = db.insertData(argsArray);
        if (id < 0)
        {
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();

        }

    }
}