package com.example.project.ui;

import android.app.Service;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.project.R;
import com.example.project.databinding.ActivityLoginBinding;
import com.example.project.databinding.ActivityMainBinding;
import com.example.project.databinding.ActivityRegister2Binding;
import com.example.project.databinding.ActivityRegisterBinding;
import com.example.project.databinding.ActivitySiteDetailBinding;
import com.example.project.databinding.ContactParentItemBinding;
import com.example.project.databinding.FragmentImageViewBinding;
import com.example.project.databinding.FragmentLocationViewBinding;
import com.example.project.databinding.FragmentProfileBinding;
import com.example.project.databinding.FragmentSiteViewBinding;
import com.example.project.databinding.ItemViewBinding;
import com.example.project.databinding.PopupOptionsLayoutBinding;

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
        CONTACT
    }

    private boolean isDark;
    private int text;
    private int background;
    private int btnBackground;
    private int btnText;
    private Drawable editBackground;
    private SensorManager sensorManager;
    private Sensor sensor;

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
    private FragmentSiteViewBinding siteViewBinding;
    private ContactParentItemBinding contactBinding;

    public ColorPalette(Context context, ActivityMainBinding mainBinding, TYPE type) {
        this.context = context;
        this.type = type;
        this.mainBinding = mainBinding;
        isDark = false;
        setSensor();
        setTheme();
    }

    public ColorPalette(Context context, FragmentImageViewBinding imageViewBinding, TYPE type) {
        this.context = context;
        this.type = type;
        this.imageViewBinding = imageViewBinding;
        isDark = false;
        setSensor();
        setTheme();
    }

    public ColorPalette(Context context, ContactParentItemBinding contactBinding, TYPE type) {
        this.context = context;
        this.type = type;
        this.contactBinding = contactBinding;
        isDark = false;
        setSensor();
        setTheme();
    }
    public ColorPalette(Context context, FragmentSiteViewBinding siteViewBinding, TYPE type) {
        this.context = context;
        this.type = type;
        this.siteViewBinding = siteViewBinding;
        isDark = false;
        setSensor();
        setTheme();
    }

    public ColorPalette(Context context, FragmentLocationViewBinding locationViewBinding, TYPE type) {
        this.context = context;
        this.type = type;
        this.locationViewBinding = locationViewBinding;
        isDark = false;
        setSensor();
        setTheme();
    }

    public ColorPalette(Context context,
                        PopupOptionsLayoutBinding popupOptionsLayoutBinding, TYPE type) {
        this.context = context;
        this.type = type;
        this.popupOptionsLayoutBinding = popupOptionsLayoutBinding;
        isDark = false;
        setSensor();
        setTheme();
    }

    public ColorPalette(Context context, ActivitySiteDetailBinding detailBinding, TYPE type) {
        this.context = context;
        this.type = type;
        this.detailBinding = detailBinding;
        isDark = false;
        setSensor();
        setTheme();
    }

    public ColorPalette(Context context, ActivityRegisterBinding registerBinding, TYPE type) {
        this.context = context;
        this.type = type;
        this.registerBinding = registerBinding;
        isDark = false;
        setSensor();
        setTheme();
    }

    public ColorPalette(Context context, ActivityRegister2Binding register2Binding, TYPE type) {
        this.context = context;
        this.type = type;
        this.register2Binding = register2Binding;
        isDark = false;
        setSensor();
        setTheme();
    }

    public ColorPalette(Context context, FragmentProfileBinding profileBinding, TYPE type) {
        this.context = context;
        this.type = type;
        this.profileBinding = profileBinding;
        setSensor();
        setTheme();
    }

    public ColorPalette(Context context, ActivityLoginBinding loginBinding, TYPE type) {
        this.context = context;
        this.type = type;
        this.loginBinding = loginBinding;
        setSensor();
        setTheme();
    }

    public ColorPalette(Context context, ItemViewBinding itemViewBinding, TYPE type) {
        this.context = context;
        this.type = type;
        this.itemViewBinding = itemViewBinding;
        setSensor();
        setTheme();
    }

    private void setSensor() {
        sensorManager = (SensorManager) context.getSystemService(Service.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }


    public void registerListener() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterListener() {
        sensorManager.unregisterListener(this);
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
                    if (event.values[0] < 1.0 && !isDark()) {
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
                    if (event.values[0] < 1.0 && !isDark()) {
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
                    if (event.values[0] < 1.0 && !isDark()) {
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
                    if (event.values[0] < 1.0 && !isDark()) {
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
                    if (event.values[0] < 1.0 && !isDark()) {
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
                    if (event.values[0] < 1.0 && !isDark()) {
                        setDark(true);
                        binding.setColorPalette(this);
                    } else if (event.values[0] >= 1.0 && isDark()) {
                        setDark(false);
                        binding.setColorPalette(this);
                    }
                    break;
                }
                case CONTACT: {
                    ContactParentItemBinding binding = contactBinding;
                    if (event.values[0] < 1.0 && !isDark()) {
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
                    if (event.values[0] < 1.0 && !isDark()) {
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
                    if (event.values[0] < 1.0 && !isDark()) {
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
                    if (event.values[0] < 1.0 && !isDark()) {
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
                    if (event.values[0] < 1.0 && !isDark()) {
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
                    if (event.values[0] < 1.0 && !isDark()) {
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
