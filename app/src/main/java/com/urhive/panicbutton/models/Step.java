package com.urhive.panicbutton.models;

import android.util.Log;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chirag Bhatia on 17-04-2017.
 */

public class Step {
    @Exclude
    public static final int PHOTO = 1;
    @Exclude
    public static final int TEXT = 2;
    String photo;
    String text;

    public Step() {

    }

    public Step(int PT, String string) {
        if (PT == PHOTO) {
            this.photo = string;
        } else if (PT == TEXT) {
            this.text = string;
        }
    }

    public Step(String photo, String text) {
        this.photo = photo;
        this.text = text;
    }

    // Map for JSON
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("photo", photo);
        result.put("text", text);
        Log.i("Step", "toMap: " + result.toString());
        return result;
    }

    /*@Override
    public String toString() {
        // return "Step{" + "photo='" + photo + '\'' + ", text='" + text + '\'' + '}';
        return "Step{" + "photo='" + photo + '\'' + ", text='" + text + '\'' + '}';
    }*/
}
