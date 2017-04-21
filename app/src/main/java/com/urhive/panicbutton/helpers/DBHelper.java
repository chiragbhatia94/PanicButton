package com.urhive.panicbutton.helpers;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.urhive.panicbutton.models.Emergency;
import com.urhive.panicbutton.models.Step;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chirag Bhatia on 17-04-2017.
 */

public class DBHelper {

    public static final String EMERGENCY = "emergency";
    public static final String KEYWORDS = "keywords";
    public static final String STEPS = "steps";

    private static final String TAG = "DBHelper";
    private static DatabaseReference mFirebaseDatabaseReference;

    public static void updateDB() {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        diabetes();
        bleeding();
        electrocution();
        epilepsy();
        faint();
        fracture();
        heatstroke();
        poison();
        unresponsiveAndNotBreathing();

        Log.i(TAG, "updateDB: Database Updated!");
    }

    private static void unresponsiveAndNotBreathing() {
        String name = "unresponsiveAndNotBreathing";
        String doctor_category = "Orthopediac";
        Map<String, Boolean> bodypart = new HashMap<>();

        bodypart.put("chest", true);

        bodypart.put("head", true);

        bodypart.put("neck", true);

        Map<String, Integer> keyword = new HashMap<>();
        keyword.put("abrasion", 12);
        keyword.put("cut", 12);

        String photo = "unresponsiveAndNotBreathing";

        Map<String, Integer> related_diseases = new HashMap<>();
        related_diseases.put("amputation", 20);

        List<Step> steps = new ArrayList<>();
        steps.add(new Step("unresponsiveAndNotBreathingStep1", "Check breathing by tilting their " +
                "" + "" + "" + "" + "head backwards and looking and feeling for breaths."));
        steps.add(new Step(Step.TEXT, "Call 108 as soon as possible, or get someone else to do "
                + "it" + "."));
        steps.add(new Step("unresponsiveAndNotBreathingStep3", "Push firmly downwards in the " +
                "middle of the chest and then release."));
        steps.add(new Step(Step.TEXT, "Push at a regular rate until help arrives."));

        Emergency amputation = new Emergency(name, doctor_category, bodypart, keyword, photo,
                related_diseases, steps);

        mFirebaseDatabaseReference.child(DBHelper.EMERGENCY).child(name).setValue(amputation
                .toMap());
    }

    private static void poison() {
        String name = "poison";
        String doctor_category = "Orthopediac";
        Map<String, Boolean> bodypart = new HashMap<>();
        bodypart.put("abdomen", true);
        bodypart.put("chest", true);
        bodypart.put("hands", true);
        bodypart.put("head", true);
        bodypart.put("leg", true);
        bodypart.put("neck", true);

        Map<String, Integer> keyword = new HashMap<>();
        keyword.put("abrasion", 12);
        keyword.put("cut", 12);

        String photo = "poison";

        Map<String, Integer> related_diseases = new HashMap<>();
        related_diseases.put("amputation", 20);

        List<Step> steps = new ArrayList<>();
        steps.add(new Step("poisonStep1", "Call 108 immediately"));
        steps.add(new Step("", "For Swallowed poison. Remove anything remaining in the person's "
                + "mouth. If the suspected poison is a household cleaner or other chemical, read " +
                "" + "" + "" + "" + "the container's label and follow instructions for accidental" +
                " " + "poisoning" + "."));
        steps.add(new Step("", "For Poison on the skin. Remove any contaminated clothing using "
                + "gloves. Rinse the skin for 15 to 20 minutes in a shower or with a hose."));
        steps.add(new Step("", "For Poison in the eye. Gently flush the eye with cool or " +
                "lukewarm" + " water for 20 minutes or until help arrives."));
        steps.add(new Step("", "For Inhaled poison. Get the person into fresh air as soon as " +
                "possible."));
        steps.add(new Step("", "If the person vomits, turn his or her head to the side to " +
                "prevent" + " choking."));
        steps.add(new Step("", "Begin CPR if the person shows no signs of life, such as moving, "
                + "breathing or coughing."));
        steps.add(new Step("", "DO NOT Give an unconscious person anything by mouth."));
        steps.add(new Step("", "DO NOT Induce vomiting unless you are told to do so by the " +
                "Doctor" + "."));

        Emergency amputation = new Emergency(name, doctor_category, bodypart, keyword, photo,
                related_diseases, steps);

        mFirebaseDatabaseReference.child(DBHelper.EMERGENCY).child(name).setValue(amputation
                .toMap());
    }

