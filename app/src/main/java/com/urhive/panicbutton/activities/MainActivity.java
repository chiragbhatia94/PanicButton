package com.urhive.panicbutton.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.urhive.panicbutton.services.EmergencyButtonService;

import java.util.Iterator;

public class MainActivity extends AppCompatBase {

    private static final String TAG = "MainActivity";

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
}
