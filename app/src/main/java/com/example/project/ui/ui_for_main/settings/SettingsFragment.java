package com.example.project.ui.ui_for_main.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.example.project.R;
import com.example.project.model.User;
import com.example.project.ui.ui_for_main.worksites.PopupFragment;
import com.google.android.material.snackbar.Snackbar;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
        SwitchPreference autoTheme = getPreferenceManager().findPreference("auto_dark_theme");
        autoTheme.setOnPreferenceClickListener(this);
        SwitchPreference darkTheme = getPreferenceManager().findPreference("dark_theme");
        darkTheme.setOnPreferenceClickListener(this);
        Preference deleteAccount = getPreferenceManager().findPreference("delete_account");
        deleteAccount.setOnPreferenceClickListener(this);


    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals("delete_account"))
        {
            FragmentManager manager = getFragmentManager();
            PopupDeleteFragmnet dialog = new PopupDeleteFragmnet();
            dialog.showDialog(manager);
        }
        Snackbar.make(getView(), "Restart the app (will be fixed)", Snackbar.LENGTH_SHORT).show();
        return true;
    }
}