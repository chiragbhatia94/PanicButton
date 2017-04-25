package com.urhive.panicbutton.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chirag Bhatia on 17-04-2017.
 */

public class Instruction {
    String name;
    String photo;
    List<Step> steps;

    // Constructors
    public Instruction() {

    }

    // copy constructor
    public Instruction(Instruction instruction) {
        this.name = instruction.getName();
        this.photo = instruction.getPhoto();
        this.steps = instruction.getSteps();
    }

    public Instruction(String name, String photo, List<Step> steps) {
        this.name = name;
        this.photo = photo;
        this.steps = steps;
    }

    // Getter & Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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
        result.put("photo", photo);
        result.put("steps", steps);

        return result;
    }
}
