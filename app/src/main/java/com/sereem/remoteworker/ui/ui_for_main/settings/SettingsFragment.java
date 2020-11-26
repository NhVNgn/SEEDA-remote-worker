package com.sereem.remoteworker.ui.ui_for_main.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.sereem.remoteworker.R;
import com.google.android.material.snackbar.Snackbar;

import static android.content.Context.MODE_PRIVATE;

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
        gpsVisibility.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isOn = (boolean) newValue;
                if (isOn){
                    Snackbar.make(getView(), "GPS visibility is enabled", Snackbar.LENGTH_SHORT).show();
                    SharedPreferences prefs = getContext().getSharedPreferences("user", MODE_PRIVATE);
                    prefs.edit().putBoolean("visibility", true).apply();
                }
                else {
                    Snackbar.make(getView(), "GPS visibility is disabled", Snackbar.LENGTH_SHORT).show();
                    SharedPreferences prefs = getContext().getSharedPreferences("user", MODE_PRIVATE);
                    prefs.edit().putBoolean("visibility", false).apply();
                }
                return true;
            }
        });

    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals("delete_account")) {
            FragmentManager manager = getFragmentManager();
            PopupDeleteFragmnet dialog = new PopupDeleteFragmnet();
            dialog.showDialog(manager);
        }

        Snackbar.make(getView(), "Restart the app (will be fixed)", Snackbar.LENGTH_SHORT).show();
        return true;
    }
}