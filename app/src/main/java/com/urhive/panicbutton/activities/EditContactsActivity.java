package com.urhive.panicbutton.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.urhive.panicbutton.R;
import com.urhive.panicbutton.adapters.ContactsRecyclerViewAdapter;
import com.urhive.panicbutton.helpers.DBHelper;
import com.urhive.panicbutton.models.IceContact;

import java.util.List;

public class EditContactsActivity extends AppCompatBase {

    public static final String EDIT = "EDIT";
    private static final String TAG = "EditContactsActivity";
    private static final int REQUEST_CODE_PICK_CONTACTS = 11;
    private static List<IceContact> contacts;
    private RelativeLayout addContactRL;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contacts);

        setToolbar(getString(R.string.edit_contacts));

        addContactRL = (RelativeLayout) findViewById(R.id.addContactRL);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(EditContactsActivity.this));
        contacts = DBHelper.getContactsList(EditContactsActivity.this);

        addContactRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract
                        .CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, REQUEST_CODE_PICK_CONTACTS);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case REQUEST_CODE_PICK_CONTACTS:
                    Uri uriContact = data.getData();
                    setContactsInfo(uriContact);
                    break;
            }
        } else {
            Toast.makeText(this, getString(R.string
                    .there_is_some_problem_try_again_later_check_for_permissions), Toast
                    .LENGTH_SHORT).show();
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     *
     * @param uri
     */
    private void setContactsInfo(Uri uri) {
        Cursor cursor;
        //Query the content uri
        cursor = getContentResolver().query(uri, null, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int contactIdIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone
                .RAW_CONTACT_ID);

        int id = cursor.getInt(contactIdIndex);
        String photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds
                .Phone.PHOTO_URI));
        String phoneNo = cursor.getString(phoneIndex);
        String name = cursor.getString(nameIndex);

        IceContact contact = new IceContact(id, photoUri, name, phoneNo);
        Log.i(TAG, "setContactsInfo: Selected contact is " + contact.toString());

        contacts = DBHelper.getContactsList(EditContactsActivity.this);

        int flag = 0;
        for (IceContact c : contacts) {
            if (c.getId() == contact.getId()) {
                flag = 1;
            }
        }
        if (flag == 0) {
            // i.e. contact is not there in the list
            contacts.add(contact);
            Log.i(TAG, "setContactsInfo: contact not present already");
            DBHelper.updateContactsToPreferences(EditContactsActivity.this, contacts);
            recyclerView.setAdapter(new ContactsRecyclerViewAdapter(EditContactsActivity.this,
                    contacts, EDIT));
        } else {
            Log.i(TAG, "setContactsInfo: contact was already in the list");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.setAdapter(new ContactsRecyclerViewAdapter(EditContactsActivity.this,
                contacts, EDIT));
    }
}
