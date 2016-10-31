package net.lanoda.app.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.lanoda.app.android.R;
import net.lanoda.app.android.adapters.ContactsArrayAdapter;
import net.lanoda.app.android.apiclients.ContactListClient;
import net.lanoda.app.android.apihelpers.ApiListParams;
import net.lanoda.app.android.apihelpers.ApiResult;
import net.lanoda.app.android.apihelpers.IApiTaskCallback;
import net.lanoda.app.android.models.contacts.ContactListModel;

public class ContactListActivity extends AppCompatActivity {

    protected GridView gvContactsGrid;
    protected TextView tvContactsError;
    protected SwipeRefreshLayout srlContactsSwipeRefresh;
    protected FloatingActionButton fabCreateContact;
    protected boolean mIsLargeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.contact_list_toolbar);
        //setSupportActionBar(toolbar);

        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);

        tvContactsError = (TextView) findViewById(R.id.contacts_error);
        gvContactsGrid = (GridView) findViewById(R.id.contacts_grid);
        fabCreateContact = (FloatingActionButton) findViewById(R.id.create_contact_fab);
        srlContactsSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.contacts_grid_refresh);

        if (fabCreateContact != null) {
            fabCreateContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowCreateContact();
                }
            });
        }

        if (srlContactsSwipeRefresh != null) {
            srlContactsSwipeRefresh.setColorSchemeResources(R.color.colorAccent);
            srlContactsSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    srlContactsSwipeRefresh.setRefreshing(true);
                    tvContactsError.setText("");
                    RetrieveContacts();
                }
            });
            srlContactsSwipeRefresh.post(new Runnable() {
                @Override
                public void run() {
                    srlContactsSwipeRefresh.setRefreshing(true);
                }
            });
        }

        RetrieveContacts();
    }

    protected void RetrieveContacts() {
        gvContactsGrid.removeAllViewsInLayout();

        IApiTaskCallback<ContactListModel> apiTaskCallback =
                new IApiTaskCallback<ContactListModel>() {

                    @Override
                    public void onTaskFinished(ApiResult<ContactListModel> result) {
                        if (result.IsSuccess && result.Content != null) {
                            tvContactsError.setText("");
                            ContactsArrayAdapter arrayAdapter = new ContactsArrayAdapter(
                                    ContactListActivity.this, result.Content.Contacts);

                            gvContactsGrid.setAdapter(arrayAdapter);
                            gvContactsGrid.setVisibility(View.VISIBLE);
                        } else {
                            String errorText = "Error Retrieving Contacts.\n";
                            if (result.Errors != null) {
                                for(int i = 0; i < result.Errors.size(); i++) {
                                    errorText += result.Errors.get(i).Id;
                                    errorText += ": ";
                                    errorText += result.Errors.get(i).Message;
                                    errorText += "\n";
                                }
                            }
                            tvContactsError.setText(errorText);
                        }
                        srlContactsSwipeRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onTaskUpdate(Integer... values) {

                    }

                    @Override
                    public void onTaskCancelled() {
                        srlContactsSwipeRefresh.setRefreshing(false);
                    }
                };

        ApiListParams listParams = new ApiListParams();
        listParams.PageIndex = 0;
        listParams.PageSize = 100;

        ContactListClient client = new ContactListClient(this);
        client.GetContactsForUser(listParams, apiTaskCallback);
    }

    protected void ShowCreateContact() {
        // Show Create Contact Activity
        Intent createContactIntent = new Intent(this, ContactCreateActivity.class);
        startActivity(createContactIntent);
    }
}
