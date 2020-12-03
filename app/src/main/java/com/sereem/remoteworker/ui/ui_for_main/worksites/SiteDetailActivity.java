package com.sereem.remoteworker.ui.ui_for_main.worksites;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sereem.remoteworker.R;
import com.sereem.remoteworker.databinding.ActivitySiteDetailBinding;
import com.sereem.remoteworker.model.workSite.WorkSite;
import com.sereem.remoteworker.ui.ColorPalette;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.sereem.remoteworker.ui.ui_for_main.worksites.LiveMeetingFragment.linkList;
import static com.sereem.remoteworker.ui.ui_for_main.worksites.LiveMeetingFragment.urlGoogleMeet;

public class SiteDetailActivity extends AppCompatActivity {
    private ColorPalette colorPalette;
    PagerAdapter pagerAdapter;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar;
        super.onCreate(savedInstanceState);
        ActivitySiteDetailBinding binding = DataBindingUtil.setContentView(
                this, R.layout.activity_site_detail);
        colorPalette = new ColorPalette(this, binding, ColorPalette.TYPE.DETAILS);
        binding.setColorPalette(colorPalette);
        binding.setLifecycleOwner(this);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#275F8E")));
        tabLayout = findViewById(R.id.tabBar);
        TabItem tabSiteInfo = findViewById(R.id.siteInfo);
        TabItem tabContactInfo = findViewById(R.id.contactsInfo);
        TabItem locationInfo = findViewById(R.id.locationInfo);
        TabItem meetingInfo = findViewById(R.id.liveMeetingInfo);
        ViewPager viewPager = findViewById(R.id.viewPager);
        setUpBackButton();

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        receiveGoogleMeetLink();

    }

    private void setUpBackButton(){
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        colorPalette.unregisterListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        colorPalette.registerListener();
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                tabLayout.getTabAt(3).select();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MapsFragment.MY_PERMISSIONS_REQUEST_LOCATION){
            MapsFragment mapFragment = (MapsFragment) pagerAdapter.fragments[1];
            if (mapFragment != null) {
                mapFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void receiveGoogleMeetLink(){
        linkList = new ArrayList<>();
        WorkSite workSite = WorkSite.getChosenWorksite();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(
                "lives/" + workSite.getSiteID());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                linkList.clear();
                String host = "";
                for (DataSnapshot snapshotItem : snapshot.getChildren()){
                    GoogleMeetLink googleMeetLink = snapshotItem.getValue(GoogleMeetLink.class);
                    linkList.add(googleMeetLink);
                }

                if (!linkList.isEmpty())
                {
                    GoogleMeetLink lastLink = linkList.get(linkList.size()-1);
                    host = lastLink.getHost();

                    urlGoogleMeet = lastLink.getLink();
                    if (urlGoogleMeet.equals("Meeting has ended"))
                        Toast.makeText(SiteDetailActivity.this, "No meeting available ", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(SiteDetailActivity.this, "you have a new meeting in Tab MEETINGS: " + lastLink.getLink() + " by " + host, Toast.LENGTH_SHORT).show();
                    }
                }

                int counters = linkList.size()-1;
                for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
                    counters--;
                    if (counters == 0)
                        break;
                    appleSnapshot.getRef().removeValue();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}