package net.lanoda.app.android.models.factories;

import net.lanoda.app.android.models.UserModel;

/**
 * Created by isaac on 8/20/2016.
 */

public class UserModelFactory implements IModelFactory<UserModel> {
    public UserModel create() {
        return new UserModel();
    }
}
