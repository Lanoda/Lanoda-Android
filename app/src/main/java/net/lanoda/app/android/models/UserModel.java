package net.lanoda.app.android.models;

import net.lanoda.app.android.modelfactories.IModelFactory;
import net.lanoda.app.android.modelfactories.UserModelFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by isaac on 8/14/2016.
 */
public class UserModel extends BaseModel {

    public int Id;
    public String FirstName;
    public String LastName;
    public int ImageId;
    public String Email;
    public Date LastLoginAt;

    public UserModel() {}

    @Override
    public IModelFactory<UserModel> GetFactory() {
        return new UserModelFactory();
    }

    @Override
    public boolean ToModel(JSONObject jsonObj) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.US);

        try {
            this.Id = jsonObj.getInt("id");
            this.FirstName = jsonObj.getString("firstname");
            this.LastName = jsonObj.getString("lastname");
            this.ImageId = jsonObj.getInt("image_id");
            this.Email = jsonObj.getString("email");
            this.LastLoginAt = df.parse(jsonObj.getString("last_login_at"));

        } catch(JSONException|ParseException e) {
            // TODO: Implement
            return false;
        }

        return true;
    }

    @Override
    public JSONObject ToJson() {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.US);
        JSONObject obj = new JSONObject();

        try {
            obj.put("id", this.Id);
            obj.put("firstname", this.FirstName);
            obj.put("lastname", this.LastName);
            obj.put("image_id", this.ImageId);
            obj.put("email", this.Email);
            if (this.LastLoginAt != null) {
                obj.put("last_login_at", df.format(this.LastLoginAt));
            }
        } catch(JSONException e) {
            return null;
        }

        return obj;
    }

    @Override
    public void Copy(Object model) {
        UserModel realModel = (UserModel) model;

        this.Id = realModel.Id;
        this.FirstName = realModel.FirstName;
        this.LastName = realModel.LastName;
        this.ImageId = realModel.ImageId;
        this.Email = realModel.Email;
        this.LastLoginAt = realModel.LastLoginAt;
    }
}
