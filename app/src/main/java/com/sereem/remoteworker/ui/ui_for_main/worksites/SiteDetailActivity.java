package com.sereem.remoteworker.ui.ui_for_main.worksites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sereem.remoteworker.R;
import com.sereem.remoteworker.databinding.ActivitySiteDetailBinding;
import com.sereem.remoteworker.model.User;
import com.sereem.remoteworker.model.workSite.WorkSite;
import com.sereem.remoteworker.ui.ColorPalette;
import com.sereem.remoteworker.ui.CustomSnackbar;
import com.sereem.remoteworker.ui.ErrorDialog;
import com.sereem.remoteworker.ui.ui_for_main.worksites.chat.ChatActivity;
import com.sereem.remoteworker.ui.ui_for_main.worksites.worksiteDetails.MapsFragment;
import com.sereem.remoteworker.ui.ui_for_main.worksites.worksiteDetails.PagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.sereem.remoteworker.ui.ui_for_main.worksites.LiveMeetingFragment.linkList;
import static com.sereem.remoteworker.ui.ui_for_main.worksites.LiveMeetingFragment.urlGoogleMeet;

/**
 * SiteDetailActivity class, used for interaction with chosen worksite. Uses Tab View.
 */
public class SiteDetailActivity extends AppCompatActivity {
    private ColorPalette colorPalette;
    PagerAdapter pagerAdapter;
    TabLayout tabLayout;
    private static HashMap<String, User> userList;
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
        setUserList();
        receiveGoogleMeetLink();

    }

    private void setUserList() {
        userList = new HashMap<>();
        CollectionReference userReference;
        WorkSite workSite = WorkSite.getChosenWorksite();
        User user = User.getInstance();
        userReference = FirebaseFirestore.getInstance().collection("users");
        userReference.whereArrayContains("worksites", workSite.getSiteID())
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (!user.getUID().equals(document.getId())) {
                        User coUser = document.toObject(User.class);
                        userList.put(document.getId(), coUser);
                        StorageReference storageReference = FirebaseStorage.getInstance()
                                .getReference("profileIcons/" +
                                        coUser.getUID() + ".jpeg");
                        downloadIcon(coUser, storageReference);
                    }
                }
            } else {
                ErrorDialog.show(this);
            }
        });
    }

    public static HashMap<String, User> getUserList() {
        return userList;
    }

    private void downloadIcon(User user, StorageReference storageReference) {
        File file = new File(getCacheDir() + "/" + user.getUID() + ".jpeg");
        Uri iconUri = Uri.fromFile(file);
        storageReference.getFile(iconUri).addOnCompleteListener(task -> {
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
                                           @NotNull String[] permissions,
                                           @NotNull int[] grantResults) {
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
        WorkSite workSite = WorkSite.getChosenWorksite();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(
                "lives/" + workSite.getSiteID());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String host = "";
                GoogleMeetLink googleMeetLink = snapshot.getValue(GoogleMeetLink.class);
                linkList.add(googleMeetLink);

                if (googleMeetLink != null)
                {
                    host = googleMeetLink.getHost();

                    urlGoogleMeet = googleMeetLink.getLink();
                    if (urlGoogleMeet.equals("Meeting has ended")) {
                        CustomSnackbar.create(getWindow().getDecorView().getRootView())
                                .setText("No meeting available").show();
                    }
                    else {
                        if (!googleMeetLink.getUserId().equals(User.getInstance().getUID())) {
                            showNotification(host);
                        }
                    }
                }
            }

            private void showNotification(String host) {
                AlertDialog dialog = new AlertDialog.Builder(SiteDetailActivity.this)
                        .setTitle("Notification")
                        .setMessage(host + " hosted a meeting right now!")
                        .setPositiveButton("JOIN", (dialog1, which) -> {
                            if (urlGoogleMeet == null || urlGoogleMeet.equals("No meeting available"))
                                CustomSnackbar.create(getWindow().getDecorView().getRootView())
                                        .setText("Meeting has just ended").show();
                            else{
                                String url = urlGoogleMeet;
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                if (!url.contains("Meeting has ended"))
                                    startActivity(i);
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
                if(error.getCode() != DatabaseError.PERMISSION_DENIED && getApplicationContext() != null)
                    ErrorDialog.show(getApplicationContext());
            }
        });
    }
}