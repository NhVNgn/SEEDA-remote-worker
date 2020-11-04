package com.example.project.ui.ui_for_main.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.project.R;
import com.example.project.model.Database;
import com.example.project.model.User;

public class ProfileFragment extends Fragment {

    User user;
    Database db;

    EditText emailEdit, passwordEdit;
    Button editBtn, cancelBtn;

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);

        db = new Database(root.getContext());

        getUser();

        setupEditButton();
        updateUI();

        return root;
    }

    private void getUser() {
        SharedPreferences prefs = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        String email = prefs.getString("email", "NONE");
        String password = prefs.getString("password", "NONE");

        user = db.getUser(email, password);
    }

    private void setupEditButton() {
        cancelBtn = root.findViewById(R.id.cancelButtonProfile);
        editBtn = root.findViewById(R.id.editButtonProfile);

        editBtn.setOnClickListener(v -> {
            cancelBtn.setVisibility(View.VISIBLE);
            emailEdit.setFocusable(true);
            emailEdit.setFocusableInTouchMode(true);
            emailEdit.setBackgroundResource(android.R.drawable.edit_text);
            editBtn.setText("Save");
        });
    }

    private void updateUI() {
        emailEdit = root.findViewById(R.id.emailProfileEdit);
        passwordEdit = root.findViewById(R.id.passwordProfileEdit);

        emailEdit.setText(user.getEmail());
        passwordEdit.setText(user.getPassword());
    }


}