    private static void heatstroke() {
        String name = "heatstroke";
        String doctor_category = "Orthopediac";
        Map<String, Boolean> bodypart = new HashMap<>();
        bodypart.put("abdomen", true);
        bodypart.put("chest", true);
        bodypart.put("hands", true);
        bodypart.put("head", true);
        bodypart.put("leg", true);
        bodypart.put("neck", true);

        Map<String, Integer> keyword = new HashMap<>();
        keyword.put("abrasion", 12);
        keyword.put("cut", 12);

        String photo = "heatstroke";

        Map<String, Integer> related_diseases = new HashMap<>();
        related_diseases.put("amputation", 20);

        List<Step> steps = new ArrayList<>();

        steps.add(new Step(Step.TEXT, "Call Emergency."));
        steps.add(new Step("heatstrokeStep1", "Move the person into a cool place, out of direct "
                + "sunlight."));
        steps.add(new Step(Step.TEXT, "Remove the person's unnecessary clothing, and place the "
                + "person on his or her side to expose as much skin surface to the air as " +
                "possible" + "."));
        steps.add(new Step("heatstrokeStep3", "Cool the person's entire body by sponging or " +
                "spraying cold water."));
        steps.add(new Step(Step.TEXT, "Place ice packs or cool wet towels on the neck, armpits "
                + "and groin."));
        steps.add(new Step("heatstrokeStep5", "Do not give aspirin or acetaminophen to reduce a "
                + "high body temperature."));
        steps.add(new Step(Step.TEXT, "If the person is awake and alert enough to swallow, give "
                + "the person fluids for Hydration. Do not give any hot drinks or stimulants."));


        Emergency amputation = new Emergency(name, doctor_category, bodypart, keyword, photo,
                related_diseases, steps);

        mFirebaseDatabaseReference.child(DBHelper.EMERGENCY).child(name).setValue(amputation
                .toMap());
    }

    private static void fracture() {
        String name = "fracture";
        String doctor_category = "Orthopediac";
        Map<String, Boolean> bodypart = new HashMap<>();
        bodypart.put("abdomen", true);
        bodypart.put("chest", true);
        bodypart.put("hands", true);
        bodypart.put("head", true);
        bodypart.put("leg", true);
        bodypart.put("neck", true);

        Map<String, Integer> keyword = new HashMap<>();
        keyword.put("abrasion", 12);
        keyword.put("cut", 12);

        String photo = "fracture";

        Map<String, Integer> related_diseases = new HashMap<>();
        related_diseases.put("amputation", 20);

        List<Step> steps = new ArrayList<>();
        steps.add(new Step("fractureStep1", "Stop any bleeding. Apply pressure to the wound with " +
                "" + "" + "" + "" + "a sterile bandage or a clean cloth."));
        steps.add(new Step("fractureStep2", "Immobilize the injured area."));
        steps.add(new Step("fractureStep3", "Apply ice packs to limit swelling and help relieve "
                + "pain."));
        steps.add(new Step("fractureStep4", " Help them get into a comfortable position, " +
                "encourage them to rest, and reassure them. Cover them with a blanket or " +
                "clothing" + " to keep them warm."));
        steps.add(new Step(Step.TEXT, "Get professional help:"));


        Emergency amputation = new Emergency(name, doctor_category, bodypart, keyword, photo,
                related_diseases, steps);

        mFirebaseDatabaseReference.child(DBHelper.EMERGENCY).child(name).setValue(amputation
                .toMap());
    }

