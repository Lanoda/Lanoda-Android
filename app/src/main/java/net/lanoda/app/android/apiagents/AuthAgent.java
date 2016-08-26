package net.lanoda.app.android.apiagents;

import android.content.Context;

import net.lanoda.app.android.apiclients.AuthClient;
import net.lanoda.app.android.apihelpers.ApiResult;
import net.lanoda.app.android.exceptions.ApiException;
import net.lanoda.app.android.models.ApiTokenModel;

/**
 * Created by isaac on 8/20/2016.
 */
public class AuthAgent extends BaseAgent {

    public AuthClient authClient;

    public AuthAgent(Context base) {
        super(base);
        authClient = new AuthClient(base);
    }

    public ApiTokenModel AuthorizeApp(String email, String password) throws ApiException {

        ApiResult<ApiTokenModel> apiResult = authClient.AuthorizeApp(email, password);

        if (apiResult.IsSuccess) {
            return apiResult.Content;
        } else {
            throw new ApiException(apiResult.Message, apiResult.Errors);
        }
    }

    public ApiTokenModel RequestApiToken(String email, String clientId,
                                         String clientSecret) throws ApiException {

        ApiResult<ApiTokenModel> apiResult = authClient.RequestApiToken(email, clientId,
                clientSecret);

        if (apiResult.IsSuccess) {
            return apiResult.Content;
        } else {
            throw new ApiException(apiResult.Message, apiResult.Errors);
        }
    }

    public ApiTokenModel RefreshApiToken(String token, String clientId) {

        ApiResult<ApiTokenModel> apiResult = authClient.RefreshApiToken(token, clientId);
        return apiResult.Content;
    }

}
