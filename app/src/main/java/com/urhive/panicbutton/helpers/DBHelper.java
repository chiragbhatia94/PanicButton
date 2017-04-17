package com.urhive.panicbutton.helpers;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.urhive.panicbutton.models.EmergencyDetails;

import java.util.Map;

/**
 * Created by Chirag Bhatia on 17-04-2017.
 */

public class DBHelper {

    public static final String EMERGENCY = "emergency";
    private static final String TAG = "DBHelper";

    private void writeNewEmergency(EmergencyDetails emergencyDetails) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mFirebaseDatabaseReference = mFirebaseDatabase.getReference().child
                (EMERGENCY);

        Map<String, Object> postValues = emergencyDetails.toMap();

        Log.i(TAG, "writeNewEmergency: " + postValues.toString());
        /*Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);*/
    }
}
