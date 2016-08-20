package net.lanoda.app.android.apiagents;

import android.content.Context;

import net.lanoda.app.android.apiclients.AuthClient;
import net.lanoda.app.android.apihelpers.ApiResult;
import net.lanoda.app.android.exceptions.ApiException;
import net.lanoda.app.android.models.UserModel;
import net.lanoda.app.android.models.factories.UserModelFactory;

/**
 * Created by isaac on 8/20/2016.
 */
public class AuthAgent extends BaseAgent {

    public AuthClient authClient;

    public AuthAgent(Context base) {
        super(base);
        authClient = new AuthClient(base);
    }

    public UserModel Login(String email, String password) throws ApiException {

        ApiResult<UserModel> apiResult = authClient.AttemptLogin(email,password);

        if (apiResult.IsSuccess) {
            // TODO: Perform credential storage

            return apiResult.Content;
        } else {
            throw new ApiException(apiResult.Message, apiResult.Errors);
        }

    }
}
