package net.lanoda.app.android.apiclients;

import android.content.Context;

import net.lanoda.app.android.apihelpers.IApiTaskCallback;
import net.lanoda.app.android.models.ApiTokenModel;
import net.lanoda.app.android.modelfactories.ApiTokenModelFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by isaac on 8/20/2016.
 */
public class AuthClient extends BaseClient<ApiTokenModel> {

    public AuthClient(Context base) {
        super(base, new ApiTokenModelFactory());
    }

    public void AuthorizeApp(String clientId, String email, String password,
                             IApiTaskCallback<ApiTokenModel> callback) {

        String endpoint = GetBaseApiUrl() + "authorize/client";
        String query = "";
        try {
            query = "?client_id=" + URLEncoder.encode(clientId, "UTF-8")
                    + "&email=" + URLEncoder.encode(email, "UTF-8")
                    + "&password=" + URLEncoder.encode(password, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        endpoint += query;

        PostAsync(endpoint, null, callback);
    }

    public void RequestApiToken(String clientId, String clientSecret, String email,
                                  IApiTaskCallback<ApiTokenModel> callback) {

        String endpoint = GetBaseApiUrl();
        try {
            endpoint += "api-token/request"
                    + "?client_id=" + URLEncoder.encode(clientId, "UTF-8")
                    + "&client_secret=" + URLEncoder.encode(clientSecret, "UTF-8")
                    + "&email=" + URLEncoder.encode(email, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        PostAsync(endpoint, null, callback);
    }

    public void RefreshApiToken(String clientId, String apiToken,
                                IApiTaskCallback<ApiTokenModel> callback) {

        String endpoint = GetBaseApiUrl();
        try {
            endpoint += "api-token/refresh"
                    + "?client_id=" + URLEncoder.encode(clientId, "UTF-8")
                    + "&api_token=" + URLEncoder.encode(apiToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        PostAsync(endpoint, null, callback);
    }
}
