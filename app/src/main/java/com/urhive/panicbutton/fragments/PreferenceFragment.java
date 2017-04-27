package com.urhive.panicbutton.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.TwoStatePreference;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.urhive.panicbutton.R;
import com.urhive.panicbutton.activities.ReplacementActivity;
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

        /*Preference setupICEContactsBtn = findPreference("setupICEContactsBtn");
        setupICEContactsBtn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener
        () {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity(), "this is awesome", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        Preference setupProfileBtn = findPreference("setupProfileBtn");
        setupProfileBtn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity(), "this is awesome2", Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/

        /*final Preference showPanicBtnOnLockScreen = findPreference("showPanicBtnOnLockScreen");
        showPanicBtnOnLockScreen.setOnPreferenceClickListener(new Preference
        .OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                return true;
            }
        });*/

        final TwoStatePreference showPanicButtonSwitch = (TwoStatePreference) findPreference
                ("showPanicBtnOnLockScreen");
        showPanicButtonSwitch.setOnPreferenceClickListener(new Preference
                .OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (showPanicButtonSwitch.isChecked()) {
                    Log.i(TAG, "onPreferenceClick: checked");
                    UIHelper.startPanicButtonService(getActivity());
                } else {
                    Log.i(TAG, "onPreferenceClick: unchecked");
                    UIHelper.stopPanicButtonService(getActivity());
                }
                return false;
            }
        });
        Preference developersBtn = findPreference("developersBtn");
        developersBtn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "developers");
                UIHelper.startActivity(getActivity(), ReplacementActivity.class, bundle);
                return true;
            }
        });

        final Preference aboutBtn = findPreference("aboutBtn");
        aboutBtn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "about");
                UIHelper.startActivity(getActivity(), ReplacementActivity.class, bundle);
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
                UIHelper.startActivity(getActivity(), ReplacementActivity.class, bundle);
                return true;
            }
        });

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
    }
}