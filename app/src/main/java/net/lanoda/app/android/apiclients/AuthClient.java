package net.lanoda.app.android.apiclients;

import android.content.Context;

import net.lanoda.app.android.R;
import net.lanoda.app.android.apihelpers.ApiResult;
import net.lanoda.app.android.models.ApiTokenModel;
import net.lanoda.app.android.models.UserModel;
import net.lanoda.app.android.models.factories.ApiTokenModelFactory;

/**
 * Created by isaac on 8/20/2016.
 */
public class AuthClient extends BaseClient<ApiTokenModel> {

    public AuthClient(Context base) {
        super(base, new ApiTokenModelFactory());
    }

    public ApiResult<ApiTokenModel> AuthorizeApp(String email, String password) {

        String endpoint = GetBaseApiUrl() + "auth/app"
                + "?email=" + email
                + "&password" + password;

        return GetAsync(endpoint, "AuthorizeApp");
    }

    public ApiResult<ApiTokenModel> RequestApiToken(
            String email, String clientId, String clientSecret) {

        String endpoint = GetBaseApiUrl();
        endpoint += "api-token/request"
                + "?client_id=" + clientId
                + "&client_secret=" + clientSecret
                + "&email=" + email;

        return GetAsync(endpoint, "RequestApiToken");
    }

    public ApiResult<ApiTokenModel> RefreshApiToken(String clientId, String apiToken) {

        String endpoint = GetBaseApiUrl();
        endpoint += "api-token/refresh"
                + "?client_id=" + clientId
                + "&api_token=" + apiToken;

        return GetAsync(endpoint, "RefreshApiToken");
    }
}
