package com.urhive.panicbutton.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.urhive.panicbutton.R;
import com.urhive.panicbutton.helpers.DBHelper;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.urhive.panicbutton.helpers.DBHelper.AGE_GENDER;


public class EditInfoActivity extends AppCompatBase {

    private static final String TAG = "EditInfoActivity";
    private static final int RC_CHOOSE_PHOTO = 12;

    private TextView ageGenderTV, nameTV, addressTV, bloodGroupTV, medicalNotesTV;
    private CircleImageView iv;
    private ProgressBar progressBar;

    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        prefs = PreferenceManager.getDefaultSharedPreferences(EditInfoActivity.this);

        setToolbar(getString(R.string.emergency_information));

        RelativeLayout addressRL = (RelativeLayout) findViewById(R.id.addressRL);
        RelativeLayout bloodGroupRL = (RelativeLayout) findViewById(R.id.bloodGroupRL);
        RelativeLayout medicalNotesRL = (RelativeLayout) findViewById(R.id.medicalNotesRL);

        iv = (CircleImageView) findViewById(R.id.profileIV);

        nameTV = (TextView) findViewById(R.id.nameTV);
        ageGenderTV = (TextView) findViewById(R.id.ageGenderTV);
        addressTV = (TextView) findViewById(R.id.setAddressTV);
        bloodGroupTV = (TextView) findViewById(R.id.bloodGroupTV);
        medicalNotesTV = (TextView) findViewById(R.id.setMedicalNotesTV);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        assert mFirebaseUser != null;
        if (mFirebaseUser.getPhotoUrl() != null) {
            Glide.with(EditInfoActivity.this).load(mFirebaseUser.getPhotoUrl()).crossFade()
                    .fitCenter().into(iv);
        }
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media
                        .EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RC_CHOOSE_PHOTO);
            }
        });

        nameTV.setText(mFirebaseUser.getDisplayName());
        nameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(EditInfoActivity.this).title(R.string.enter_name)
                        .inputType(InputType.TYPE_CLASS_TEXT | InputType
                                .TYPE_TEXT_VARIATION_PERSON_NAME).input(getString(R.string
                        .name_hint), nameTV.getText().toString(), new MaterialDialog
                        .InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest
                                .Builder().setDisplayName(input.toString()).build();

                        mFirebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated.");
                                    nameTV.setText(mFirebaseUser.getDisplayName());
                                }
                            }
                        });
                    }
                }).show();
            }
        });

        ageGenderTV.setText(prefs.getString(AGE_GENDER, getString(R.string.age_gender)));
        ageGenderTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(EditInfoActivity.this).title(R.string.enter_name)
                        .inputType(InputType.TYPE_CLASS_TEXT | InputType
                                .TYPE_TEXT_VARIATION_PERSON_NAME).input(getString(R.string
                        .age_gender), ageGenderTV.getText().toString(), new MaterialDialog
                        .InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        prefs.edit().putString(DBHelper.AGE_GENDER, input.toString()).apply();
                        ageGenderTV.setText(input);
                    }
                }).show();
            }
        });

        addressTV.setText(prefs.getString(DBHelper.ADDRESS, getString(R.string.unknown)));
        addressRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(EditInfoActivity.this).title(R.string.enter_address)
                        .inputType(InputType.TYPE_CLASS_TEXT | InputType
                                .TYPE_TEXT_VARIATION_POSTAL_ADDRESS).input(getString(R.string
                        .address_hint), addressTV.getText().toString(), new MaterialDialog
                        .InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        prefs.edit().putString(DBHelper.ADDRESS, input.toString()).apply();
                        addressTV.setText(input);
                    }
                }).show();

            }
        });

        bloodGroupTV.setText(prefs.getString(DBHelper.BLOOD_GROUP, getString(R.string
                .unspecified)));
        bloodGroupRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(EditInfoActivity.this).title(R.string
                        .select_blood_group).items(R.array.blood_groups)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog
                                .ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which,
                                               CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to
                         * actually be selected.
                         **/

                        prefs.edit().putString(DBHelper.BLOOD_GROUP, text.toString()).apply();
                        bloodGroupTV.setText(text);
                        return true;
                    }
                }).positiveText(R.string.choose).show();

            }
        });

        medicalNotesTV.setText(prefs.getString(DBHelper.MEDICAL_NOTES, getString(R.string
                .unknown)));
        medicalNotesRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(EditInfoActivity.this).title(R.string.medical_notes)
                        .inputType(InputType.TYPE_CLASS_TEXT | InputType
                                .TYPE_TEXT_VARIATION_PERSON_NAME).input(getString(R.string
                        .medical_notes_hint), medicalNotesTV.getText().toString(), new
                        MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        prefs.edit().putString(DBHelper.MEDICAL_NOTES, input.toString()).apply();
                        medicalNotesTV.setText(input);
                    }
                }).show();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RC_CHOOSE_PHOTO:
                    Uri selectedImage = data.getData();
                    uploadPhoto(selectedImage);
            }
        } else {
            switch (requestCode) {
                case RC_CHOOSE_PHOTO:
                    Toast.makeText(this, R.string
                            .there_is_some_problem_try_again_later_check_for_permissions, Toast
                            .LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void uploadPhoto(Uri selectedImage) {
        // Upload to Cloud Storage
        StorageReference imgRef = FirebaseStorage.getInstance().getReference().child(DBHelper
                .USERS).child(mFirebaseUser.getUid());
        progressBar.setVisibility(View.VISIBLE);
        imgRef.putFile(selectedImage).addOnSuccessListener(this, new OnSuccessListener<UploadTask
                .TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Upload to Cloud Storage
                @SuppressWarnings("VisibleForTests") UserProfileChangeRequest profileUpdates =
                        new UserProfileChangeRequest.Builder().setPhotoUri(taskSnapshot
                                .getDownloadUrl()).build();

                mFirebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Glide.with(EditInfoActivity.this).load(mFirebaseUser.getPhotoUrl())
                                    .crossFade().fitCenter().into(iv);
                            Log.d(TAG, "User profile updated.");
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(EditInfoActivity.this, R.string.image_uploaded, Toast
                                    .LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditInfoActivity.this, R.string
                        .there_is_some_problem_try_again_later, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
