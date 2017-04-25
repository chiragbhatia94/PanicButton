package com.urhive.panicbutton.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.urhive.panicbutton.R;
import com.urhive.panicbutton.activities.OpenSourceLibrariesActivity;
import com.urhive.panicbutton.helpers.UIHelper;

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
                return true;
            }
        });

        final Preference aboutBtn = findPreference("aboutBtn");
        aboutBtn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "about");
                UIHelper.startActivity(getActivity(), OpenSourceLibrariesActivity.class, bundle);
                return true;
            }
        });

        Preference openSourceLibraryBtn = findPreference("openSourceLibraryBtn");
        openSourceLibraryBtn.setOnPreferenceClickListener(new Preference
                .OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "openSourceLibrary");
                UIHelper.startActivity(getActivity(), OpenSourceLibrariesActivity.class, bundle);
                return true;
            }
        });
    }
}