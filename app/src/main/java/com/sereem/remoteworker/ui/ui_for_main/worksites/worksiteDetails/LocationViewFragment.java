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
import com.sereem.remoteworker.databinding.FragmentLocationViewBinding;
import com.sereem.remoteworker.model.workSite.SiteDatabase;
import com.sereem.remoteworker.model.workSite.WorkSite;
import com.sereem.remoteworker.ui.ColorPalette;

/**
 * LocationViewFragment, used for displaying google maps inside the app. Displays the location of
 * chosen worksite.
 */
public class LocationViewFragment extends Fragment {
    SiteDatabase siteDB;
    WorkSite userWorkSite = null;
    TextView addressText;
    Button mapButton;
    private ColorPalette colorPalette;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_location_view, container, false);
        FragmentLocationViewBinding binding = FragmentLocationViewBinding.bind(root);
        colorPalette = new ColorPalette(getContext(), binding, ColorPalette.TYPE.LOCATION);
        binding.setColorPalette(colorPalette);
        siteDB = new SiteDatabase(root.getContext());
        getSite();
        mapButton = (Button) root.findViewById(R.id.addressButton);
        mapButton.setOnClickListener(view -> launchMapIntent());
        addressText = root.findViewById(R.id.addressTextDisplay);
        addressText.setText(userWorkSite.getLocation());


        return root;

    }
    private void getSite() {
        userWorkSite = WorkSite.getChosenWorksite();
    }

    public void launchMapIntent(){
        // process address
        StringBuilder uri_address = new StringBuilder();
        String raw_address = userWorkSite.getLocation();
        String[] separate_address = raw_address.split(",");
        String url = "geo:0,0?q=";
        uri_address.append(url).append(separate_address[0]).append(",").append(separate_address[1]);

        Uri gmmIntentUri =  Uri.parse(String.valueOf(uri_address));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
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