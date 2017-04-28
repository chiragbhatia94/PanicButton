package com.urhive.panicbutton.activities;

import android.os.Bundle;

import com.urhive.panicbutton.R;

public class EditContactsActivity extends AppCompatBase {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contacts);

        setToolbar(getString(R.string.edit_contacts));
    }
}
