package com.urhive.panicbutton.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chirag Bhatia on 17-04-2017.
 */

public class Keywords {
    int rating;
    Map<String, Integer> related_diseases;

    // constructors
    public Keywords() {
    }

    public Keywords(int rating, Map<String, Integer> related_diseases) {
        this.rating = rating;
        this.related_diseases = related_diseases;
    }

    // getter and setters

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Map<String, Integer> getRelated_diseases() {
        return related_diseases;
    }

    public void setRelated_diseases(Map<String, Integer> related_diseases) {
        this.related_diseases = related_diseases;
    }

    // Map for JSON
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("rating", rating);
        result.put("related_diseases", related_diseases);
        return result;
    }
}
