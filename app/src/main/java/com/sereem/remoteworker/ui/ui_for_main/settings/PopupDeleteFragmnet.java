package com.sereem.remoteworker.ui.ui_for_main.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

import com.sereem.remoteworker.R;
import com.sereem.remoteworker.databinding.PopupOptionsLayoutBinding;
import com.sereem.remoteworker.model.Database;
import com.sereem.remoteworker.model.siteAttendance.attendanceDatabase;
import com.sereem.remoteworker.ui.LoginActivity;

public class PopupDeleteFragmnet  extends AppCompatDialogFragment {

    //private ColorPalette colorPalette;
    String correct_password;
    String correct_email;
    EditText passwordConfirmEditText;
    Database db;
    attendanceDatabase attendDatabase;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.popup_delete_option, null);
        //PopupOptionsLayoutBinding binding = PopupOptionsLayoutBinding.bind(v);
        //colorPalette = new ColorPalette(getContext(), binding, ColorPalette.TYPE.POPUP);
        //binding.setColorPalette(colorPalette);
        passwordConfirmEditText = v.findViewById(R.id.deletePasswordEditText);
        db = new Database(v.getContext());
        attendDatabase = new attendanceDatabase(v.getContext());
        DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String entered_password = passwordConfirmEditText.getText().toString();
                if (isPasswordMatch(entered_password))
                {
                    db.deleteRow(correct_email);
                    attendDatabase.deleteRow(correct_email);

                    Toast.makeText(getContext(), "YOUR ACCOUNT IS DELETED", Toast.LENGTH_SHORT).show();
                    SharedPreferences prefs = getContext()
                            .getSharedPreferences("user", Context.MODE_PRIVATE);
                    prefs.edit().putString("username", null).putString("password", null)
                            .putInt("id", -1).apply();
                    Intent intent = LoginActivity.makeIntent(v.getContext());
                    startActivity(intent);
                    getActivity().finish();
                }
                else
                    Toast.makeText(getContext(), "WRONG PASSWORD", Toast.LENGTH_SHORT).show();
            }
        };

        DialogInterface.OnClickListener backListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        };


        return new AlertDialog.Builder(getActivity())
                .setTitle("Are you sure you want to delete your account?")
                .setView(v)
                .setPositiveButton("DELETE", deleteListener)
                .setNegativeButton("BACK", backListener)
                .create();
    }
    public void showDialog(FragmentManager manager){
        this.show(manager, "Popup message is open");
    }

    @Override
    public void onPause() {
        super.onPause();
        //colorPalette.unregisterListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        //colorPalette.registerListener();
    }

    public boolean isPasswordMatch(String enteredPassword){
        SharedPreferences prefs = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        correct_password = prefs.getString("password", "NONE");
        correct_email = prefs.getString("email", "NONE");
        return enteredPassword.equals(correct_password);
    }

}