    private static void faint() {
        String name = "faint";
        String doctor_category = "Orthopediac";
        Map<String, Boolean> bodypart = new HashMap<>();
        bodypart.put("abdomen", true);
        bodypart.put("chest", true);
        bodypart.put("hands", true);
        bodypart.put("head", true);
        bodypart.put("leg", true);
        bodypart.put("neck", true);

        Map<String, Integer> keyword = new HashMap<>();
        keyword.put("abrasion", 12);
        keyword.put("cut", 12);

        String photo = "faint2";

        Map<String, Integer> related_diseases = new HashMap<>();
        related_diseases.put("amputation", 20);

        List<Step> steps = new ArrayList<>();
        steps.add(new Step("faintStep1", "If you feel faint, Lie down or sit down. To reduce the " +
                "" + "" + "" + "" + "chance of fainting again, don't get up too quickly."));
        steps.add(new Step("faintStep2", "Place your head between your knees if you sit down."));
        steps.add(new Step(Step.TEXT, "if someone else faints, Lay the person flat on his or her " +
                "" + "" + "" + "" + "back."));
        steps.add(new Step("faintStep4", "Elevate the person's legs to restore blood flow to the " +
                "" + "" + "" + "" + "brain."));
        steps.add(new Step(Step.TEXT, "Loosen tight clothing."));
        steps.add(new Step(Step.TEXT, "Try to Revive the Person"));
        steps.add(new Step(Step.TEXT, "Shake the person vigorously, tap briskly, or yell."));
        steps.add(new Step("faintStep8", "If the person doesn't respond, call 108 immediately " +
                "and" + " start CPR if necessary."));

        Emergency amputation = new Emergency(name, doctor_category, bodypart, keyword, photo,
                related_diseases, steps);

        mFirebaseDatabaseReference.child(DBHelper.EMERGENCY).child(name).setValue(amputation
                .toMap());
    }

    private static void epilepsy() {
        String name = "epilepsy";
        String doctor_category = "Orthopediac";
        Map<String, Boolean> bodypart = new HashMap<>();
        bodypart.put("abdomen", true);
        bodypart.put("chest", true);
        bodypart.put("hands", true);
        bodypart.put("head", true);
        bodypart.put("leg", true);
        bodypart.put("neck", true);

        Map<String, Integer> keyword = new HashMap<>();
        keyword.put("abrasion", 12);
        keyword.put("cut", 12);

        String photo = "epilepsy";

        Map<String, Integer> related_diseases = new HashMap<>();
        related_diseases.put("amputation", 20);

        List<Step> steps = new ArrayList<>();
        steps.add(new Step(Step.TEXT, "Stay calm."));
        steps.add(new Step(Step.TEXT, "Move objects like furniture away from them."));
        steps.add(new Step(Step.TEXT, "Note the time the seizure starts."));
        steps.add(new Step("epilepsyStep4", "Do not restrain them but use a blanket or clothing "
                + "to protect their head from injury."));
        steps.add(new Step("epilepsyStep5", "Don't put anything in their mouth."));
        steps.add(new Step("epilepsyStep6", "After the seizure, help the person to rest on their " +
                "" + "" + "" + "" + "side with their head tilted back."));
        steps.add(new Step(Step.TEXT, "If a convulsive (shaking) seizure doesn't stop after 5 " +
                "minutes, call for an ambulance."));
        Emergency amputation = new Emergency(name, doctor_category, bodypart, keyword, photo,
                related_diseases, steps);

        mFirebaseDatabaseReference.child(DBHelper.EMERGENCY).child(name).setValue(amputation
                .toMap());
    }

    private static void electrocution() {
        String name = "electrocution";
        String doctor_category = "Orthopediac";
        Map<String, Boolean> bodypart = new HashMap<>();
        bodypart.put("abdomen", true);
        bodypart.put("chest", true);
        bodypart.put("hands", true);
        bodypart.put("head", true);
        bodypart.put("leg", true);
        bodypart.put("neck", true);

        Map<String, Integer> keyword = new HashMap<>();
        keyword.put("abrasion", 12);
        keyword.put("cut", 12);

        String photo = "electrocution";

        Map<String, Integer> related_diseases = new HashMap<>();
        related_diseases.put("amputation", 20);

        List<Step> steps = new ArrayList<>();
        steps.add(new Step(Step.TEXT, "Turn off the source of electricity, if possible. If not, "
                + "move the source away from you and the person, using a dry, nonconducting " +
                "object " + "made of cardboard, plastic or wood."));
        steps.add(new Step(Step.TEXT, "Call an ambulance or 108 immediately."));
        steps.add(new Step("electrocutionStep3", "When you can safely touch the person, DO CPR "
                + "if" + " the person is not breathing."));
        steps.add(new Step(Step.TEXT, "Keep the victim Warm."));
        steps.add(new Step(Step.TEXT, "Cover any burned areas with a clean cloth. Don't use a " +
                "blanket or towel, because loose fibers can stick to the burns."));
        steps.add(new Step(Step.TEXT, "Clear Breathing passage and wait till help arrives."));


        Emergency amputation = new Emergency(name, doctor_category, bodypart, keyword, photo,
                related_diseases, steps);

        mFirebaseDatabaseReference.child(DBHelper.EMERGENCY).child(name).setValue(amputation
                .toMap());
    }

