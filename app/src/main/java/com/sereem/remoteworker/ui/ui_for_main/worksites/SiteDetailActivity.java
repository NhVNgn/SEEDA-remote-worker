package com.sereem.remoteworker.ui.ui_for_main.worksites;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sereem.remoteworker.R;
import com.sereem.remoteworker.databinding.ActivitySiteDetailBinding;
import com.sereem.remoteworker.ui.ColorPalette;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.sereem.remoteworker.ui.ui_for_main.worksites.chat.ChatActivity;
import com.sereem.remoteworker.ui.ui_for_main.worksites.worksiteDetails.MapsFragment;
import com.sereem.remoteworker.ui.ui_for_main.worksites.worksiteDetails.PagerAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

public class SiteDetailActivity extends AppCompatActivity {
    private ColorPalette colorPalette;
    PagerAdapter pagerAdapter;
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
        TabLayout tabLayout = findViewById(R.id.tabBar);
        TabItem tabSiteInfo = findViewById(R.id.siteInfo);
        TabItem tabContactInfo = findViewById(R.id.contactsInfo);
        TabItem locationInfo = findViewById(R.id.locationInfo);
        ViewPager viewPager = findViewById(R.id.viewPager);
        setUpBackButton();
        FloatingActionButton fob = findViewById(R.id.fobChat);
        fob.setOnClickListener(v -> startActivity(new Intent(
                SiteDetailActivity.this, ChatActivity.class)));

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


}