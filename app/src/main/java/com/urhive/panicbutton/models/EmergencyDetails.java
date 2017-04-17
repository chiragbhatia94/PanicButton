package com.urhive.panicbutton.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chirag Bhatia on 17-04-2017.
 */

public class EmergencyDetails {
    Map<String, Boolean> bodypart;
    Map<String, Integer> keyword;
    String photo;
    Map<String, Integer> related_diseases;
    Map<String, Step> steps;

    // Constructors
    public EmergencyDetails() {

    }

    public EmergencyDetails(Map<String, Boolean> bodypart, Map<String, Integer> keyword, String
            photo, Map<String, Integer> related_diseases, Map<String, Step> steps) {
        this.bodypart = bodypart;
        this.keyword = keyword;
        this.photo = photo;
        this.related_diseases = related_diseases;
        this.steps = steps;
    }

    // Getter & Setter
    public Map<String, Boolean> getBodypart() {
        return bodypart;
    }

    public void setBodypart(Map<String, Boolean> bodypart) {
        this.bodypart = bodypart;
    }

    public Map<String, Integer> getKeyword() {
        return keyword;
    }

    public void setKeyword(Map<String, Integer> keyword) {
        this.keyword = keyword;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Map<String, Integer> getRelated_diseases() {
        return related_diseases;
    }

    public void setRelated_diseases(Map<String, Integer> related_diseases) {
        this.related_diseases = related_diseases;
    }

    public Map<String, Step> getSteps() {
        return steps;
    }

    public void setSteps(Map<String, Step> steps) {
        this.steps = steps;
    }

    // Map for JSON
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("bodypart", bodypart);
        result.put("keyword", keyword);
        result.put("photo", photo);
        result.put("related_diseases", related_diseases);
        result.put("steps", steps);

        return result;
    }
}
