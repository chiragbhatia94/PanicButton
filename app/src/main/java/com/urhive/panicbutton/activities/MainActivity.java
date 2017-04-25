package com.urhive.panicbutton.activities;

import android.content.Intent;
import android.database.Cursor;
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
import com.urhive.panicbutton.models.IceContact;
import com.urhive.panicbutton.services.EmergencyFloatingService;

import java.util.Arrays;
import java.util.Iterator;

public class MainActivity extends AppCompatBase {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE_PICK_CONTACTS = 11;

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

        providerList.append(getString(R.string.providers_used)).append(" ");

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

        Intent intent = new Intent(MainActivity.this, EmergencyFloatingService.class);
        startService(intent);
    }

    public void stopService(View view) {
        Log.i(TAG, "stopService: " + "clicked on stop service button");

        Intent intent = new Intent(MainActivity.this, EmergencyFloatingService.class);
        stopService(intent);

        Toast.makeText(this, R.string.panic_button_stopped, Toast.LENGTH_SHORT).show();
    }

    public void contactPicker(View view) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract
                .CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, REQUEST_CODE_PICK_CONTACTS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case REQUEST_CODE_PICK_CONTACTS:
                    Uri uriContact = data.getData();
                    setContactsInfo(uriContact);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     *
     * @param uri
     */
    private void setContactsInfo(Uri uri) {
        Cursor cursor;
        //Query the content uri
        cursor = getContentResolver().query(uri, null, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        // column index of the phone number
        int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        // column index of the contact name
        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int contactIdIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone
                .RAW_CONTACT_ID);
        /*String[] columns = cursor.getColumnNames();
        for (String column : columns) {
            Log.i(TAG, "setContactsInfo: " + column);
        }*/
        Log.i(TAG, "setContactsInfo: " + Arrays.toString(cursor.getColumnNames()));
        String photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds
                .Phone.PHOTO_URI));
        Log.i(TAG, "setContactsInfo: Thumbnail Uri " + photoUri);
        Log.i(TAG, "setContactsInfo: ContactId " + cursor.getInt(contactIdIndex));
        String phoneNo = cursor.getString(phoneIndex);
        String name = cursor.getString(nameIndex);

        IceContact contact = new IceContact(cursor.getInt(contactIdIndex), photoUri, cursor
                .getString(nameIndex), cursor.getString(phoneIndex));
        Log.i(TAG, "setContactsInfo: Selected contact is " + contact.toString());

        TextView textView1 = (TextView) findViewById(R.id.contactName);
        TextView textView2 = (TextView) findViewById(R.id.contactNumberTV);
        ImageView contactDPIV = (ImageView) findViewById(R.id.contactDPIV);

        // Set the value to the textviews
        textView1.setText(name);
        textView2.setText(phoneNo);
        if (photoUri != null) contactDPIV.setImageURI(Uri.parse(photoUri));
    }

    public void tempButton(View view) {
        DBHelper.updateDB();
    }

    public void tempButton2(View view) {
        Intent intent = new Intent(MainActivity.this, LockScreenActivity.class);
        startActivity(intent);
        /*Intent intent = new Intent(MainActivity.this, HardwareTriggerService.class);
        startService(intent);*/
    }
}
