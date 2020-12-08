package com.sereem.remoteworker.ui;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.preference.PreferenceManager;

import com.sereem.remoteworker.R;
import com.sereem.remoteworker.databinding.ActivityLoginBinding;
import com.sereem.remoteworker.databinding.ActivityMainBinding;
import com.sereem.remoteworker.databinding.ActivityRegister2Binding;
import com.sereem.remoteworker.databinding.ActivityRegisterBinding;
import com.sereem.remoteworker.databinding.ActivitySiteDetailBinding;
import com.sereem.remoteworker.databinding.FragmentImageViewBinding;
import com.sereem.remoteworker.databinding.FragmentLiveMeetingBinding;
import com.sereem.remoteworker.databinding.FragmentLocationViewBinding;
import com.sereem.remoteworker.databinding.FragmentProfileBinding;
import com.sereem.remoteworker.databinding.FragmentSiteViewBinding;
import com.sereem.remoteworker.databinding.ItemViewBinding;
import com.sereem.remoteworker.databinding.PopupOptionsLayoutBinding;

/**
 * ColorPalette class, implements SensorEventListener, used for changing app's theme
 * and manipulating with light sensor.
 */
public class ColorPalette implements SensorEventListener {
    public enum TYPE {
        LOGIN,
        MAIN,
        ITEM_VIEW,
        PROFILE,
        REGISTER,
        REGISTER2,
        DETAILS,
        POPUP,
        IMAGE_VIEW,
        LOCATION,
        SITE_VIEW,
        CONTACT,
        MEETINGS
    }

    private boolean isDark;
    private int text;
    private int background;
    private int btnBackground;
    private int btnText;
    private Drawable editBackground;
    private SensorManager sensorManager;
    private Sensor sensor;
    private boolean isAutoOff;

    private Context context;

    private TYPE type;

    private ActivityLoginBinding loginBinding;
    private ActivityMainBinding mainBinding;
    private ItemViewBinding itemViewBinding;
    private FragmentProfileBinding profileBinding;
    private ActivityRegister2Binding register2Binding;
    private ActivityRegisterBinding registerBinding;
    private ActivitySiteDetailBinding detailBinding;
    private PopupOptionsLayoutBinding popupOptionsLayoutBinding;
    private FragmentImageViewBinding imageViewBinding;
    private FragmentLocationViewBinding locationViewBinding;
    private FragmentLiveMeetingBinding fragmentLiveMeetingBinding;
    private FragmentSiteViewBinding siteViewBinding;

    public ColorPalette(Context context, ActivityMainBinding mainBinding, TYPE type) {
        this.context = context;
        this.type = type;
        this.mainBinding = mainBinding;
        getSettings();
        setSensor();
        setTheme();
    }

    public ColorPalette(Context context, FragmentImageViewBinding imageViewBinding, TYPE type) {
        this.context = context;
        this.type = type;
        this.imageViewBinding = imageViewBinding;
        getSettings();
        setSensor();
        setTheme();
    }

    public ColorPalette(Context context, FragmentSiteViewBinding siteViewBinding, TYPE type) {
        this.context = context;
        this.type = type;
        this.siteViewBinding = siteViewBinding;
        getSettings();
        setSensor();
        setTheme();
    }
    public ColorPalette(Context context, FragmentLocationViewBinding locationViewBinding, TYPE type) {
        this.context = context;
        this.type = type;
        this.locationViewBinding = locationViewBinding;
        getSettings();
        setSensor();
        setTheme();
    }

    public ColorPalette(Context context, FragmentLiveMeetingBinding fragmentLiveMeetingBinding, TYPE type) {
        this.context = context;
        this.type = type;
        this.fragmentLiveMeetingBinding = fragmentLiveMeetingBinding;
        getSettings();
        setSensor();
        setTheme();
    }

