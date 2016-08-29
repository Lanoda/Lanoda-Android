package net.lanoda.app.android.apihelpers;

import net.lanoda.app.android.models.BaseModel;

/**
 * Created by isaac on 8/27/2016.
 */
public interface IApiTaskCallback<T extends BaseModel> {

    void onTaskFinished(ApiResult<T> result);
    void onTaskUpdate(Integer...values);
    void onTaskCancelled();

}