    private static void bleeding() {
        String name = "bleeding";
        String doctor_category = "orthopaedic";
        Map<String, Boolean> bodypart = new HashMap<>();
        bodypart.put("abdomen", true);
        bodypart.put("chest", true);
        bodypart.put("hands", true);
        bodypart.put("head", true);
        bodypart.put("leg", true);
        bodypart.put("neck", true);

        Map<String, Integer> keyword = new HashMap<>();
        keyword.put("abrasion", 12);
        keyword.put("cut", 12);

        String photo = "bleeding";

        Map<String, Integer> related_diseases = new HashMap<>();
        related_diseases.put("amputation", 20);

        List<Step> steps = new ArrayList<>();
        steps.add(new Step("bleedingStep1", "Apply direct pressure to the bleeding wound to stop " +
                "" + "" + "" + "" + "bleeding."));
        steps.add(new Step(Step.TEXT, "If the cut/wound is minor Gently clean with soap and warm " +
                "" + "" + "" + "" + "water, Don’t use hydrogen peroxide or iodine."));
        steps.add(new Step("bleedingStep3", "If the cut/wound is major Try to Raise the injured "
                + "area above Heart Level."));
        steps.add(new Step("bleedingStep4", "If a foreign body is embedded in the wound, DO NOT "
                + "remove it but apply padding on either side of the object and build it up to "
                + "avoid pressure on the foreign body."));
        steps.add(new Step(Step.TEXT, "Keep the patient at total rest."));
        steps.add(new Step(Step.TEXT, "Seek medical assistance"));
        Emergency amputation = new Emergency(name, doctor_category, bodypart, keyword, photo,
                related_diseases, steps);

        mFirebaseDatabaseReference.child(DBHelper.EMERGENCY).child(name).setValue(amputation
                .toMap());
    }

    private static void diabetes() {
        String name = "diabetes";
        String doctor_category = "Orthopediac";
        Map<String, Boolean> bodypart = new HashMap<>();
        bodypart.put("abdomen", true);

        bodypart.put("leg", true);

        Map<String, Integer> keyword = new HashMap<>();
        keyword.put("abrasion", 12);
        keyword.put("cut", 12);

        String photo = "diabetes";

        Map<String, Integer> related_diseases = new HashMap<>();
        related_diseases.put("amputation", 20);

        List<Step> steps = new ArrayList<>();
        steps.add(new Step("diabetesStep1", "Give sugar / glucose in LIQUID FORM, for example " +
                "honey, glucose syrup or soft drink (non-diabetic) – not a diet or low calorie "
                + "drink."));
        steps.add(new Step(Step.TEXT, "repeat if necessary."));
        steps.add(new Step(Step.TEXT, "closely monitor casualty."));
        steps.add(new Step(Step.TEXT, "Reassure the person. Most people will gradually improve, "
                + "but if in doubt, call 108."));
        steps.add(new Step("diabetesStep5", "no food if unconscious or semi-conscious."));
        steps.add(new Step("diabetesStep6", "If the patient is unconscious ,Support the patient "
                + "on their side into Recovery Position and call 108."));


        Emergency amputation = new Emergency(name, doctor_category, bodypart, keyword, photo,
                related_diseases, steps);

        mFirebaseDatabaseReference.child(DBHelper.EMERGENCY).child(name).setValue(amputation
                .toMap());
    }
}
