package com.sereem.remoteworker.ui.ui_for_main.worksites;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sereem.remoteworker.R;
import com.sereem.remoteworker.databinding.FragmentSiteViewBinding;
import com.sereem.remoteworker.model.workSite.SiteDatabase;
import com.sereem.remoteworker.model.workSite.WorkSite;
import com.sereem.remoteworker.ui.ColorPalette;

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
        siteIdText.setText(userWorkSite.getSiteID());
        operationHoursText.setText(userWorkSite.getHours());
        masterPointText.setText(userWorkSite.getMasterPoint());
        return root;
    }

    private void getSite() {
//        SharedPreferences prefs = getActivity().getSharedPreferences(
//                "user", Context.MODE_PRIVATE);

//        String id = prefs.getString("last_accessed_site_id", "NONE");

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