    public ColorPalette(Context context,
                        PopupOptionsLayoutBinding popupOptionsLayoutBinding, TYPE type) {
        this.context = context;
        this.type = type;
        this.popupOptionsLayoutBinding = popupOptionsLayoutBinding;
        getSettings();
        setSensor();
        setTheme();
    }

    public ColorPalette(Context context, ActivitySiteDetailBinding detailBinding, TYPE type) {
        this.context = context;
        this.type = type;
        this.detailBinding = detailBinding;
        getSettings();
        setSensor();
        setTheme();
    }

    public ColorPalette(Context context, ActivityRegisterBinding registerBinding, TYPE type) {
        this.context = context;
        this.type = type;
        this.registerBinding = registerBinding;
        getSettings();
        setSensor();
        setTheme();
    }

    public ColorPalette(Context context, ActivityRegister2Binding register2Binding, TYPE type) {
        this.context = context;
        this.type = type;
        this.register2Binding = register2Binding;
        getSettings();
        setSensor();
        setTheme();
    }

    public ColorPalette(Context context, FragmentProfileBinding profileBinding, TYPE type) {
        this.context = context;
        this.type = type;
        this.profileBinding = profileBinding;
        getSettings();
        setSensor();
        setTheme();
    }

    public ColorPalette(Context context, ActivityLoginBinding loginBinding, TYPE type) {
        this.context = context;
        this.type = type;
        this.loginBinding = loginBinding;
        getSettings();
        setSensor();
        setTheme();
    }

    public ColorPalette(Context context, ItemViewBinding itemViewBinding, TYPE type) {
        this.context = context;
        this.type = type;
        this.itemViewBinding = itemViewBinding;
        getSettings();
        setSensor();
        setTheme();
    }

