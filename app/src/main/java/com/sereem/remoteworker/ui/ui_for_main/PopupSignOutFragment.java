package com.sereem.remoteworker.ui.ui_for_main;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.sereem.remoteworker.R;
import com.sereem.remoteworker.model.User;
import com.sereem.remoteworker.ui.LoginActivity;
import com.sereem.remoteworker.ui.MainActivity;
import com.sereem.remoteworker.ui.ui_for_main.service.LocationService;

/**
 * PopupSignOutFragment class, used for singing out the user from the app.
 */
public class PopupSignOutFragment  extends AppCompatDialogFragment {
    Activity activity;
    private FirebaseAuth fAuth;
    public PopupSignOutFragment(Activity activity) {
        this.activity = activity;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.popup_sign_out, null);
        fAuth = FirebaseAuth.getInstance();
        DialogInterface.OnClickListener yesListener = (dialogInterface, i) -> {
            SharedPreferences prefs = getContext()
                    .getSharedPreferences("user", Context.MODE_PRIVATE);
            prefs.edit().putString("email", null).putString("password", null)
                    .putString("UID", "").apply();
            fAuth.signOut();
            Intent stopIntent = MainActivity.serviceIntent;
            stopIntent.setAction(LocationService.ACTION_STOP_FOREGROUND_SERVICE);
            getActivity().startService(stopIntent);
            saveInSharedPrefs("", "");
            User.setInstanceToNull();

            Intent intent = new Intent(activity, LoginActivity.class);
            startActivity(intent);
            activity.finish();
        };

        DialogInterface.OnClickListener backListener = (dialogInterface, i) -> dismiss();

        return new AlertDialog.Builder(getActivity())
                .setTitle("Are you sure you want to sign out from your account?")
                .setPositiveButton("Yes", yesListener)
                .setNegativeButton("No", backListener)
                .create();
    }
    public void showDialog(FragmentManager manager){
        this.show(manager, "Popup message is open");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void saveInSharedPrefs(String email, String id) {
        SharedPreferences prefs = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        prefs.edit().putString("email", email).putString("UID", id).apply();
    }

}

