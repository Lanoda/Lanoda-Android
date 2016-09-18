package net.lanoda.app.android.modelfactories;

import net.lanoda.app.android.models.ApiTokenModel;

/**
 * Created by isaac on 8/21/2016.
 */
public class ApiTokenModelFactory implements IModelFactory<ApiTokenModel> {

    public ApiTokenModel create() {
        return new ApiTokenModel();
    }

}
