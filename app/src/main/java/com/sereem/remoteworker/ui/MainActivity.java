package com.sereem.remoteworker.ui;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.sereem.remoteworker.R;
import com.sereem.remoteworker.databinding.ActivityMainBinding;
import com.sereem.remoteworker.model.Database;
import com.sereem.remoteworker.model.User;
import com.sereem.remoteworker.ui.ui_for_main.PopupSignOutFragment;
import com.google.android.material.internal.NavigationMenuItemView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Database db;
    private static View headerView;
    private ColorPalette colorPalette;
    private ActivityMainBinding binding;
    private SensorManager sensorManager;
    private Sensor sensor;
    private NavigationView navigationView;

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

        db = new Database(this);
        showUserInfo();
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
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        User user = db.getUser(prefs.getInt("id", -1));
        headerView = navigationView.getHeaderView(0);
        ImageView icon = headerView.findViewById(R.id.profileIconMain);
        if(user.getIconUri() != null && !user.getIconUri().toString().equals("")) {
            icon.setImageURI(user.getIconUri());
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