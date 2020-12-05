package com.sereem.remoteworker.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;

import androidx.fragment.app.DialogFragment;

import com.sereem.remoteworker.R;

public class ErrorDialog {
    public static void show(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Error")
                .setMessage("Oops, something went wrong... Please check your internet " +
                        "connection and try again.")
                .setNegativeButton(android.R.string.ok, null)
                .setIcon(R.drawable.ic_error_red)
                .show();
    }
}
