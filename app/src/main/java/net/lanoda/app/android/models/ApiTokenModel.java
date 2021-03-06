package net.lanoda.app.android.models;

import net.lanoda.app.android.modelfactories.ApiTokenModelFactory;
import net.lanoda.app.android.modelfactories.IModelFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by isaac on 8/21/2016.
 */
public class ApiTokenModel extends BaseModel {

    public int Id;
    public String ApiToken;
    public String RefreshToken;
    public String ClientId;
    public Date Expires;

    public ApiTokenModel() {}

    public IModelFactory<ApiTokenModel> GetFactory() {
        return new ApiTokenModelFactory();
    }

    public boolean ToModel(JSONObject jsonObj) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.US);

        try {
            this.Id = jsonObj.getInt("id");
            this.ApiToken = jsonObj.getString("api_token");
            this.ClientId = jsonObj.getString("client_id");
            this.RefreshToken = jsonObj.getString("refresh_token");

            JSONObject expireDate = jsonObj.getJSONObject("expires");
            String dateString = expireDate.getString("date");
            this.Expires = df.parse(dateString);

        } catch(JSONException|ParseException e) {
            return false;
        }

        return true;
    }

    public JSONObject ToJson() {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.US);
        JSONObject obj = new JSONObject();

        try {
            obj.put("id", this.Id);
            obj.put("api_token", this.ApiToken);
            obj.put("client_id", this.ClientId);
            obj.put("refresh_token", this.RefreshToken);
            if (this.Expires != null) {
                obj.put("expires", df.format(this.Expires));
            }
        } catch(JSONException e) {
            return null;
        }

        return obj;
    }

    public void Copy(Object model) {
        ApiTokenModel realModel = (ApiTokenModel) model;

        this.Id = realModel.Id;
        this.ApiToken = realModel.ApiToken;
        this.ClientId = realModel.ClientId;
        this.RefreshToken = realModel.RefreshToken;
        this.Expires = realModel.Expires;
    }
}
