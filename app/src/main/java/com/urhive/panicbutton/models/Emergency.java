package com.urhive.panicbutton.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chirag Bhatia on 17-04-2017.
 */

public class Emergency {
    String name;
    String doctor_category;
    Map<String, Boolean> bodypart;
    Map<String, Integer> keyword;
    String photo;
    Map<String, Integer> related_diseases;
    List<Step> steps;

    // Constructors
    public Emergency() {

    }

    // copy constructor
    public Emergency(Emergency emergency) {
        this.name = emergency.getName();
        this.doctor_category = emergency.getDoctor_category();
        this.bodypart = emergency.getBodypart();
        this.keyword = emergency.getKeyword();
        this.photo = emergency.getPhoto();
        this.related_diseases = emergency.getRelated_diseases();
        this.steps = emergency.getSteps();
    }

    public Emergency(String name, String doctor_category, Map<String, Boolean> bodypart,
                     Map<String, Integer> keyword, String photo, Map<String, Integer>
                             related_diseases, List<Step> steps) {
        this.name = name;
        this.doctor_category = doctor_category;
        this.bodypart = bodypart;
        this.keyword = keyword;
        this.photo = photo;
        this.related_diseases = related_diseases;
        this.steps = steps;
    }

    // Getter & Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoctor_category() {
        return doctor_category;
    }

    public void setDoctor_category(String doctor_category) {
        this.doctor_category = doctor_category;
    }

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

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    // Map for JSON
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("doctor_category", doctor_category);
        result.put("bodypart", bodypart);
        result.put("keyword", keyword);
        result.put("photo", photo);
        result.put("related_diseases", related_diseases);
        result.put("steps", steps);

        return result;
    }
}
