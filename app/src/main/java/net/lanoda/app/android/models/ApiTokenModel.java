package net.lanoda.app.android.models;

import net.lanoda.app.android.models.BaseModel;
import net.lanoda.app.android.models.factories.ApiTokenModelFactory;
import net.lanoda.app.android.models.factories.IModelFactory;

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

    public ApiTokenModel() {}

    public IModelFactory<ApiTokenModel> GetFactory() {
        return new ApiTokenModelFactory();
    }

    public boolean ToModel(JSONObject jsonObj) {

        DateFormat df = DateFormat.getDateInstance();

        try {
            this.ApiTokenId = jsonObj.getInt("id");
            this.ApiToken = jsonObj.getString("api_token");
            this.ClientId = jsonObj.getString("client_id");
            this.UserId = jsonObj.getInt("user_id");

            JSONObject expireDate = jsonObj.getJSONObject("expires");
            this.Expires = df.parse(expireDate.getString("date"));

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

    public void Copy(Object model) {
        ApiTokenModel realModel = (ApiTokenModel) model;

        this.ApiTokenId = realModel.ApiTokenId;
        this.ApiToken = realModel.ApiToken;
        this.ClientId = realModel.ClientId;
        this.UserId = realModel.UserId;
        this.Expires = realModel.Expires;
    }
}
