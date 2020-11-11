package com.example.project.ui;
import com.example.project.R;
import com.example.project.model.Database;
import com.example.project.ui.ui_for_main.worksites.SiteDetailActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {


    EditText emailEditText, passwordEditText;
    public static final String NEW_EMAIL = "new_email";
    public static final String NEW_PASSWORD = "new_password";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
    }

    public void nextStep(View view){
        Intent intent = new Intent(this, Register2.class);
        String new_email = emailEditText.getText().toString();
        String new_password = passwordEditText.getText().toString();
        intent.putExtra(NEW_EMAIL, new_email);
        intent.putExtra(NEW_PASSWORD, new_password);
        startActivity(intent);
        finish();
        /*Intent intent = new Intent(globalContext, SiteDetailActivity.class);
        String site_id = userSites.get(position).getSiteId();
        intent.putExtra(PROJECT_ID, site_id);
        saveInSharedPrefs(site_id, root);
        startActivity(intent);*/

    }
}