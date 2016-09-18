package net.lanoda.app.android.modelfactories.contacts;

import net.lanoda.app.android.modelfactories.IModelFactory;
import net.lanoda.app.android.models.contacts.ContactListModel;

/**
 * Created by isaac on 9/17/2016.
 */
public class ContactListModelFactory implements IModelFactory<ContactListModel> {

    public ContactListModel create() {
        return new ContactListModel();
    }

}
