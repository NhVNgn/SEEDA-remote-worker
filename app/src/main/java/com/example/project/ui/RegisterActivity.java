package com.example.project.ui;
import com.example.project.R;
import com.example.project.databinding.ActivityRegister2Binding;
import com.example.project.databinding.ActivityRegisterBinding;
import com.example.project.model.Database;
import com.example.project.ui.ui_for_main.worksites.SiteDetailActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

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
    private ColorPalette colorPalette;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegisterBinding binding = DataBindingUtil.setContentView(this,
                R.layout.activity_register);
        colorPalette = new ColorPalette(this, binding, ColorPalette.TYPE.REGISTER);
        binding.setColorPalette(colorPalette);
        binding.setLifecycleOwner(this);

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