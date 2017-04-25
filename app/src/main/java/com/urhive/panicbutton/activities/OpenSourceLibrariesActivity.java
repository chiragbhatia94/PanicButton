package com.urhive.panicbutton.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsFragment;
import com.urhive.panicbutton.R;

import mehdi.sakout.aboutpage.AboutPage;

public class OpenSourceLibrariesActivity extends AppCompatBase {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source_libraries);

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

            View aboutPage = new AboutPage(OpenSourceLibrariesActivity.this).isRTL(false)
                    .setImage(R.mipmap.ic_launcher).setDescription(getString(R.string.app_slogan)
                    ).addGroup("Ur Hive").addEmail("urhive.com@gmail.com").addWebsite
                            ("https://chiragbhatia94.github.io/PanicButton/").addPlayStore("com" +
                            ".urhive.panicbutton").addGitHub("chiragbhatia94").create();

            FrameLayout fl = (FrameLayout) findViewById(R.id.content_frame);
            fl.addView(aboutPage);
        }

    }
}
