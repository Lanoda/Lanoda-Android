package net.lanoda.app.android.models;

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

    public void TransformJson(JSONObject jsonObj) {

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm");

        try {
            this.UserId = jsonObj.getInt("id");
            this.FirstName = jsonObj.getString("firstname");
            this.LastName = jsonObj.getString("lastname");
            this.ImageId = jsonObj.getInt("image_id");
            this.Email = jsonObj.getString("email");
            this.LastLoginAt = df.parse(jsonObj.getString("last_login_at"));

        } catch(JSONException e) {
            // TODO: Implement

        } catch(ParseException e) {
            // TODO: Implement
        }
    }
}
