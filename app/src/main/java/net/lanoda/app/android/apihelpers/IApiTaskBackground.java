package net.lanoda.app.android.apihelpers;

import net.lanoda.app.android.models.BaseModel;

/**
 * Created by isaac on 8/28/2016.
 */
public interface IApiTaskBackground<T extends BaseModel> {
    public ApiResult<T> doInBackground(String[]...params);
}
