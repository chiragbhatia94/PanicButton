package com.urhive.panicbutton.helpers;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.urhive.panicbutton.models.Emergency;
import com.urhive.panicbutton.models.Instruction;
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
    public static final String INSTRUCTION = "instruction";
    public static final String KEYWORDS = "keywords";
    public static final String STEPS = "steps";
    public static final String CONTACTS = "contacts";

    public static final String AGE_GENDER = "age_gender";
    public static final String ADDRESS = "address";
    public static final String BLOOD_GROUP = "blood_group";
    public static final String MEDICAL_NOTES = "medical_notes";
    public static final String USERS = "users";
    private static final String TAG = "DBHelper";
    private static DatabaseReference mFirebaseDatabaseReference;

    public static void updateDB() {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        // emergencydiabetes();
        bleeding();
        electrocution();
        epilepsy();
        faint();
        fracture();
        heatstroke();
        poison();
        unresponsiveAndNotBreathing();
        strainAndSprain();
        hypothermia();
        allergy();
        burn();
        amputation();

        // instructions
        cpr();

        Log.i(TAG, "updateDB: Database Updated!");
    }

    // instructions
    private static void cpr() {
        String name = "CPR";
        String photo = "cpr";

        List<Step> steps = new ArrayList<>();
        // sequence of the steps matters here
        steps.add(new Step(Step.TEXT, "Make sure the area is safe."));
        steps.add(new Step(Step.TEXT, "Turn the person on his back."));
        steps.add(new Step("cprStep3", "Verify if the victim is breathing by listening, looking " +
                "and feeling."));
        steps.add(new Step("cprStep4", "If the victim is not breathing: do a chin lift and clear " +
                "the airways if needed."));
        steps.add(new Step(Step.TEXT, "Give chest compressions"));
        // steps are not complete but you would have got the idea!

        Instruction cpr = new Instruction(name, photo, steps);

        mFirebaseDatabaseReference.child(DBHelper.INSTRUCTION).child(name).setValue(cpr.toMap());
    }

    // emergency
    private static void amputation() {
        String name = "Amputation";
        String doctor_category = "Orthopediac";
        Map<String, Boolean> bodypart = new HashMap<>();


        bodypart.put("hands", true);

        bodypart.put("leg", true);


        Map<String, Integer> keyword = new HashMap<>();
        keyword.put("abrasion", 12);
        keyword.put("cut", 12);

        String photo = "amputation";

        Map<String, Integer> related_diseases = new HashMap<>();
        related_diseases.put("amputation", 20);

        List<Step> steps = new ArrayList<>();
        steps.add(new Step("burnStep1", "Call 108."));
        steps.add(new Step("amputationStep2", "Control blood loss by applying direct pressure and" +
                " raising the injured part above the casualty's heart."));
        steps.add(new Step(Step.TEXT, "Don’t reposition the person if you suspect a head, neck, " +
                "back, or leg injury."));
        steps.add(new Step("bleedingStep4", "Apply steady, direct pressure to the wound. If " +
                "there’s an object in the wound, apply pressure around it, not directly over it."));
        steps.add(new Step(Step.TEXT, "If blood soaks through, apply another covering over the " +
                "first one. Don’t take the first one off."));
        steps.add(new Step(Step.TEXT, "Calm the person as much as possible until medical help " +
                "arrives."));
        steps.add(new Step("amputationStep7", "Wrap the severed part in kitchen film or a plastic" +
                " bag. Wrap the package in gauze or soft fabric and place it in a container full " +
                "of crushed ice."));
        steps.add(new Step(Step.TEXT, "DO NOT let the severed part touch the crushed ice when " +
                "packing it. "));


        Emergency amputation = new Emergency(name, doctor_category, bodypart, keyword, photo,
                related_diseases, steps);

        mFirebaseDatabaseReference.child(DBHelper.EMERGENCY).child(name).setValue(amputation
                .toMap());
    }

    private static void burn() {
        String name = "Burn";
        String doctor_category = "Dermatologist";
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

        String photo = "burn";

        Map<String, Integer> related_diseases = new HashMap<>();
        related_diseases.put("amputation", 20);

        List<Step> steps = new ArrayList<>();
        steps.add(new Step("burnStep1", "Call 108 if The burn is Severe."));
        steps.add(new Step("burnStep2", "Stop Burning Immediately by putting out fire or " +
                "isolating the person."));
        steps.add(new Step("burnStep3", "Help the person stop, drop, and roll to smother flames " +
                "if any."));
        steps.add(new Step(Step.TEXT, "Remove smoldering material from the person. Remove hot or " +
                "burned clothing. If clothing sticks to skin, cut or tear around it."));
        steps.add(new Step(Step.TEXT, "Take off jewelry, belts, and tight clothing. Burns can " +
                "swell quickly."));
        steps.add(new Step(Step.TEXT, "For Minor Burns, Cool burn, Protect burn and Treat pain)"));
        steps.add(new Step(Step.TEXT, "For Major Burns, Cool burn, Protect burn, Prevent Shock " +
                "and Treat pain"));
        steps.add(new Step("burnStep8", "Hold burned skin under cool (not cold) running water or " +
                "immerse in cool water until pain subsides."));
        steps.add(new Step("burnStep9", "Cover with sterile, non-adhesive bandage or clean cloth." +
                " Do not apply butter or ointments, which can cause infection."));
        steps.add(new Step(Step.TEXT, "Give over-the-counter pain reliever such as ibuprofen " +
                "(Advil, Motrin), acetaminophen (Tylenol), or naproxen (Aleve)."));
        steps.add(new Step(Step.TEXT, "Seek Medical assistance."));

        Emergency amputation = new Emergency(name, doctor_category, bodypart, keyword, photo,
                related_diseases, steps);

        mFirebaseDatabaseReference.child(DBHelper.EMERGENCY).child(name).setValue(amputation
                .toMap());
    }

    private static void allergy() {
        String name = "Allergy";
        String doctor_category = "Dermatologist";
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

        String photo = "allergy";

        Map<String, Integer> related_diseases = new HashMap<>();
        related_diseases.put("amputation", 20);

        List<Step> steps = new ArrayList<>();
        steps.add(new Step("allergyStep1", "The most serious allergic reactions can cause " +
                "anaphylaxis. This reaction occurs minutes after exposure and, if left untreated," +
                " can lead to loss unconsciousness, respiratory distress, and cardiac arrest."));
        steps.add(new Step(Step.TEXT, "Call 108 immediately."));
        steps.add(new Step("allergyStep3", "Help the person lie on their back."));
        steps.add(new Step("allergyStep4", "Loosen tight clothing and cover the person with a " +
                "blanket. Don't give the person anything to drink."));
        steps.add(new Step("allergyStep5", "Turn them on their side if they are vomiting or " +
                "bleeding."));
        steps.add(new Step("electrocutionStep3", "If there are no signs of breathing, coughing or" +
                " movement, begin CPR."));
        steps.add(new Step(Step.TEXT, "Wait till help arrives."));

        Emergency amputation = new Emergency(name, doctor_category, bodypart, keyword, photo,
                related_diseases, steps);

        mFirebaseDatabaseReference.child(DBHelper.EMERGENCY).child(name).setValue(amputation
                .toMap());
    }

    private static void hypothermia() {
        String name = "Hypothermia";
        String doctor_category = "Emergency Medicine";
        Map<String, Boolean> bodypart = new HashMap<>();

        bodypart.put("chest", true);
        bodypart.put("hands", true);
        bodypart.put("head", true);
        bodypart.put("leg", true);


        Map<String, Integer> keyword = new HashMap<>();
        keyword.put("abrasion", 12);
        keyword.put("cut", 12);

        String photo = "hypothermia";

        Map<String, Integer> related_diseases = new HashMap<>();
        related_diseases.put("amputation", 20);

        List<Step> steps = new ArrayList<>();
        steps.add(new Step(Step.TEXT, "Call 108."));
        steps.add(new Step(Step.TEXT, "Get the person indoors and Insulate the individual from " +
                "the cold ground."));
        steps.add(new Step(Step.TEXT, "Remove wet clothing and Replace wet things with warm, dry " +
                "coats or blankets."));
        steps.add(new Step("hypothermiaStep4", "Warm the person's trunk first, not hands and feet" +
                ". Warming extremities first can cause shock."));
        steps.add(new Step("hypothermiaStep5", "Do not immerse the person in warm water. Rapid " +
                "warming can cause heart arrhythmia."));
        steps.add(new Step("hypothermiaStep6", "If using hot water bottles or chemical hot packs," +
                " wrap them in cloth; don't apply them directly to the skin."));
        steps.add(new Step("hypothermiaStep7", "Give the person a warm drink, if conscious. Avoid" +
                " caffeine alcohol or cigarettes."));
        steps.add(new Step("hypothermiaStep8", "Alcohol hinders the rewarming process, and " +
                "tobacco products interfere with circulation that is needed for rewarming."));
        steps.add(new Step("hypothermiaStep9", "Begin CPR, if Necessary, While Warming Person"));
        steps.add(new Step("hypothermiaStep10", "Once the body temperature begins to rise, keep " +
                "the person dry and wrapped in a warm blanket. Wrap the person's head and neck, " +
                "as well."));
        steps.add(new Step(Step.TEXT, "Seek Medical assistance"));

        Emergency amputation = new Emergency(name, doctor_category, bodypart, keyword, photo,
                related_diseases, steps);

        mFirebaseDatabaseReference.child(DBHelper.EMERGENCY).child(name).setValue(amputation
                .toMap());
    }

    private static void strainAndSprain() {
        String name = "Strain And Sprain";
        String doctor_category = "Orthopediac";
        Map<String, Boolean> bodypart = new HashMap<>();
        bodypart.put("hands", true);
        bodypart.put("head", true);
        bodypart.put("leg", true);
        bodypart.put("neck", true);
        bodypart.put("abdomen", true);

        Map<String, Integer> keyword = new HashMap<>();
        keyword.put("abrasion", 12);
        keyword.put("cut", 12);

        String photo = "strainAndSprain";

        Map<String, Integer> related_diseases = new HashMap<>();
        related_diseases.put("amputation", 20);

        List<Step> steps = new ArrayList<>();
        steps.add(new Step(Step.TEXT, "Get the person to rest."));
        steps.add(new Step(Step.TEXT, "Use PRICE (Protect, Rest, Ice, Compress, Elevate) " +
                "Technique."));
        steps.add(new Step("strainAndSprainStep3", "Protect by applying an elastic bandage, " +
                "sling, or splint."));
        steps.add(new Step(Step.TEXT, "Rest the muscle for a minimum of a single day."));
        steps.add(new Step("strainAndSprainStep4", "Wrap an icepack or cold compress in a towel " +
                "and place over the injured part immediately. Continue for no more than 20 " +
                "minutes at a time, ana apply four to eight times a day."));
        steps.add(new Step("strainAndSprainStep5", "Compress by gently wrapping with elastic " +
                "Bandage (Don’t wrap tightly)."));
        steps.add(new Step("strainAndSprainStep6", "Elevate injured area above the person's heart" +
                " level, if possible."));
        steps.add(new Step(Step.TEXT, "Manage Pain and Inflammation after consulting a doctor."));

        Emergency amputation = new Emergency(name, doctor_category, bodypart, keyword, photo,
                related_diseases, steps);

        mFirebaseDatabaseReference.child(DBHelper.EMERGENCY).child(name).setValue(amputation.toMap());
    }

    private static void unresponsiveAndNotBreathing() {
        String name = "Unresponsive And Not Breathing";
        String doctor_category = "Emergency Medicine";
        Map<String, Boolean> bodypart = new HashMap<>();
        bodypart.put("hands", true);

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
        steps.add(new Step("unresponsiveAndNotBreathingStep1", "Check breathing by tilting their " + "" + "" + "" + "head backwards and looking and feeling for breaths."));
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
        String doctor_category = "Emergency Medicine";
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
        steps.add(new Step(Step.TEXT, "For Swallowed poison. Remove anything remaining in the " +
                "person's mouth. If the suspected poison is a household cleaner or other " +
                "chemical, read the container's label and follow instructions for accidental " +
                "poisoning."));
        steps.add(new Step("poisonStep3", "For Poison on the skin. Remove any contaminated " +
                "clothing using gloves. Rinse the skin for 15 to 20 minutes in a shower or with a" +
                " hose."));
        steps.add(new Step("poisonStep4", "For Poison in the eye. Gently flush the eye with cool or lukewarm water for 20 minutes or until help arrives."));
        steps.add(new Step("poisonStep5", "For Inhaled poison. Get the person into fresh air as " +
                "soon as possible."));
        steps.add(new Step("poisonStep6", "If the person vomits, turn his or her head to the side to prevent choking."));
        steps.add(new Step("poisonStep7", "Begin CPR if the person shows no signs of life, such as moving, breathing or coughing."));
        steps.add(new Step(Step.TEXT, "DO NOT Give an unconscious person anything by mouth."));
        steps.add(new Step(Step.TEXT, "DO NOT Induce vomiting unless you are told to do so by the Doctor."));

        Emergency amputation = new Emergency(name, doctor_category, bodypart, keyword, photo,
                related_diseases, steps);

        mFirebaseDatabaseReference.child(DBHelper.EMERGENCY).child(name).setValue(amputation
                .toMap());
    }

    private static void heatstroke() {
        String name = "Heat Stroke";
        String doctor_category = "General Medicine";
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
        String name = "Fracture";
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
        steps.add(new Step("fractureStep1", "Stop any bleeding. Apply pressure to the wound with " + "" + "" + "" + "a sterile bandage or a clean cloth."));
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
        String name = "Faint";
        String doctor_category = "Emergency Medicine";
        Map<String, Boolean> bodypart = new HashMap<>();
        bodypart.put("abdomen", true);
        bodypart.put("chest", true);
        bodypart.put("hands", true);
        bodypart.put("head", true);
        bodypart.put("leg", true);


        Map<String, Integer> keyword = new HashMap<>();
        keyword.put("abrasion", 12);
        keyword.put("cut", 12);

        String photo = "faint2";

        Map<String, Integer> related_diseases = new HashMap<>();
        related_diseases.put("amputation", 20);

        List<Step> steps = new ArrayList<>();
        steps.add(new Step("faintStep1", "If you feel faint, Lie down or sit down. To reduce the " + "" + "" + "" + "chance of fainting again, don't get up too quickly."));
        steps.add(new Step("faintStep2", "Place your head between your knees if you sit down."));
        steps.add(new Step(Step.TEXT, "if someone else faints, Lay the person flat on his or her " + "" + "" + "" + "back."));
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
        String name = "Epilepsy";
        String doctor_category = "Neurologist";
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
        steps.add(new Step("epilepsyStep6", "After the seizure, help the person to rest on their " + "" + "" + "" + "side with their head tilted back."));
        steps.add(new Step(Step.TEXT, "If a convulsive (shaking) seizure doesn't stop after 5 " +
                "minutes, call for an ambulance."));
        Emergency amputation = new Emergency(name, doctor_category, bodypart, keyword, photo,
                related_diseases, steps);

        mFirebaseDatabaseReference.child(DBHelper.EMERGENCY).child(name).setValue(amputation
                .toMap());
    }

    private static void electrocution() {
        String name = "Electrocution";
        String doctor_category = "Emergency Medicine";
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
        String name = "Bleeding";
        String doctor_category = "Orthopaedic";
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
        steps.add(new Step("bleedingStep1", "Apply direct pressure to the bleeding wound to stop " + "" + "" + "" + "bleeding."));
        steps.add(new Step(Step.TEXT, "If the cut/wound is minor Gently clean with soap and warm " + "" + "" + "" + "water, Don’t use hydrogen peroxide or iodine."));
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
        String name = "Diabetes";
        String doctor_category = "General Medicine";
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
