package com.urhive.panicbutton.activities;

import android.content.ContentUris;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.urhive.panicbutton.R;
import com.urhive.panicbutton.helpers.DBHelper;
import com.urhive.panicbutton.models.Emergency;
import com.urhive.panicbutton.models.Keywords;
import com.urhive.panicbutton.models.Step;
import com.urhive.panicbutton.services.EmergencyButtonService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatBase {

    private static final String TAG = "MainActivity";

    private static final int RESULT_PICK_CONTACT = 850;
    private TextView signinStatusTV;
    private ImageView profilePictureIV;
    private TextView emailTV, displayNameTV, enabledProvidersTV;

    private void initViews() {
        signinStatusTV = (TextView) findViewById(R.id.signinStatusTV);
        profilePictureIV = (ImageView) findViewById(R.id.user_profile_picture);
        emailTV = (TextView) findViewById(R.id.user_email);
        displayNameTV = (TextView) findViewById(R.id.user_display_name);
        enabledProvidersTV = (TextView) findViewById(R.id.user_enabled_providers);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setProfile(mFirebaseUser);
    }

    private void setProfile(FirebaseUser user) {
        if (user.getPhotoUrl() != null) {
            Glide.with(this).load(user.getPhotoUrl()).fitCenter().into(profilePictureIV);
        }

        emailTV.setText(TextUtils.isEmpty(user.getEmail()) ? getString(R.string.no_email) : user
                .getEmail());
        displayNameTV.setText(TextUtils.isEmpty(user.getDisplayName()) ? getString(R.string
                .no_display_name) : user.getDisplayName());

        StringBuilder providerList = new StringBuilder(100);

        providerList.append(getString(R.string.providers_used) + " ");

        if (user.getProviders() == null || user.getProviders().isEmpty()) {
            providerList.append(getString(R.string.none));
        } else {
            Iterator<String> providerIter = user.getProviders().iterator();
            while (providerIter.hasNext()) {
                String provider = providerIter.next();
                if (GoogleAuthProvider.PROVIDER_ID.equals(provider)) {
                    providerList.append(getString(R.string.google));
                } else if (FacebookAuthProvider.PROVIDER_ID.equals(provider)) {
                    providerList.append(getString(R.string.facebook));
                } else if (EmailAuthProvider.PROVIDER_ID.equals(provider)) {
                    providerList.append(getString(R.string.password));
                } else {
                    providerList.append(provider);
                }

                if (providerIter.hasNext()) {
                    providerList.append(", ");
                }
            }
        }

        enabledProvidersTV.setText(providerList);

        signinStatusTV.setText(getString(R.string.welcome) + ", " + mFirebaseUser.getDisplayName
                () + "!");
    }

    public void startService(View view) {
        Log.i(TAG, "startService: " + "clicked on startService button");

        Intent intent = new Intent(MainActivity.this, EmergencyButtonService.class);
        startService(intent);
    }

    public void stopService(View view) {
        Log.i(TAG, "stopService: " + "clicked on stop service button");

        Intent intent = new Intent(MainActivity.this, EmergencyButtonService.class);
        stopService(intent);

        Toast.makeText(this, R.string.panic_button_stopped, Toast.LENGTH_SHORT).show();
    }

    public void contactPicker(View view) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract
                .CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     *
     * @param data
     */
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone
                    .DISPLAY_NAME);

            //int photoIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.)
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);

            TextView textView1 = (TextView) findViewById(R.id.contactName);
            TextView textView2 = (TextView) findViewById(R.id.contactNumberTV);

            // for contact DP
            // not working
            int idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
            /*Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                    idIndex);
            Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo
                    .CONTENT_DIRECTORY);
            InputStream is = ContactsContract.Contacts.openContactPhotoInputStream
            (getContentResolver(), contactUri, false);
            Bitmap bmp = BitmapFactory.decodeStream(is);*/
            Bitmap bmp = BitmapFactory.decodeStream(openPhoto(idIndex));
            CircleImageView circleImageView = (CircleImageView) findViewById(R.id.contactDPIV);
            circleImageView.setImageBitmap(bmp);

            // Set the value to the textviews
            textView1.setText(name);
            textView2.setText(phoneNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // for photo thumbnail
    public InputStream openPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo
                .CONTENT_DIRECTORY);
        Cursor cursor = getContentResolver().query(photoUri, new String[]{ContactsContract
                .Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return new ByteArrayInputStream(data);
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    // for larger photo
    public InputStream openDisplayPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                contactId);
        Uri displayPhotoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo
                .DISPLAY_PHOTO);
        try {
            AssetFileDescriptor fd = getContentResolver().openAssetFileDescriptor
                    (displayPhotoUri, "r");
            return fd.createInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    public void tempButton(View view) {
        String name = "Heavy bleeding";
        String doctor_category = "Orthopediac";
        Map<String, Boolean> bodypart = new HashMap<>();
        bodypart.put("abdomen", true);
        bodypart.put("chest", true);
        bodypart.put("hands", true);
        bodypart.put("head", true);
        bodypart.put("leg", true);
        bodypart.put("neck", true);

        Map<String, Integer> keyword = new HashMap<>();
        keyword.put("abrasion", 12);
        keyword.put("cut", 12);

        String photo = "https://firebasestorage.googleapis.com/v0/b/panicbutton-467cb.appspot" +
                ".com/o/step%20photo%2FBleeding%2FBleedingStep1.PNG?alt=media&token=bfc15a71-b76e" +
                "" + "" + "" + "" + "-482a-9b79-a03e2d9509c4";

        Map<String, Integer> related_diseases = new HashMap<>();
        related_diseases.put("amputation", 20);

        List<Step> steps = new ArrayList<>();
        steps.add(new Step("https://firebasestorage.googleapis.com/v0/b/panicbutton-467cb" + "" +
                ".appspot.com/o/step%20photo%2FBleeding%2FBleedingStep1.PNG?alt=media&token" +
                "=bfc15a71-b76e" + "-482a-9b79-a03e2d9509c4", "Apply direct pressure on cut or "
                + "wound with clear"));
        steps.add(new Step("url", "text"));
        steps.add(new Step(Step.TEXT, "text"));
        steps.add(new Step(Step.PHOTO, "url"));

        Emergency amputation = new Emergency(name, doctor_category, bodypart, keyword, photo,
                related_diseases, steps);

        mFirebaseDatabaseReference.child(DBHelper.EMERGENCY).child(name).setValue(amputation
                .toMap());

        String keywordName = "cut";
        int keywordRating = 12;
        Map<String, Integer> related_diseases2 = new HashMap<>();
        related_diseases2.put("amputation", 25);
        related_diseases2.put("bleeding", 50);
        Keywords keywords = new Keywords(keywordRating, related_diseases2);
        mFirebaseDatabaseReference.child(DBHelper.KEYWORDS).child(keywordName).setValue(keywords
                .toMap());
    }

    public void tempButton2(View view) {
        /*mFirebaseDatabaseReference.child(DBHelper.EMERGENCY).child("amputation")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Emergency ed = dataSnapshot.getValue(Emergency.class);
            }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        mFirebaseDatabaseReference.child(DBHelper.KEYWORDS).child("abaration")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                *//*Keywords keywords = dataSnapshot.getValue(Keywords.class);*//*
                Object obj = dataSnapshot.getValue();
                Log.i(TAG, "onDataChange: " + obj.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        mFirebaseDatabaseReference.child(DBHelper.EMERGENCY);
    }
}
