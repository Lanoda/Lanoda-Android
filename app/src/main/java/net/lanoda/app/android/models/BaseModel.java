package net.lanoda.app.android.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by isaac on 8/14/2016.
 */
public abstract class BaseModel {

    public abstract void TransformJson(JSONObject jsonObj);

}
