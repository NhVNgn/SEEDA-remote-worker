package com.sereem.remoteworker.ui;
import com.sereem.remoteworker.R;
import com.sereem.remoteworker.databinding.ActivityRegister2Binding;
import com.sereem.remoteworker.databinding.ActivityRegisterBinding;
import com.sereem.remoteworker.model.Database;
import com.sereem.remoteworker.model.User;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {


    EditText emailEditText, passwordEditText;
    public static final String NEW_EMAIL = "new_email";
    public static final String NEW_PASSWORD = "new_password";
    Database db;
    private ColorPalette colorPalette;
    private Snackbar snackbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRegisterBinding binding = DataBindingUtil.setContentView(this,
                R.layout.activity_register);
        colorPalette = new ColorPalette(this, binding, ColorPalette.TYPE.REGISTER);
        binding.setColorPalette(colorPalette);
        binding.setLifecycleOwner(this);
        setContentView(R.layout.activity_register);
        db = new Database(this);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        createSnackBar();
    }

    private void createSnackBar() {
        snackbar = Snackbar.make(findViewById(android.R.id.content), "", Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(Color.parseColor("#204E75"))
                .setTextColor(Color.WHITE);
    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void nextStep(View view) {
        Intent intent = new Intent(this, Register2.class);
        String new_email = emailEditText.getText().toString();
        String new_password = passwordEditText.getText().toString();
        if (new_email.isEmpty()) {
            snackbar.setText("Email is empty").show();
            return;
        }
        if (new_password.isEmpty()) {
            snackbar.setText("Password is empty").show();
            return;
        }
        User user = db.getUserByEmail(new_email);
        if (!user.getFirstName().equals("NOT_FOUND")){
            snackbar.setText("This user name is already used").show();

            return;
        }
        if(!isValidMail(new_email)) {
            snackbar.setText("Email is invalid").show();
            return;
        }

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