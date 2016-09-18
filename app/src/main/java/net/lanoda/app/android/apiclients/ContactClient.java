package net.lanoda.app.android.apiclients;

import android.content.Context;

import net.lanoda.app.android.apihelpers.ApiListParams;
import net.lanoda.app.android.apihelpers.IApiTaskCallback;
import net.lanoda.app.android.models.contacts.ContactListModel;
import net.lanoda.app.android.models.contacts.ContactModel;
import net.lanoda.app.android.modelfactories.contacts.ContactModelFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by isaac on 9/17/2016.
 */
public class ContactClient extends BaseClient<ContactModel> {

    public ContactClient(Context base) {
        super(base, new ContactModelFactory());
    }

    public void DeleteContact(String contactId, IApiTaskCallback<ContactModel> callback) {
        String endpoint = GetBaseApiUrl() + "contacts" + "/" + contactId;
        DeleteAsync(endpoint, callback);
    }

    public void GetContact(int contactId, IApiTaskCallback<ContactModel> callback) {
        String endpoint = GetBaseApiUrl() + "contacts" + "/" + contactId;
        GetAsync(endpoint, callback);
    }

    public void CreateContact(ContactModel contact, IApiTaskCallback<ContactModel> callback) {
        String endpoint = GetBaseApiUrl() + "contacts";
        PostAsync(endpoint, contact, callback);
    }

    public void UpdateContact(int contactId, ContactModel contact,
                              IApiTaskCallback<ContactModel> callback) {
        String endpoint = GetBaseApiUrl() + "contacts" + "/" + contactId;
        PutAsync(endpoint, contact, callback);
    }
}
