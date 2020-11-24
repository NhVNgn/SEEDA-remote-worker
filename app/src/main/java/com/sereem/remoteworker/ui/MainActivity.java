package com.sereem.remoteworker.ui;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sereem.remoteworker.R;
import com.sereem.remoteworker.databinding.ActivityMainBinding;
//import com.sereem.remoteworker.model.Database;
import com.sereem.remoteworker.model.User;
import com.sereem.remoteworker.ui.ui_for_main.PopupSignOutFragment;
import com.google.android.material.internal.NavigationMenuItemView;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static Uri iconUri;

    private AppBarConfiguration mAppBarConfiguration;
//    private Database db;
    private static View headerView;
    private ColorPalette colorPalette;
    private ActivityMainBinding binding;
    private SensorManager sensorManager;
    private Sensor sensor;
    private NavigationView navigationView;
    private DocumentReference documentReference;
    private StorageReference storageReference;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView
                (this, R.layout.activity_main);
        colorPalette = new ColorPalette(this, binding, ColorPalette.TYPE.MAIN);
        binding.setColorPalette(colorPalette);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_worksites, R.id.nav_profile, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

//        db = new Database(this);
        initializeDocumentReference();
//        initializeStorageReference();
//        downloadIconFromUri();
    }

    private void downloadIconFromUri() {
//        ProgressBar progressBar = root.findViewById(R.id.progressBarProfile);
//        progressBar.setVisibility(View.VISIBLE);
        File file = new File(getCacheDir() + "/" + user.getUID() + ".jpg");
        iconUri = Uri.fromFile(file);
        storageReference.getFile(iconUri).addOnCompleteListener(task -> {
            if(!task.isSuccessful()) {
                Toast.makeText(this, task.getException().getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }
//            progressBarsBar.setVisibility(View.INVISIBLE);
        });
    }

    private void initializeStorageReference() {
        storageReference = FirebaseStorage.getInstance().getReference("profileIcons/" +
                user.getUID() + ".jpg");
    }

    private void initializeDocumentReference() {
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        String UID = prefs.getString("UID", "");
        documentReference = FirebaseFirestore.getInstance().document(
                "/users/" + UID + "/");
        documentReference.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if(document != null && document.exists()) {
                    user = User.createNewInstance(document.toObject(User.class));
                    initializeStorageReference();
                    downloadIconFromUri();
                    showUserInfo();
                }
            } else {
                Toast.makeText(MainActivity.this,
                        Objects.requireNonNull(task.getException()).getLocalizedMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
        documentReference.addSnapshotListener((value, error) -> {
            if(error != null) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG)
                .show();
            }
            else if(value != null && value.exists()) {
                user = User.createNewInstance(value.toObject(User.class));
                showUserInfo();
            }
        });
    }

    private void setUpSignOutOption() {
        NavigationMenuItemView signOut = (NavigationMenuItemView) navigationView.findViewById(R.id.nav_sign_out);
        signOut.setOnClickListener(v -> {
            PopupSignOutFragment dialog = new PopupSignOutFragment(MainActivity.this);
            dialog.showDialog(getSupportFragmentManager());
        });
    }

    @SuppressLint("SetTextI18n")
    private void showUserInfo() {
        headerView = navigationView.getHeaderView(0);
        ImageView icon = headerView.findViewById(R.id.profileIconMain);
        if(user.getIconUri() != null && !user.getIconUri().equals("")) {
            icon.setImageURI(iconUri);
        } else {
            icon.setImageResource(R.drawable.profile_icon);
        }
        TextView emailText = headerView.findViewById(R.id.emailMainTextView);
        emailText.setText(user.getEmail());
        TextView nameText = headerView.findViewById(R.id.nameMainTextView);
        nameText.setText(user.getFirstName() + " " + user.getLastName());
    }

    public static View getHeaderView() {
        return headerView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        setUpSignOutOption();
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public static Intent makeLaunchIntent(Context context, String email, String password) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        return intent;
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
}