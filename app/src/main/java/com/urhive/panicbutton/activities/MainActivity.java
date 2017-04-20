package com.urhive.panicbutton.activities;

import android.content.ContentUris;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.urhive.panicbutton.R;
import com.urhive.panicbutton.helpers.DBHelper;
import com.urhive.panicbutton.models.Emergency;
import com.urhive.panicbutton.models.Step;
import com.urhive.panicbutton.services.EmergencyButtonService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatBase {

    private static final String TAG = "MainActivity";

    private static final int RESULT_PICK_CONTACT = 850;
    private TextView signinStatusTV;
    private ImageView profilePictureIV;
    private TextView emailTV, displayNameTV, enabledProvidersTV;

    private void initViews() {
        signinStatusTV = (TextView) findViewById(R.id.signinStatusTV);
        profilePictureIV = (ImageView) findViewById(R.id.user_profile_picture);
        emailTV = (TextView) findViewById(R.id.user_email);
        displayNameTV = (TextView) findViewById(R.id.user_display_name);
        enabledProvidersTV = (TextView) findViewById(R.id.user_enabled_providers);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setProfile(mFirebaseUser);
    }

    private void setProfile(FirebaseUser user) {
        if (user.getPhotoUrl() != null) {
            Glide.with(this).load(user.getPhotoUrl()).fitCenter().into(profilePictureIV);
        }

        emailTV.setText(TextUtils.isEmpty(user.getEmail()) ? getString(R.string.no_email) : user
                .getEmail());
        displayNameTV.setText(TextUtils.isEmpty(user.getDisplayName()) ? getString(R.string
                .no_display_name) : user.getDisplayName());

        StringBuilder providerList = new StringBuilder(100);

        providerList.append(getString(R.string.providers_used) + " ");

        if (user.getProviders() == null || user.getProviders().isEmpty()) {
            providerList.append(getString(R.string.none));
        } else {
            Iterator<String> providerIter = user.getProviders().iterator();
            while (providerIter.hasNext()) {
                String provider = providerIter.next();
                if (GoogleAuthProvider.PROVIDER_ID.equals(provider)) {
                    providerList.append(getString(R.string.google));
                } else if (FacebookAuthProvider.PROVIDER_ID.equals(provider)) {
                    providerList.append(getString(R.string.facebook));
                } else if (EmailAuthProvider.PROVIDER_ID.equals(provider)) {
                    providerList.append(getString(R.string.password));
                } else {
                    providerList.append(provider);
                }

                if (providerIter.hasNext()) {
                    providerList.append(", ");
                }
            }
        }

        enabledProvidersTV.setText(providerList);

        signinStatusTV.setText(getString(R.string.welcome) + ", " + mFirebaseUser.getDisplayName
                () + "!");
    }

    public void startService(View view) {
        Log.i(TAG, "startService: " + "clicked on startService button");

        Intent intent = new Intent(MainActivity.this, EmergencyButtonService.class);
        startService(intent);
    }

    public void stopService(View view) {
        Log.i(TAG, "stopService: " + "clicked on stop service button");

        Intent intent = new Intent(MainActivity.this, EmergencyButtonService.class);
        stopService(intent);

        Toast.makeText(this, R.string.panic_button_stopped, Toast.LENGTH_SHORT).show();
    }

    public void contactPicker(View view) {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract
                .CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     *
     * @param data
     */
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone
                    .DISPLAY_NAME);

            //int photoIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.)
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);

            TextView textView1 = (TextView) findViewById(R.id.contactName);
            TextView textView2 = (TextView) findViewById(R.id.contactNumberTV);

            // for contact DP
            // not working
            int idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
            /*Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                    idIndex);
            Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo
                    .CONTENT_DIRECTORY);
            InputStream is = ContactsContract.Contacts.openContactPhotoInputStream
            (getContentResolver(), contactUri, false);
            Bitmap bmp = BitmapFactory.decodeStream(is);*/
            Bitmap bmp = BitmapFactory.decodeStream(openPhoto(idIndex));
            CircleImageView circleImageView = (CircleImageView) findViewById(R.id.contactDPIV);
            circleImageView.setImageBitmap(bmp);

            // Set the value to the textviews
            textView1.setText(name);
            textView2.setText(phoneNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // for photo thumbnail
    public InputStream openPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo
                .CONTENT_DIRECTORY);
        Cursor cursor = getContentResolver().query(photoUri, new String[]{ContactsContract
                .Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return new ByteArrayInputStream(data);
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    // for larger photo
    public InputStream openDisplayPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                contactId);
        Uri displayPhotoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo
                .DISPLAY_PHOTO);
        try {
            AssetFileDescriptor fd = getContentResolver().openAssetFileDescriptor
                    (displayPhotoUri, "r");
            return fd.createInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    public void tempButton(View view) {
        diabetes();
        bleeding();
        electrocution();
        epilepsy();
        faint();
        fracture();
        heatstroke();
        poison();
        unresponsiveAndNotBreathing();
    }

    public void tempButton2(View view) {
        /*mFirebaseDatabaseReference.child(DBHelper.EMERGENCY);*/

        StorageReference mRef = FirebaseStorage.getInstance().getReference().child("bleeding");
        Glide.with(MainActivity.this).using(new FirebaseImageLoader()).load(mRef).into
                (profilePictureIV);
    }

    public void unresponsiveAndNotBreathing() {
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
                "" + "head backwards and looking and feeling for breaths."));
        steps.add(new Step(Step.TEXT, "Call 108 as soon as possible, or get someone else to do " +
                "it" + "."));
        steps.add(new Step("unresponsiveAndNotBreathingStep3", "Push firmly downwards in the " +
                "middle of the chest and then release."));
        steps.add(new Step(Step.TEXT, "Push at a regular rate until help arrives."));

        Emergency amputation = new Emergency(name, doctor_category, bodypart, keyword, photo,
                related_diseases, steps);

        mFirebaseDatabaseReference.child(DBHelper.EMERGENCY).child(name).setValue(amputation
                .toMap());
    }

    public void poison() {
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
                "" + "the container's label and follow instructions for accidental poisoning."));
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
        steps.add(new Step("", "DO NOT Induce vomiting unless you are told to do so by the Doctor" +
                "."));

        Emergency amputation = new Emergency(name, doctor_category, bodypart, keyword, photo,
                related_diseases, steps);

        mFirebaseDatabaseReference.child(DBHelper.EMERGENCY).child(name).setValue(amputation
                .toMap());
    }

    public void heatstroke() {
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

    public void fracture() {
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
                "" + "a sterile bandage or a clean cloth."));
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

    public void faint() {
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
                "" + "chance of fainting again, don't get up too quickly."));
        steps.add(new Step("faintStep2", "Place your head between your knees if you sit down."));
        steps.add(new Step(Step.TEXT, "if someone else faints, Lay the person flat on his or her " +
                "" + "back."));
        steps.add(new Step("faintStep4", "Elevate the person's legs to restore blood flow to the " +
                "" + "brain."));
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

    public void epilepsy() {
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
                "" + "side with their head tilted back."));
        steps.add(new Step(Step.TEXT, "If a convulsive (shaking) seizure doesn't stop after 5 " +
                "minutes, call for an ambulance."));
        Emergency amputation = new Emergency(name, doctor_category, bodypart, keyword, photo,
                related_diseases, steps);

        mFirebaseDatabaseReference.child(DBHelper.EMERGENCY).child(name).setValue(amputation
                .toMap());
    }

    public void electrocution() {
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
        steps.add(new Step("electrocutionStep3", "When you can safely touch the person, DO CPR " +
                "if" + " the person is not breathing."));
        steps.add(new Step(Step.TEXT, "Keep the victim Warm."));
        steps.add(new Step(Step.TEXT, "Cover any burned areas with a clean cloth. Don't use a " +
                "blanket or towel, because loose fibers can stick to the burns."));
        steps.add(new Step(Step.TEXT, "Clear Breathing passage and wait till help arrives."));


        Emergency amputation = new Emergency(name, doctor_category, bodypart, keyword, photo,
                related_diseases, steps);

        mFirebaseDatabaseReference.child(DBHelper.EMERGENCY).child(name).setValue(amputation
                .toMap());
    }

    public void bleeding() {
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
                "" + "bleeding."));
        steps.add(new Step(Step.TEXT, "If the cut/wound is minor Gently clean with soap and warm " +
                "" + "water, Don’t use hydrogen peroxide or iodine."));
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

    void diabetes() {
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
