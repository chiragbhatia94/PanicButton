package com.urhive.panicbutton.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.urhive.panicbutton.R;

/**
 * Created by Chirag Bhatia on 07-04-2017.
 */

public class PreferenceFragment extends android.preference.PreferenceFragment {
    private static final String TAG = "PreferenceFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

        Preference signOutBtn = findPreference("signOutBtn");
        signOutBtn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                FirebaseAuth.getInstance().signOut();
                // TODO: 24-04-2017
                // if there is any service running stop it here

                Log.i(TAG, "onPreferenceClick: User signed out!");
                return true;
            }
        });

        Preference developersBtn = findPreference("developersBtn");
        developersBtn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity(), "This is wow!", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}