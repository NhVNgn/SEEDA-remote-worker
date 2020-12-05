package com.sereem.remoteworker.model;

import android.graphics.Color;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class CustomSnackbar {
    public static Snackbar create(View view) {
        Snackbar snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(Color.parseColor("#204E75"))
                .setTextColor(Color.WHITE);
        return snackbar;
    }
}
