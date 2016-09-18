package net.lanoda.app.android.models.contacts;

import net.lanoda.app.android.modelfactories.contacts.ContactListModelFactory;
import net.lanoda.app.android.models.BaseListModel;
import net.lanoda.app.android.modelfactories.IModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by isaac on 9/17/2016.
 */
public class ContactListModel extends BaseListModel {

    public List<ContactModel> Contacts;

    public ContactListModel() {}

    @Override
    public IModelFactory<ContactListModel> GetFactory() {
        return new ContactListModelFactory();
    }

    @Override
    public boolean ToModel(JSONObject jsonObj) {

        try {

            JSONArray contactArray = jsonObj.getJSONArray("Contacts");

            this.Contacts = new ArrayList<>();
            for(int i = 0; i < contactArray.length(); i++) {
                ContactModel contact = new ContactModel();
                contact.ToModel((JSONObject) contactArray.get(i));
                this.Contacts.add(contact);
            }

            this.TotalResults = jsonObj.getInt("TotalResults");
            this.PageIndex = jsonObj.getInt("PageIndex");
            this.PageSize = jsonObj.getInt("PageSize");

        } catch(JSONException e) {
            // TODO: Implement
            return false;
        }

        return true;
    }

    @Override
    public JSONObject ToJson() {
        return null;
    }

    @Override
    public void Copy(Object model) {
        ContactListModel realModel = (ContactListModel) model;

        this.Contacts = realModel.Contacts;
        this.PageIndex = realModel.PageIndex;
        this.PageSize = realModel.PageSize;
        this.TotalResults = realModel.TotalResults;
    }
}