    private void getSettings() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        isAutoOff = prefs.getBoolean("auto_dark_theme", false);
        if(isAutoOff) {
            isDark = prefs.getBoolean("dark_theme", false);
        }
//        setTheme();
    }

    private void setSensor() {
            sensorManager = (SensorManager) context.getSystemService(Service.SENSOR_SERVICE);
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }


    public void registerListener() {
        if(!isAutoOff) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void unregisterListener() {
        if (!isAutoOff) {
            sensorManager.unregisterListener(this);
        }
    }

    private void setTheme() {
        if(isDark) {
            setDarkTheme();
        } else {
            setLightTheme();
        }
    }


    private void setLightTheme() {
        text = context.getColor(R.color.text_light);
        background = context.getColor(R.color.background_light);
        btnBackground = context.getColor(R.color.btn_background_light);
        btnText = context.getColor(R.color.btn_text_light);
        editBackground = context.getDrawable(R.drawable.edit_text_light);
    }

    private void setDarkTheme() {
        text = context.getColor(R.color.text_dark);
        background = context.getColor(R.color.background_dark);
        btnBackground = context.getColor(R.color.btn_background_dark);
        btnText = context.getColor(R.color.btn_text_dark);
        editBackground = context.getDrawable(R.drawable.edit_text_dark);
    }

    public boolean isDark() {
        return isDark;
    }

    public void setDark(boolean dark) {
        isDark = dark;
        setTheme();
    }

    public int getText() {
        return text;
    }

    public int getBackground() {
        return background;
    }

    public int getBtnBackground() {
        return btnBackground;
    }

    public int getBtnText() {
        return btnText;
    }

    public Drawable getEditBackground() {
        return editBackground;
    }





    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_LIGHT) {
            switch (type) {
                case LOGIN: {
                    ActivityLoginBinding binding = loginBinding;
                    if (event.values[0] == 0 && !isDark()) {
                        setDark(true);
                        binding.setColorPalette(this);
                    } else if (event.values[0] >= 1.0 && isDark()) {
                        setDark(false);
                        binding.setColorPalette(this);
                    }
                    break;
                }
                case PROFILE: {
                    FragmentProfileBinding binding = profileBinding;
                    if (event.values[0] == 0 && !isDark()) {
                        setDark(true);
                        binding.setColorPalette(this);
                    } else if (event.values[0] >= 1.0 && isDark()) {
                        setDark(false);
                        binding.setColorPalette(this);
                    }
                    break;
                }
                case SITE_VIEW: {
                    FragmentSiteViewBinding binding = siteViewBinding;
                    if (event.values[0] == 0 && !isDark()) {
                        setDark(true);
                        binding.setColorPalette(this);
                    } else if (event.values[0] >= 1.0 && isDark()) {
                        setDark(false);
                        binding.setColorPalette(this);
                    }
                    break;
                }
                case IMAGE_VIEW: {
                    FragmentImageViewBinding binding = imageViewBinding;
                    if (event.values[0] == 0 && !isDark()) {
                        setDark(true);
                        binding.setColorPalette(this);
                    } else if (event.values[0] >= 1.0 && isDark()) {
                        setDark(false);
                        binding.setColorPalette(this);
                    }
                    break;
                }
                case POPUP: {
                    PopupOptionsLayoutBinding binding = popupOptionsLayoutBinding;
                    if (event.values[0] == 0 && !isDark()) {
                        setDark(true);
                        binding.setColorPalette(this);
                    } else if (event.values[0] >= 1.0 && isDark()) {
                        setDark(false);
                        binding.setColorPalette(this);
                    }
                    break;
                }
                case LOCATION: {
                    FragmentLocationViewBinding binding = locationViewBinding;
                    if (event.values[0] == 0 && !isDark()) {
                        setDark(true);
                        binding.setColorPalette(this);
                    } else if (event.values[0] >= 1.0 && isDark()) {
                        setDark(false);
                        binding.setColorPalette(this);
                    }
                    break;
                }
                case MEETINGS: {
                    FragmentLiveMeetingBinding binding = fragmentLiveMeetingBinding;
                    if (event.values[0] == 0 && !isDark()) {
                        setDark(true);
                        binding.setColorPalette(this);
                    } else if (event.values[0] >= 1.0 && isDark()) {
                        setDark(false);
                        binding.setColorPalette(this);
                    }
                    break;
                }
                case REGISTER2: {
                    ActivityRegister2Binding binding = register2Binding;
                    if (event.values[0] == 0 && !isDark()) {
                        setDark(true);
                        binding.setColorPalette(this);
                    } else if (event.values[0] >= 1.0 && isDark()) {
                        setDark(false);
                        binding.setColorPalette(this);
                    }
                    break;
                }
                case REGISTER: {
                    ActivityRegisterBinding binding = registerBinding;
                    if (event.values[0] == 0 && !isDark()) {
                        setDark(true);
                        binding.setColorPalette(this);
                    } else if (event.values[0] >= 1.0 && isDark()) {
                        setDark(false);
                        binding.setColorPalette(this);
                    }
                    break;
                }
                case DETAILS: {
                    ActivitySiteDetailBinding binding = detailBinding;
                    if (event.values[0] == 0 && !isDark()) {
                        setDark(true);
                        binding.setColorPalette(this);
                    } else if (event.values[0] >= 1.0 && isDark()) {
                        setDark(false);
                        binding.setColorPalette(this);
                    }
                    break;
                }
                case MAIN: {
                    ActivityMainBinding binding = mainBinding;
                    if (event.values[0] == 0 && !isDark()) {
                        setDark(true);
                        binding.setColorPalette(this);
                    } else if (event.values[0] >= 1.0 && isDark()) {
                        setDark(false);
                        binding.setColorPalette(this);
                    }
                    break;
                }
                case ITEM_VIEW: {
                    ItemViewBinding binding = itemViewBinding;
                    if (event.values[0] == 0 && !isDark()) {
                        setDark(true);
                        binding.setColorPalette(this);
                    } else if (event.values[0] >= 1.0 && isDark()) {
                        setDark(false);
                        binding.setColorPalette(this);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
