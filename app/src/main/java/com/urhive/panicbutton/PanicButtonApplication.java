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
        // SugarContext.init(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().getReference().keepSynced(false);
        Stetho.initializeWithDefaults(this);
    }
}
