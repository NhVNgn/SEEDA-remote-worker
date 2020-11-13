package com.example.project.ui.ui_for_main.worksites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.databinding.FragmentSiteViewBinding;
import com.example.project.model.workSite.SiteDatabase;
import com.example.project.model.workSite.WorkSite;
import com.example.project.ui.ColorPalette;

import org.w3c.dom.Text;

import java.util.List;

public class SiteViewFragment extends Fragment {

    SiteDatabase siteDB;
    WorkSite userWorkSite = null;
    TextView siteNameText;
    TextView siteIdText;
    TextView masterPointText;
    TextView operationHoursText;
    private ColorPalette colorPalette;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_site_view, container, false);
        FragmentSiteViewBinding binding = FragmentSiteViewBinding.bind(root);
        colorPalette = new ColorPalette(getContext(), binding, ColorPalette.TYPE.SITE_VIEW);
        binding.setColorPalette(colorPalette);
        siteDB = new SiteDatabase(root.getContext());
        getSite();
        siteNameText = root.findViewById(R.id.siteNameTextDisplay);
        siteIdText = root.findViewById(R.id.siteIdTextDisplay);
        masterPointText = root.findViewById(R.id.masterPointTextDisplay);
        operationHoursText = root.findViewById(R.id.hoursTextDisplay);

        siteNameText.setText(userWorkSite.getName());
        siteIdText.setText(userWorkSite.getSiteId());
        operationHoursText.setText(userWorkSite.getHours());
        masterPointText.setText(userWorkSite.getMasterPoint());
        return root;
    }

    private void getSite() {
        SharedPreferences prefs = getActivity().getSharedPreferences(
                "user", Context.MODE_PRIVATE);

        String id = prefs.getString("last_accessed_site_id", "NONE");
         userWorkSite = siteDB.getSite(id);
    }

    @Override
    public void onPause() {
        super.onPause();
        colorPalette.unregisterListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        colorPalette.registerListener();
    }
}