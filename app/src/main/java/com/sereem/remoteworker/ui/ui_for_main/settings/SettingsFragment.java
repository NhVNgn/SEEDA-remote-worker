package com.sereem.remoteworker.ui.ui_for_main.settings;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.sereem.remoteworker.R;
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

        SwitchPreference gpsVisibility = getPreferenceManager().findPreference("gps_visibility");
        gpsVisibility.setOnPreferenceClickListener(this);

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