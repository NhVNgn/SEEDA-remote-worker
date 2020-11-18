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

import com.sereem.remoteworker.R;
import com.sereem.remoteworker.ui.LoginActivity;

public class PopupSignOutFragment  extends AppCompatDialogFragment {
    Activity activity;

    public PopupSignOutFragment(Activity activity) {
        this.activity = activity;
    }

    //private ColorPalette colorPalette;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.popup_sign_out, null);
        //PopupOptionsLayoutBinding binding = PopupOptionsLayoutBinding.bind(v);
        //colorPalette = new ColorPalette(getContext(), binding, ColorPalette.TYPE.POPUP);
        //binding.setColorPalette(colorPalette);
        DialogInterface.OnClickListener yesListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences prefs = getContext()
                        .getSharedPreferences("user", Context.MODE_PRIVATE);
                prefs.edit().putString("email", null).putString("password", null)
                        .putInt("id", -1).apply();
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                activity.finish();
            }
        };

        DialogInterface.OnClickListener backListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dismiss();
            }
        };

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
        //colorPalette.unregisterListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        //colorPalette.registerListener();
    }

}

