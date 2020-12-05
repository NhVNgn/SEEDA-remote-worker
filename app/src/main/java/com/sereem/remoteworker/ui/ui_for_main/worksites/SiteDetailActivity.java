package com.sereem.remoteworker.ui.ui_for_main.worksites;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sereem.remoteworker.R;
import com.sereem.remoteworker.databinding.ActivitySiteDetailBinding;
import com.sereem.remoteworker.model.User;
import com.sereem.remoteworker.model.workSite.WorkSite;
import com.sereem.remoteworker.ui.ColorPalette;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.sereem.remoteworker.ui.ui_for_main.worksites.chat.ChatActivity;
import com.sereem.remoteworker.ui.ui_for_main.worksites.worksiteDetails.MapsFragment;
import com.sereem.remoteworker.ui.ui_for_main.worksites.worksiteDetails.PagerAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
                    if (urlGoogleMeet.equals("Meeting has ended")) {
                        //Toast.makeText(SiteDetailActivity.this, "No meeting available ", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (!lastLink.getHost().equals(User.getInstance().getFirstName())) {
                            showNotification(host, lastLink);
                        }
                    }
                }

            }

            private void showNotification(String host, GoogleMeetLink lastLink) {
                AlertDialog dialog = new AlertDialog.Builder(SiteDetailActivity.this)
                        .setTitle("Notification")
                        .setMessage(host + " hosted a meeting right now!")
                        .setPositiveButton("JOIN", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (urlGoogleMeet == null || urlGoogleMeet.equals("No meeting available"))
                                    Toast.makeText(SiteDetailActivity.this, "Meeting has just ended", Toast.LENGTH_SHORT).show();
                                else{
                                    String url = urlGoogleMeet;
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    if (!url.contains("Meeting has ended"))
                                        startActivity(i);
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    private static final int AUTO_DISMISS_MILLIS = 6000;
                    @Override
                    public void onShow(final DialogInterface dialog) {
                        final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                        final CharSequence negativeButtonText = defaultButton.getText();
                        new CountDownTimer(AUTO_DISMISS_MILLIS, 100) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                defaultButton.setText(String.format(
                                        Locale.getDefault(), "%s (%d)",
                                        negativeButtonText,
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
                                ));
                            }
                            @Override
                            public void onFinish() {
                                if (((AlertDialog) dialog).isShowing()) {
                                    dialog.dismiss();
                                }
                            }
                        }.start();
                    }
                });
                if(!isFinishing())
                {
                    dialog.show();
                }


            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}