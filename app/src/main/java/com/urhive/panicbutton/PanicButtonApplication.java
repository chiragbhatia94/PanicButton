package com.urhive.panicbutton;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Chirag Bhatia on 15-04-2017.
 */

public class PanicButtonApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Stetho.initializeWithDefaults(this);
    }
}
