package net.lanoda.app.android.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by isaac on 8/21/2016.
 */
public class ApiTokenModel extends BaseModel {

    public int ApiTokenId;
    public String ApiToken;
    public String ClientId;
    public int UserId;
    public Date Expires;


    public boolean ToModel(JSONObject jsonObj) {

        DateFormat df = DateFormat.getDateInstance();

        try {
            this.ApiTokenId = jsonObj.getInt("id");
            this.ApiToken = jsonObj.getString("api_token");
            this.ClientId = jsonObj.getString("client_id");
            this.UserId = jsonObj.getInt("user_id");
            this.Expires = df.parse(jsonObj.getString("expires"));

        } catch(JSONException|ParseException e) {
            return false;
        }

        return true;
    }

    public JSONObject ToJson() {

        DateFormat df = DateFormat.getDateInstance();
        JSONObject obj = new JSONObject();

        try {
            obj.put("id", this.ApiTokenId);
            obj.put("api_token", this.ApiToken);
            obj.put("client_id", this.ClientId);
            obj.put("user_id", this.UserId);
            obj.put("expires", this.Expires);
        } catch(JSONException e) {
            return null;
        }

        return obj;
    }

}
