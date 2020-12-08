package com.sereem.remoteworker.ui;

import android.graphics.Color;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

/**
 * CustomSnackbar class, used as custom android Snack bar.
 */
public class CustomSnackbar {
    public static Snackbar create(View view) {
        Snackbar snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(Color.parseColor("#204E75"))
                .setTextColor(Color.WHITE);
        return snackbar;
    }
}
