package com.urhive.panicbutton.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsFragment;
import com.urhive.panicbutton.R;

import mehdi.sakout.aboutpage.AboutPage;

public class ReplacementActivity extends AppCompatBase {
    private static final String TAG = ReplacementActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replacement);

        Bundle bundle = getIntent().getExtras();
        String type = bundle.getString("type");

        if (type.equals("openSourceLibrary")) {
            setToolbar(getString(R.string.openSourceLibraries));

            LibsFragment fragment = new LibsBuilder()
                    //Pass the fields of your application to the lib so it can find all
                    // external lib information
                    .withFields(R.string.class.getFields()).withAboutAppName(getString(R.string
                            .app_name)).withAboutIconShown(true).withAboutVersionShown(true)
                    .withAboutDescription(getString(R.string.app_slogan))
                    //get the fragment
                    .fragment();

            final android.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        } else if (type.equals("about")) {
            setToolbar(getString(R.string.about));

            View aboutPage = new AboutPage(ReplacementActivity.this).isRTL(false).setImage(R
                    .mipmap.ic_launcher).setDescription(getString(R.string.app_slogan)).addGroup
                    ("Ur Hive").addEmail("urhive.com@gmail.com").addWebsite
                    ("https://chiragbhatia94.github.io/PanicButton/").addPlayStore("com" + "" +
                    "" + ".urhive.panicbutton").addGitHub("chiragbhatia94").create();

            FrameLayout fl = (FrameLayout) findViewById(R.id.content_frame);
            fl.addView(aboutPage);
        } else if (type.equals("developers")) {
            setToolbar(getString(R.string.developers));
            FrameLayout fl = (FrameLayout) findViewById(R.id.content_frame);
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout
                    .developer_profile_layout, null, false);
            TextView emailAyushi = (TextView) view.findViewById(R.id.emailAyushi);
            TextView emailArshita = (TextView) view.findViewById(R.id.emailArshita);
            TextView emailChirag = (TextView) view.findViewById(R.id.emailChirag);
            TextView emailHardik = (TextView) view.findViewById(R.id.emailHardik);

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String email = ((TextView) v).getText().toString();
                    Log.i(TAG, "onClick: ");
                    intent.putExtra(Intent.EXTRA_EMAIL, email);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                    intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

                    startActivity(Intent.createChooser(intent, "Send Email"));
                }
            };

            emailAyushi.setOnClickListener(onClickListener);
            emailArshita.setOnClickListener(onClickListener);
            emailChirag.setOnClickListener(onClickListener);
            emailHardik.setOnClickListener(onClickListener);
            fl.addView(view);
        }
    }
}
