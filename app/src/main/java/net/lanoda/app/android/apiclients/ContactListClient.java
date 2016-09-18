package net.lanoda.app.android.apiclients;

import android.content.Context;

import net.lanoda.app.android.apihelpers.ApiListParams;
import net.lanoda.app.android.apihelpers.IApiTaskCallback;
import net.lanoda.app.android.modelfactories.IModelFactory;
import net.lanoda.app.android.modelfactories.contacts.ContactListModelFactory;
import net.lanoda.app.android.models.contacts.ContactListModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by isaac on 9/17/2016.
 */
public class ContactListClient extends BaseClient<ContactListModel> {

    public ContactListClient(Context base) {
        super(base, new ContactListModelFactory());
    }

    public void GetContactsForUser(ApiListParams listParams,
                                   IApiTaskCallback<ContactListModel> callback) {

        String endpoint = GetBaseApiUrl() + "contacts";
        String query = "";
        try {

            query = "?filters=" + URLEncoder.encode(listParams.GetFilterString(), "UTF-8")
                    + "&sort=" + URLEncoder.encode(listParams.GetSortString(), "UTF-8")
                    + "&pageIndex=" + listParams.PageIndex
                    + "&pageSize=" + listParams.PageSize;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        endpoint += query;

        GetAsync(endpoint, callback);
    }
}
