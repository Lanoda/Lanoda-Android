package net.lanoda.app.android.apiclients;

import android.content.Context;

import net.lanoda.app.android.apihelpers.ApiResult;
import net.lanoda.app.android.models.UserModel;
import net.lanoda.app.android.models.factories.UserModelFactory;

/**
 * Created by isaac on 8/20/2016.
 */
public class AuthClient extends BaseClient<UserModel> {

    public AuthClient(Context base) {
        super(base, new UserModelFactory());
    }

    public ApiResult<UserModel> AttemptLogin (String email, String password) {
        return GetAsync("users", "LoginAttempt");
    }

}
