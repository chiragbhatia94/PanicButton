package com.urhive.panicbutton.fragments;

import android.support.v4.app.Fragment;

import com.urhive.panicbutton.fragments.controllers.BackPressImpl;
import com.urhive.panicbutton.interfaces.OnBackPressListener;

/**
 * Created by Chirag Bhatia on 21-04-2017.
 */

public class FragmentBase extends Fragment implements OnBackPressListener {
    @Override
    public boolean onBackPressed() {
        return new BackPressImpl(this).onBackPressed();
    }
}
