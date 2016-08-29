package net.lanoda.app.android.models;

import net.lanoda.app.android.models.factories.ApiTokenModelFactory;
import net.lanoda.app.android.models.factories.IModelFactory;
import net.lanoda.app.android.models.factories.UserModelFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by isaac on 8/14/2016.
 */
public class UserModel extends BaseModel {

    public int UserId;
    public String FirstName;
    public String LastName;
    public int ImageId;
    public String Email;
    public Date LastLoginAt;

    public UserModel() {};

    public IModelFactory<UserModel> GetFactory() {
        return new UserModelFactory();
    }

    @Override
    public boolean ToModel(JSONObject jsonObj) {

        DateFormat df = DateFormat.getDateInstance();

        try {
            this.UserId = jsonObj.getInt("id");
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
        return null;
    }

    @Override
    public void Copy(Object model) {

    }
}
