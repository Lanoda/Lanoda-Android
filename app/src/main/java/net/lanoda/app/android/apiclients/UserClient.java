package net.lanoda.app.android.apiclients;

import android.content.Context;

import net.lanoda.app.android.models.UserModel;
import net.lanoda.app.android.models.factories.UserModelFactory;

/**
 * Created by isaac on 8/20/2016.
 */
public class UserClient extends BaseClient<UserModel> {

    public UserClient(Context base) {
        super(base, new UserModelFactory());
    }

}
