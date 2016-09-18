package net.lanoda.app.android.models;

import net.lanoda.app.android.modelfactories.IModelFactory;

import org.json.JSONObject;

/**
 * Created by isaac on 8/14/2016.
 */
public abstract class BaseModel {

    public abstract IModelFactory GetFactory();

    public abstract boolean ToModel(JSONObject jsonObj);

    public abstract JSONObject ToJson();

    public abstract void Copy(Object model);
}
