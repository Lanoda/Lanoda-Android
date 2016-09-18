package net.lanoda.app.android.modelfactories.contacts;

import net.lanoda.app.android.modelfactories.IModelFactory;
import net.lanoda.app.android.models.contacts.ContactModel;

/**
 * Created by isaac on 9/17/2016.
 */
public class ContactModelFactory implements IModelFactory<ContactModel> {

    public ContactModel create() {
        return new ContactModel();
    }

}
