package net.lanoda.app.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import net.lanoda.app.android.R;
import net.lanoda.app.android.adapters.ContactsArrayAdapter;
import net.lanoda.app.android.apiclients.ContactListClient;
import net.lanoda.app.android.apihelpers.ApiListParams;
import net.lanoda.app.android.apihelpers.ApiResult;
import net.lanoda.app.android.apihelpers.IApiTaskCallback;
import net.lanoda.app.android.models.contacts.ContactListModel;

public class ContactListActivity extends AppCompatActivity {

    protected GridView contactsGrid;
    protected TextView contactsError;
    protected boolean mIsLargeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.contact_list_toolbar);
        //setSupportActionBar(toolbar);

        contactsError = (TextView) findViewById(R.id.contacts_error);
        contactsGrid = (GridView) findViewById(R.id.contacts_grid);

        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);

        RetrieveContacts();

        FloatingActionButton createContactFab =
                (FloatingActionButton) findViewById(R.id.create_contact_fab);
        if (createContactFab != null) {
            createContactFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowCreateContact();
                }
            });
        }
    }

    protected void RetrieveContacts() {
        IApiTaskCallback<ContactListModel> apiTaskCallback =
                new IApiTaskCallback<ContactListModel>() {

                    @Override
                    public void onTaskFinished(ApiResult<ContactListModel> result) {
                        if (result.IsSuccess && result.Content != null) {
                            ContactsArrayAdapter arrayAdapter = new ContactsArrayAdapter(
                                    ContactListActivity.this, result.Content.Contacts);

                            contactsGrid.setAdapter(arrayAdapter);
                        } else {
                            String errorText = "Error Retrieving Contacts";
                            if (result.Errors != null) {
                                for(int i = 0; i < result.Errors.size(); i++) {
                                    errorText += result.Errors.get(i).Id;
                                    errorText += ": ";
                                    errorText += result.Errors.get(i).Message;
                                    errorText += "\n";
                                }
                            }
                            contactsError.setText(errorText);
                        }
                    }

                    @Override
                    public void onTaskUpdate(Integer... values) {

                    }

                    @Override
                    public void onTaskCancelled() {

                    }
                };

        ContactListClient client = new ContactListClient(this);

        ApiListParams listParams = new ApiListParams();
        listParams.PageIndex = 0;
        listParams.PageSize = 100;
        client.GetContactsForUser(listParams, apiTaskCallback);
    }

    protected void ShowCreateContact() {
        // Show Create Contact Activity
        Intent createContactIntent = new Intent(this, ContactCreateActivity.class);
        startActivity(createContactIntent);
    }
}
