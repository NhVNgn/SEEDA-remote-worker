package com.sereem.remoteworker.ui.ui_for_main.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sereem.remoteworker.R;
import com.sereem.remoteworker.databinding.FragmentProfileBinding;
import com.sereem.remoteworker.ui.CustomSnackbar;
import com.sereem.remoteworker.model.Database;
import com.sereem.remoteworker.model.User;
import com.sereem.remoteworker.ui.ColorPalette;
import com.sereem.remoteworker.ui.MainActivity;
import com.google.android.material.snackbar.Snackbar;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * ProfileFragment class, used for displaying and editing user information
 */
public class ProfileFragment extends Fragment {

    private User user;
    private Database db;

    private EditText emailEdit, firstNameEdit, lastNameEdit, birthdayEdit, phoneEdit,
            companyIdEdit, emFirstNameEdit, emLastNameEdit, emPhoneEdit, emRelationEdit, medicalConsider;
    private ImageButton editBtn1, cancelBtn1, editBtn2, cancelBtn2, editBtn3, cancelBtn3,
                        editBtn4, cancelBtn4;
    private ImageView icon;
    private Button changePassButton;
    private AtomicInteger btnCount1, btnCount2, btnCount3, btnCount4;
    private List<EditText> editTextList1, editTextList2, editTextList3, editTextList4;
    private Drawable addIcon;

    private Uri resultUri;
    boolean isIconUpdated;
    private Snackbar snackbar;

    private ColorPalette colorPalette;

    private View root;

    private DocumentReference documentReference;
    private StorageReference storageReference;
    private File iconFile;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile, container, false);

        FragmentProfileBinding binding = FragmentProfileBinding.bind(root);
        colorPalette = new ColorPalette(getContext(), binding, ColorPalette.TYPE.PROFILE);
        binding.setColorPalette(colorPalette);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        user = User.getInstance();
        iconFile = new File(getActivity().getCacheDir() + "/" + user.getUID() + ".jpeg");

        initializeDocumentReference();
        initializeStorageReference();

        db = new Database(root.getContext());

        isIconUpdated = false;

        addIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_add_blue, null);

        initializeVariables();

        setupEditAction(editTextList1, editBtn1, cancelBtn1, btnCount1, 1);
        setupEditAction(editTextList2, editBtn2, cancelBtn2, btnCount2, 2);
        setupEditAction(editTextList3, editBtn3, cancelBtn3, btnCount3, 3);
        setupEditAction(editTextList4, editBtn4, cancelBtn4, btnCount4, 4);

        setupChangePasswordButton();

        setupIconAction();

        updateUI();

        return root;
    }

    private void setupChangePasswordButton() {
        PopupChangePassword dialogue = new PopupChangePassword();
        changePassButton.setOnClickListener(v -> {
            dialogue.showDialog(getFragmentManager());
            FirebaseAuth.getInstance().sendPasswordResetEmail(user.getEmail())
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            dialogue.setMessage("The link was sent to:", user.getEmail());
                        } else {
                            dialogue.setMessage("Error occurred. Try again later.", null);
                        }
                    });
        });
    }

    private void initializeDocumentReference() {
        SharedPreferences prefs = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        String UID = prefs.getString("UID", "");
        documentReference = FirebaseFirestore.getInstance().document(
                "/users/" + UID + "/");
    }

    private void initializeStorageReference() {
        storageReference = FirebaseStorage.getInstance().getReference("profileIcons/" +
                user.getUID() + ".jpeg");
    }

    private void initializeVariables() {
        // First block
        btnCount1 = new AtomicInteger();

        icon = root.findViewById(R.id.profileIconProfile);

        emailEdit = root.findViewById(R.id.emailProfileEdit);

        editBtn1 = root.findViewById(R.id.editButtonProfile1);
        cancelBtn1 = root.findViewById(R.id.cancelButtonProfile1);

        editTextList1 = new ArrayList<>();

        changePassButton = root.findViewById(R.id.buttonChangePassword);


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

        //Medical Considerations
        btnCount4 = new AtomicInteger();

        medicalConsider = root.findViewById(R.id.medicalConsiderProfileEdit);

        editBtn4 = root.findViewById(R.id.editButtonProfile4);
        cancelBtn4 = root.findViewById(R.id.cancelButtonProfile4);

        editTextList4 = new ArrayList<>();
        editTextList4.add(medicalConsider);
    }

    private void setupEditAction(List<EditText> editTextList, ImageButton editBtn,
                                 ImageButton cancelBtn, AtomicInteger btnCount, int numOfBlock) {
        editBtn.setOnClickListener(v -> {
            snackbar = CustomSnackbar.create(getView());
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
                if(numOfBlock == 1) {
                    icon.setImageAlpha(255);
                    icon.setForeground(null);
                    if(isIconUpdated) {
                        try {
                            saveIcon(resultUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        updateIcon();
                    }
                } else {
                    updateDataInDatabase1();
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
                ImageViewFragment fragment = new ImageViewFragment(MainActivity.iconUri);
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
                MainActivity.iconUri = resultUri;
                isIconUpdated = true;
                updateIcon();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                error.printStackTrace();
            }
        }
    }

    private void updateUI() {
        updateIcon();
        emailEdit.setText(user.getEmail());
        firstNameEdit.setText(user.getFirstName());
        lastNameEdit.setText(user.getLastName());
        birthdayEdit.setText(user.getBirthday());
        phoneEdit.setText(user.getPhone());
        companyIdEdit.setText(user.getCompanyID());
        emFirstNameEdit.setText(user.getEmFirstName());
        emLastNameEdit.setText(user.getEmLastName());
        emPhoneEdit.setText(user.getEmPhone());
        emRelationEdit.setText(user.getEmRelation());
        medicalConsider.setText(user.getMedicalConsiderations());
    }

    private void updateIcon() {
        View headerView = MainActivity.getHeaderView();
        ImageView iconMain = headerView.findViewById(R.id.profileIconMain);
        if(user.getIconUri() != null && !user.getIconUri().equals("")) {
            icon.setImageURI(MainActivity.iconUri);
            iconMain.setImageURI(MainActivity.iconUri);
        } else {
            icon.setImageResource(R.drawable.profile_icon);
        }
    }

    private void updateDataInDatabase1() {
        documentReference.set(User.createUserForSaving(
                user.getUID(),
                companyIdEdit.getText().toString(),
                firstNameEdit.getText().toString(),
                lastNameEdit.getText().toString(),
                emailEdit.getText().toString(),
                phoneEdit.getText().toString(),
                birthdayEdit.getText().toString(),
                emFirstNameEdit.getText().toString(),
                emLastNameEdit.getText().toString(),
                emPhoneEdit.getText().toString(),
                emRelationEdit.getText().toString(),
                medicalConsider.getText().toString(),
                user.getIconUri(),
                user.getWorksites())).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                snackbar.setText("Saved").show();
            } else {
                snackbar.setText("Error occurred").show();
            }
        });
    }

    private void saveIcon(Uri uri) throws IOException {
        if(isIconUpdated) {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),
                    uri);
            iconFile.delete();
            iconFile.createNewFile();
            FileOutputStream fout = new FileOutputStream(iconFile);
            bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fout);
            storageReference.putFile(Uri.fromFile(iconFile)).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    storageReference.getDownloadUrl().addOnSuccessListener(uri1 -> {
                        user.setIconUri(uri1.toString());
                        updateDataInDatabase1();
                        updateIcon();
                    });
                }
            });
            isIconUpdated = false;
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