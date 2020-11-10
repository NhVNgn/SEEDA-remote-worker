package com.example.project.ui.ui_for_main.worksites;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.project.R;

public class PopupFragment extends AppCompatDialogFragment {
    ImageButton sms;
    ImageButton phone;
    ImageButton email;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
       View v = LayoutInflater.from(getActivity()).inflate(R.layout.popup_options_layout, null);
        sms = v.findViewById(R.id.smsButton);
        phone = v.findViewById(R.id.phoneButton);
        email = v.findViewById(R.id.emailButton);
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri sms = Uri.parse("smsto:7782316872");
                Intent intent = new Intent(Intent.ACTION_SENDTO, sms);
                intent.putExtra(Intent.EXTRA_TEXT, "How are you doing today?");
                startActivity(Intent.createChooser(intent, "send a text to"));
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"nhanvyhl1234@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "This is subject header");
                startActivity(intent);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "7782316872"));
                startActivity(intent);
            }
        });
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        };
        return new AlertDialog.Builder(getActivity())
                .setTitle("Choose an app to contact")
                .setView(v)
                .setPositiveButton("BACK", listener)
                .create();
    }
}
