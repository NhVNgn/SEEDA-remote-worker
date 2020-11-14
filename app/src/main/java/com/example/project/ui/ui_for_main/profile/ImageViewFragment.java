package com.example.project.ui.ui_for_main.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.project.R;
import com.example.project.databinding.FragmentImageViewBinding;
import com.example.project.ui.ColorPalette;

public class ImageViewFragment extends AppCompatDialogFragment {
    private Uri iconUri;
    private ColorPalette colorPalette;

    public ImageViewFragment(Uri uri) {
        iconUri = uri;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_image_view, null);
        FragmentImageViewBinding binding = FragmentImageViewBinding.bind(v);
        colorPalette = new ColorPalette(getContext(), binding, ColorPalette.TYPE.IMAGE_VIEW);
        binding.setColorPalette(colorPalette);
        ImageButton closeBtn = v.findViewById(R.id.closeButtonImageView);
        closeBtn.setOnClickListener(v1 -> {
            dismiss();
        });

        ImageView icon = v.findViewById(R.id.iconImageView);
        icon.setImageURI(iconUri);

        Dialog dialog = new AlertDialog.Builder(getActivity()).setView(v).create();
        dialog.setTitle(null);
        return dialog;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(colorPalette != null) {
            colorPalette.unregisterListener();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(colorPalette != null) {
            colorPalette.registerListener();
        }
    }
}


