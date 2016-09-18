package net.lanoda.app.android.activities;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import net.lanoda.app.android.R;
import net.lanoda.app.android.apiclients.ContactClient;
import net.lanoda.app.android.apiclients.ContactListClient;
import net.lanoda.app.android.apihelpers.ApiListParams;
import net.lanoda.app.android.apihelpers.ApiResult;
import net.lanoda.app.android.apihelpers.IApiTaskCallback;
import net.lanoda.app.android.models.contacts.ContactListModel;
import net.lanoda.app.android.models.contacts.ContactModel;

public class ContactsActivity extends AppCompatActivity {

    protected GridView contactsGrid;
    protected TextView contactsError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contactsError = (TextView) findViewById(R.id.contacts_error);
        contactsGrid = (GridView) findViewById(R.id.contacts_grid);


        RetrieveContacts();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    protected void RetrieveContacts() {
        IApiTaskCallback<ContactListModel> apiTaskCallback =
                new IApiTaskCallback<ContactListModel>() {

                    @Override
                    public void onTaskFinished(ApiResult<ContactListModel> result) {
                        if (result.IsSuccess && result.Content != null) {
                            ArrayAdapter<ContactModel> arrayAdapter = new ArrayAdapter<>(
                                    ContactsActivity.this,
                                    R.layout.contacts_grid_item,
                                    R.id.contacts_grid_item_text,
                                    result.Content.Contacts);

                            contactsGrid.setAdapter(arrayAdapter);
                        } else {
                            String errorText = "";
                            for(int i = 0; i < result.Errors.size(); i++) {
                                errorText += result.Errors.get(i).Id;
                                errorText += ": ";
                                errorText += result.Errors.get(i).Message;
                                errorText += "\n";
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

}
