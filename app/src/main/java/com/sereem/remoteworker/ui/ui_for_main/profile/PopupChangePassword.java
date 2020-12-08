package com.sereem.remoteworker.ui.ui_for_main.profile;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

import com.sereem.remoteworker.R;
import com.sereem.remoteworker.model.Database;
import com.sereem.remoteworker.model.siteAttendance.attendanceDatabase;
import com.sereem.remoteworker.ui.LoginActivity;

/**
 * PopupChangePassword, used for changing user's password
 */
public class PopupChangePassword extends AppCompatDialogFragment {
    //private ColorPalette colorPalette;
    TextView dialogueMessage, emailText;
    ProgressBar progressBar;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.popup_change_password, null);

        dialogueMessage = v.findViewById(R.id.changeMessage);
        emailText = v.findViewById(R.id.changeEmail);
        progressBar = v.findViewById(R.id.progressBarPasswordChange);

        Dialog.OnClickListener listener = (dialog, which) -> {
            if(progressBar.getVisibility() == View.INVISIBLE) {
                dismiss();
                dialogueMessage.setVisibility(View.INVISIBLE);
                emailText.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
        };


        return new AlertDialog.Builder(getActivity())
                .setTitle("Password change")
                .setView(v)
                .setPositiveButton("OK", listener)
                .create();
    }
    public void showDialog(FragmentManager manager){
        this.show(manager, "Popup message is open");
    }

    public void setMessage(String message, String email) {
        dialogueMessage.setText(message);
        dialogueMessage.setVisibility(View.VISIBLE);
        emailText.setText(email);
        emailText.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
