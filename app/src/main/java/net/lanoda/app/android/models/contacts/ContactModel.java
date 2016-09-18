package net.lanoda.app.android.models.contacts;

import net.lanoda.app.android.models.BaseModel;
import net.lanoda.app.android.modelfactories.contacts.ContactModelFactory;
import net.lanoda.app.android.modelfactories.IModelFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by isaac on 9/17/2016.
 */
public class ContactModel extends BaseModel {

    public int Id;
    public int UserId;
    public int ImageId;
    public String FirstName;
    public String MiddleName;
    public String LastName;
    public String Phone;
    public String Email;
    public String Address;
    public int Age;
    public Date Birthday;

    public ContactModel() {}

    @Override
    public IModelFactory<ContactModel> GetFactory() {
        return new ContactModelFactory();
    }

    @Override
    public boolean ToModel(JSONObject jsonObj) {

        DateFormat df = DateFormat.getDateInstance();

        try {

            this.Id = jsonObj.getInt("id");
            this.FirstName = jsonObj.getString("firstname");
            this.MiddleName = jsonObj.getString("middlename");
            this.LastName = jsonObj.getString("lastname");
            this.Phone = jsonObj.getString("phone");
            this.Email = jsonObj.getString("email");
            this.Address = jsonObj.getString("address");
            this.Age = jsonObj.getInt("age");
            this.Birthday = df.parse(jsonObj.getString("birthday"));

        } catch(JSONException |ParseException e) {
            // TODO: Implement
            return false;
        }

        return true;
    }

    @Override
    public JSONObject ToJson() {

        DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT);
        JSONObject obj = new JSONObject();

        try {
            obj.put("id", this.Id);
            obj.put("user_id", this.UserId);
            obj.put("image_id", this.ImageId);
            obj.put("firstname", this.FirstName);
            obj.put("middlename", this.MiddleName);
            obj.put("lastname", this.LastName);
            obj.put("phone", this.Phone);
            obj.put("email", this.Email);
            obj.put("address", this.Address);
            obj.put("age", this.Age);
            if (this.Birthday != null) {
                obj.put("birthday", df.format(this.Birthday));
            }
        } catch(JSONException e) {
            return null;
        }

        return obj;
    }

    @Override
    public void Copy(Object model) {
        ContactModel realModel = (ContactModel) model;

        this.Id = realModel.Id;
        this.UserId = realModel.UserId;
        this.ImageId = realModel.ImageId;
        this.FirstName = realModel.FirstName;
        this.MiddleName = realModel.MiddleName;
        this.LastName = realModel.LastName;
        this.Phone = realModel.Phone;
        this.Email = realModel.Email;
        this.Address = realModel.Address;
        this.Age = realModel.Age;
        this.Birthday = realModel.Birthday;
    }
}
