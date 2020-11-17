package com.example.project.ui.ui_for_main.profile;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.project.R;
import com.example.project.databinding.FragmentProfileBinding;
import com.example.project.model.Constants;
import com.example.project.model.Database;
import com.example.project.model.User;
import com.example.project.ui.ColorPalette;
import com.example.project.ui.MainActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private User user;
    private Database db;

    private EditText emailEdit, passwordEdit, firstNameEdit, lastNameEdit, birthdayEdit, phoneEdit,
            companyIdEdit, emFirstNameEdit, emLastNameEdit, emPhoneEdit, emRelationEdit;
    private ImageButton editBtn1, cancelBtn1, editBtn2, cancelBtn2, editBtn3, cancelBtn3;
    private ImageView icon;
    private AtomicInteger btnCount1, btnCount2, btnCount3;
    private List<EditText> editTextList1, editTextList2, editTextList3;
    private Drawable addIcon;

    private Uri resultUri;
    boolean isIconUpdated;
    private Snackbar snackbar;

    private ColorPalette colorPalette;

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);

        FragmentProfileBinding binding = FragmentProfileBinding.bind(root);
        colorPalette = new ColorPalette(getContext(), binding, ColorPalette.TYPE.PROFILE);
        binding.setColorPalette(colorPalette);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        db = new Database(root.getContext());

        isIconUpdated = false;

        addIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_add_blue, null);

        updateUser();
        initializeVariables();

        setupEditAction(editTextList1, editBtn1, cancelBtn1, btnCount1, 1);
        setupEditAction(editTextList2, editBtn2, cancelBtn2, btnCount2, 2);
        setupEditAction(editTextList3, editBtn3, cancelBtn3, btnCount3, 3);

        setupIconAction();

        updateUI();

        return root;
    }

    private void createSnackBar() {
        snackbar = Snackbar.make(getView(), "", Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(Color.parseColor("#204E75"))
                .setTextColor(Color.WHITE);
    }

    private void updateUser() {
        SharedPreferences prefs = getActivity().getSharedPreferences(
                "user", Context.MODE_PRIVATE);

        Integer id = prefs.getInt("id", -1);

        user = db.getUser(id);
    }

    private void initializeVariables() {
        // First block
        btnCount1 = new AtomicInteger();

        icon = root.findViewById(R.id.profileIconProfile);

        emailEdit = root.findViewById(R.id.emailProfileEdit);
        passwordEdit = root.findViewById(R.id.passwordProfileEdit);

        editBtn1 = root.findViewById(R.id.editButtonProfile1);
        cancelBtn1 = root.findViewById(R.id.cancelButtonProfile1);

        editTextList1 = new ArrayList<>();
        editTextList1.add(emailEdit);
        editTextList1.add(passwordEdit);

        // Second Block
        btnCount2 = new AtomicInteger();

        firstNameEdit = root.findViewById(R.id.firstNameProfileEdit);
        lastNameEdit = root.findViewById(R.id.lastNameProfileEdit);
        birthdayEdit = root.findViewById(R.id.birthdayProfileEdit);
        phoneEdit = root.findViewById(R.id.phoneProfileEdit);
        companyIdEdit = root.findViewById(R.id.companyIdProfileEdit);

        editBtn2 = root.findViewById(R.id.editButtonProfile2);
        cancelBtn2 = root.findViewById(R.id.cancelButtonProfile2);

        editTextList2 = new ArrayList<>();
        editTextList2.add(firstNameEdit);
        editTextList2.add(lastNameEdit);
        editTextList2.add(birthdayEdit);
        editTextList2.add(phoneEdit);
        editTextList2.add(companyIdEdit);

        //Third block
        btnCount3 = new AtomicInteger();

        emFirstNameEdit = root.findViewById(R.id.emFirstNameProfileEdit);
        emLastNameEdit = root.findViewById(R.id.emLastNameProfileEdit);
        emPhoneEdit = root.findViewById(R.id.emPhoneProfileEdit);
        emRelationEdit = root.findViewById(R.id.emRelationProfileEdit);

        editBtn3 = root.findViewById(R.id.editButtonProfile3);
        cancelBtn3 = root.findViewById(R.id.cancelButtonProfile3);

        editTextList3 = new ArrayList<>();
        editTextList3.add(emFirstNameEdit);
        editTextList3.add(emLastNameEdit);
        editTextList3.add(emPhoneEdit);
        editTextList3.add(emRelationEdit);
    }

    private void setupEditAction(List<EditText> editTextList, ImageButton editBtn,
                                 ImageButton cancelBtn, AtomicInteger btnCount, int numOfBlock) {
        editBtn.setOnClickListener(v -> {
            createSnackBar();
            if(btnCount.get() % 2 == 0) {
                for(EditText editText : editTextList) {
                    makeTextEditable(editText);
                }
                cancelBtn.setVisibility(View.VISIBLE);
                if(colorPalette.isDark()) {
                    editBtn.setImageResource(R.drawable.ic_save_dark);
                } else {
                    editBtn.setImageResource(R.drawable.ic_save_white);
                }
                if(numOfBlock == 1) {
                    icon.setImageAlpha(50);
                    icon.setForeground(addIcon);
                }
            } else {
                if(!isValidMail(emailEdit.getText().toString())) {
                    snackbar.setText(getString(R.string.invalid_email))
                            .setDuration(Snackbar.LENGTH_LONG)
                            .show();
                    return;
                } if(!isValidMobile(phoneEdit.getText().toString())) {
                    snackbar.setText(getString(R.string.invalid_number))
                            .setDuration(Snackbar.LENGTH_LONG)
                            .show();
                }
                for(EditText editText : editTextList) {
                    makeTextNotEditable(editText);
                }
                cancelBtn.setVisibility(View.GONE);
                if(colorPalette.isDark()) {
                    editBtn.setImageResource(R.drawable.ic_edit_dark);
                } else {
                    editBtn.setImageResource(R.drawable.ic_edit_white);
                }
                updateDataInDatabase1(numOfBlock);
                updateUser();
                updateNavView();
                if(numOfBlock == 1) {
                    icon.setImageAlpha(255);
                    icon.setForeground(null);
                    updateIcon();
                }
            }
            btnCount.incrementAndGet();
            isIconUpdated = false;
        });

        cancelBtn.setOnClickListener(v -> {
            for(EditText editText : editTextList) {
                makeTextNotEditable(editText);
            }
            cancelBtn.setVisibility(View.GONE);
            if(colorPalette.isDark()) {
                editBtn.setImageResource(R.drawable.ic_edit_dark);
            } else {
                editBtn.setImageResource(R.drawable.ic_edit_white);
            }
            btnCount.incrementAndGet();
            snackbar.setText("Cancelled").setDuration(Snackbar.LENGTH_SHORT).show();
            icon.setImageAlpha(255);
            icon.setForeground(null);
            updateUI();
            isIconUpdated = false;
        });
    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    private void setupIconAction() {
        icon.setOnClickListener(v -> {
            if(btnCount1.get() % 2 != 0) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setMultiTouchEnabled(true)
                        .setAllowFlipping(false)
                        .setAllowRotation(false)
                        .setAllowCounterRotation(false)
                        .setFixAspectRatio(true)
                        .start(getContext(), this);
            } else {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                ImageViewFragment fragment = new ImageViewFragment(user.getIconUri());
                fragment.show(manager, "MassageDialogue");
            }
        });
    }

    private void makeTextEditable(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.setTextColor(getContext().getColor(R.color.text_light));
        editText.setBackgroundResource(android.R.drawable.edit_text);
    }

    private void makeTextNotEditable(EditText editText) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
        editText.setBackgroundResource(0);
        editText.setPadding(0, 0, 0 ,0);
        if(colorPalette.isDark()) {
            editText.setTextColor(getContext().getColor(R.color.text_dark));
        } else {
            editText.setTextColor(getContext().getColor(R.color.text_light));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                icon.setImageURI(resultUri);
                isIconUpdated = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.printStackTrace();
            }
        }
    }

    private void updateUI() {
        updateIcon();
        emailEdit.setText(user.getEmail());
        passwordEdit.setText(user.getPassword());
        firstNameEdit.setText(user.getFirstName());
        lastNameEdit.setText(user.getLastName());
        birthdayEdit.setText(user.getBirthday());
        phoneEdit.setText(user.getPhone());
        companyIdEdit.setText(user.getCompanyID());
        emFirstNameEdit.setText(user.getEmFirstName());
        emLastNameEdit.setText(user.getEmLastName());
        emPhoneEdit.setText(user.getEmPhone());
        emRelationEdit.setText(user.getEmRelation());
    }

    private void updateIcon() {
        if(user.getIconUri() != null && !user.getIconUri().toString().equals("")) {
            icon.setImageURI(user.getIconUri());
        } else {
            icon.setImageResource(R.drawable.profile_icon);
        }
    }

    private void updateNavView() {
        View headerView = MainActivity.getHeaderView();
        ImageView icon = headerView.findViewById(R.id.profileIconMain);
        if(user.getIconUri() != null) {
            icon.setImageURI(user.getIconUri());
        } else {
            icon.setImageResource(R.drawable.profile_icon);
        }
        TextView emailText = headerView.findViewById(R.id.emailMainTextView);
        emailText.setText(user.getEmail());
        TextView nameText = headerView.findViewById(R.id.nameMainTextView);
        nameText.setText(user.getFirstName() + " " + user.getLastName());
    }

    private void updateDataInDatabase1(int numOfBlock) {
        int result = 0;
        if(numOfBlock == 1) {
            ContentValues cv = new ContentValues();
            cv.put(Constants.EMAIL, emailEdit.getText().toString());
            cv.put(Constants.PASSWORD, passwordEdit.getText().toString());
//            saveIcon(cv);
            if(isIconUpdated) {
                cv.put(Constants.ICON_URI, resultUri.toString());
            }
            result = db.update(user.getId(), cv);
        } else if(numOfBlock == 2) {
            ContentValues cv = new ContentValues();
            cv.put(Constants.FIRST_NAME, firstNameEdit.getText().toString());
            cv.put(Constants.LAST_NAME, lastNameEdit.getText().toString());
            cv.put(Constants.BIRTHDAY, birthdayEdit.getText().toString());
            cv.put(Constants.PHONE, phoneEdit.getText().toString());
            cv.put(Constants.COMPANY_ID, companyIdEdit.getText().toString());
            result = db.update(user.getId(), cv);
        } else if(numOfBlock == 3) {
            ContentValues cv = new ContentValues();
            cv.put(Constants.EM_FIRST_NAME, emFirstNameEdit.getText().toString());
            cv.put(Constants.EM_LAST_NAME, emLastNameEdit.getText().toString());
            cv.put(Constants.EM_PHONE, emPhoneEdit.getText().toString());
            cv.put(Constants.EM_RELATION, emRelationEdit.getText().toString());
            result = db.update(user.getId(), cv);
        }

        if(result > 0) {
            snackbar.setText("Saved").setDuration(Snackbar.LENGTH_SHORT).show();
            updateSharedPrefs(numOfBlock);
        } else {
            snackbar.setText("Cancelled").setDuration(Snackbar.LENGTH_SHORT).show();
        }
    }

//    private void saveIcon(ContentValues cv) {
//        if(isIconUpdated) {
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
//                        getActivity().getContentResolver(), resultUri);
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
//                byte[] iconInBytes = stream.toByteArray();
//                cv.put(Constants.ICON_RES, iconInBytes);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private void updateSharedPrefs(int numOfBlock) {
        if(numOfBlock == 1) {
            SharedPreferences prefs = getActivity().getSharedPreferences("user",
                    Context.MODE_PRIVATE);
            prefs.edit().remove("email").remove("password")
                    .putString("email", emailEdit.getText().toString())
                    .putString("password", passwordEdit.getText().toString())
                    .apply();
        }
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