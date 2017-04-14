package com.urhive.panicbutton.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.urhive.panicbutton.R;

/**
 * Created by Chirag Bhatia on 14-04-2017.
 */

public class AppCompatBase extends AppCompatActivity {

    protected static FirebaseAuth mFirebaseAuth;
    protected static FirebaseAuth.AuthStateListener mAuthListener;
    protected static FirebaseUser mFirebaseUser;
    protected static FirebaseDatabase mFirebaseDatabase;
    protected static DatabaseReference mFirebaseDatabaseReference;

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle
            persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    /*
    * Your toolbar should always have an id of toolbar
    * */
    protected void setToolbar() {
        // for setting toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // supportactionbar home icon is grey in color while adding toolbarnavigationicon is of
        // our defined color
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        // toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
    }

    /*
    * Your toolbar should always have an id of toolbar
    * */
    protected void setToolbar(String title) {
        // for setting toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // supportactionbar home icon is grey in color while adding toolbarnavigationicon is of
        // our defined color
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(title);
        }
        // toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
    }

    protected void setToolbarWithoutHomeAsUp() {
        // for setting toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // supportactionbar home icon is grey in color while adding toolbarnavigationicon is of
        // our defined color
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
    }

    protected void setToolbarWithoutHomeAsUp(String title) {
        // for setting toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // supportactionbar home icon is grey in color while adding toolbarnavigationicon is of
        // our defined color
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            //ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(title);
        }
        // toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
    }

    protected void switchWhenUserSignsIn() {

    }

    protected void switchWhenUserSignsOut() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = firebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    // User is signed in
                    switchWhenUserSignsIn();
                } else {
                    // User is signed out
                    switchWhenUserSignsOut();
                }
            }
        };

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        //getWindow().setBackgroundDrawableResource(R.drawable.nontransparentbackground);
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.setMessage(getString(R.string.loading));
        mProgressDialog.show();
    }

    public void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService
                    (INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void hideSoftKeyboardAtActivityStart() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService
                (INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            /*case R.id.action_signout:
                FirebaseAuth.getInstance().signOut();
                return true;*/
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                Toast.makeText(AppCompatBase.this, "Yet to be developed!", Toast.LENGTH_SHORT)
                        .show();
        }
        return super.onOptionsItemSelected(item);
    }
}
