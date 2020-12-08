package com.sereem.remoteworker.ui.ui_for_main.worksites.worksiteDetails;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.sereem.remoteworker.R;
import com.sereem.remoteworker.databinding.FragmentSiteViewBinding;
import com.sereem.remoteworker.model.workSite.SiteDatabase;
import com.sereem.remoteworker.model.workSite.WorkSite;
import com.sereem.remoteworker.ui.ColorPalette;

/**
 * SiteViewFragment class, used for displaying the information about chosen worksite.
 */
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



        Button button = root.findViewById(R.id.siteEmergencyInfoText);
        button.setOnClickListener(view -> {
            String url = "https://www.seerem.seeda.ca/home";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        siteNameText.setText(userWorkSite.getName());
        siteIdText.setText(userWorkSite.getSiteID());
        operationHoursText.setText(userWorkSite.getHours());
        masterPointText.setText(userWorkSite.getMasterPoint());
        return root;
    }

    private void getSite() {
        userWorkSite = WorkSite.getChosenWorksite();
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