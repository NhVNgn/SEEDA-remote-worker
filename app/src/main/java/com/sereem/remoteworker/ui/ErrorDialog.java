package com.sereem.remoteworker.ui;

import android.app.AlertDialog;
import android.content.Context;

import com.sereem.remoteworker.R;

/**
 * ErrorDialog class, used for displaying internet connection errors.
 */
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
