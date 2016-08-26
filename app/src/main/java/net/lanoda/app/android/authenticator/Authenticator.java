package net.lanoda.app.android.authenticator;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

import net.lanoda.app.android.apiagents.AuthAgent;
import net.lanoda.app.android.apihelpers.ApiResult;
import net.lanoda.app.android.apiclients.AuthClient;
import net.lanoda.app.android.exceptions.ApiException;
import net.lanoda.app.android.exceptions.FormValidationException;
import net.lanoda.app.android.models.ApiTokenModel;
import net.lanoda.app.android.models.UserModel;

/**
 * Created by isaac on 8/20/2016.
 */
public class Authenticator extends ContextWrapper {

    private AuthAgent authAgent;

    public Authenticator(Context base) {
        super(base);
        authAgent = new AuthAgent(base);
    }

    public ApiTokenModel AuthorizeApp(View email, View password) throws FormValidationException {

        if (!isEmailValid(email.toString())) {
            throw new FormValidationException(email, "Email is not valid");
        } else if (!isPasswordValid(password.toString())) {
            throw new FormValidationException(password, "Password not valid");
        }

        ApiTokenModel apiTokenModel = null;

        try {
            apiTokenModel = authAgent.AuthorizeApp(email.toString(), password.toString());
        }
        catch (ApiException e) {
            // TODO: Handle Exception
        }

        return apiTokenModel;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

}
