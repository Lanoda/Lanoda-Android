package net.lanoda.app.android.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.lanoda.app.android.R;
import net.lanoda.app.android.apiclients.AuthClient;
import net.lanoda.app.android.apihelpers.ApiError;
import net.lanoda.app.android.apihelpers.ApiResult;
import net.lanoda.app.android.apihelpers.IApiTaskCallback;
import net.lanoda.app.android.models.ApiTokenModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private AuthClient authClient;


    // UI references.
    private AutoCompleteTextView mEmailView;
    private TextInputEditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView mLoginErrorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        SharedPreferences sharedPref = getBaseContext().getSharedPreferences(
                getString(R.string.api_token_pref_key), Context.MODE_PRIVATE);
        String apiToken = sharedPref.getString(getString(R.string.api_token_token_key), null);

        String expiresString = sharedPref.getString(getBaseContext().getString(
                R.string.api_token_expires_key), null);

        String refreshToken = sharedPref.getString(getBaseContext().getString(
                R.string.api_token_refresh_key), null);

        Date convertedExpires = null;
        if (expiresString != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa", Locale.US);
            try {
                convertedExpires = dateFormat.parse(expiresString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Calendar c = Calendar.getInstance();
        Date today = c.getTime();

        if (apiToken != null && convertedExpires != null && refreshToken != null) {
            if (today.after(convertedExpires)) {
                refreshApiToken(refreshToken);
            } else {
                Intent redirectToContacts = new Intent(this, ContactListActivity.class);
                startActivity(redirectToContacts);
                finish();
            }
        }

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (TextInputEditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLoginErrorView = (TextView) findViewById(R.id.login_error_message);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        if (mEmailSignInButton != null) {
            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });
        }

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Initialize the authentication client.
        authClient = new AuthClient(getBaseContext());

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            showProgress(true);

            IApiTaskCallback<ApiTokenModel> apiTaskCallback = new IApiTaskCallback<ApiTokenModel>() {
                @Override
                public void onTaskFinished(ApiResult<ApiTokenModel> result) {

                    ApiResult<ApiTokenModel> apiResult = new ApiResult<>();

                    apiResult.Content = result.Content;
                    apiResult.Errors = result.Errors;
                    apiResult.IsSuccess = result.IsSuccess;

                    if (apiResult.Errors != null) {
                        String errorText = "";
                        for (ApiError current : result.Errors) {
                            errorText += "\n";
                            errorText += current.Id;
                            errorText += ": ";
                            errorText += current.Message;
                            errorText += "\n";
                        }
                        mLoginErrorView.setText(errorText);
                    } else if (result.Content != null) {
                        SharedPreferences sharedPref = getBaseContext().getSharedPreferences(
                                getString(R.string.api_token_pref_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.api_token_token_key),
                                result.Content.ApiToken);
                        editor.putString(getString(R.string.api_token_refresh_key),
                                result.Content.RefreshToken);

                        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa", Locale.US);
                        editor.putString(getString(R.string.api_token_expires_key),
                                df.format(result.Content.Expires));
                        editor.apply();

                        Intent redirectToContacts = new Intent(LoginActivity.this, ContactListActivity.class);
                        startActivity(redirectToContacts);
                        finish();
                    }


                    showProgress(false);
                }

                @Override
                public void onTaskUpdate(Integer... values) {
                    mLoginErrorView.setText("Login at: " + values[0]);
                }

                @Override
                public void onTaskCancelled() {
                    String toastMessage = "Login Cancelled";
                    int toastDuration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getApplicationContext(), toastMessage, toastDuration);
                    toast.show();
                }
            };

            authClient.AuthorizeApp(getString(R.string.client_id), email, password, apiTaskCallback);

            /*
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
            */
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void refreshApiToken(String refreshToken) {

        IApiTaskCallback<ApiTokenModel> apiTaskCallback = new IApiTaskCallback<ApiTokenModel>() {
            @Override
            public void onTaskFinished(ApiResult<ApiTokenModel> result) {

                ApiResult<ApiTokenModel> apiResult = new ApiResult<>();

                apiResult.Content = result.Content;
                apiResult.Errors = result.Errors;
                apiResult.IsSuccess = result.IsSuccess;

                if (apiResult.Errors != null) {
                    String errorText = "";
                    for (ApiError current : result.Errors) {
                        errorText += "\n";
                        errorText += current.Id;
                        errorText += ": ";
                        errorText += current.Message;
                        errorText += "\n";
                    }
                    mLoginErrorView.setText(errorText);
                } else if (result.Content != null) {
                    SharedPreferences sharedPref = getBaseContext().getSharedPreferences(
                            getString(R.string.api_token_pref_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.api_token_token_key),
                            result.Content.ApiToken);
                    editor.putString(getString(R.string.api_token_refresh_key),
                            result.Content.RefreshToken);
                    editor.putString(getString(R.string.api_token_expires_key),
                            result.Content.Expires.toString());
                    editor.apply();

                    Intent redirectToContacts = new Intent(LoginActivity.this, ContactListActivity.class);
                    startActivity(redirectToContacts);
                    finish();
                }


                showProgress(false);
            }

            @Override
            public void onTaskUpdate(Integer... values) {
                mLoginErrorView.setText("Login at: " + values[0]);
            }

            @Override
            public void onTaskCancelled() {
                String toastMessage = "Login Cancelled";
                int toastDuration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getApplicationContext(), toastMessage, toastDuration);
                toast.show();
            }
        };

        authClient.RefreshApiToken(refreshToken, apiTaskCallback);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

