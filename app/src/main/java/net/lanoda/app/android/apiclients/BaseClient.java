package net.lanoda.app.android.apiclients;

import android.content.Context;

import net.lanoda.app.android.R;
import net.lanoda.app.android.apihelpers.ApiTask;
import net.lanoda.app.android.apihelpers.IApiTaskCallback;
import net.lanoda.app.android.models.BaseModel;
import net.lanoda.app.android.models.factories.IModelFactory;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

/**
 * Created by isaac on 8/20/2016.
 */
public class BaseClient<T extends BaseModel> {

    private static BaseClient mInstance;
    private Context baseContext;
    private IModelFactory<T> modelFactory;

    // Volley Variables
    //private RequestQueue mRequestQueue;

    public BaseClient(Context base, IModelFactory<T> factory) {
        baseContext = base;
        //mRequestQueue = getRequestQueue();
        //mRequestQueue.start();
        modelFactory = factory;
    }

    /*
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(baseContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public void CancelTask(String TAG) {
        mRequestQueue.cancelAll(TAG);
    }
    */

    public String GetBaseApiUrl() {
        return baseContext.getString(R.string.api_root_url);
    }

    public void DeleteAsync(String url, IApiTaskCallback<T> callback) {
        AsyncRequest(url, "DELETE", null, callback);
    }

    public void GetAsync(String url, IApiTaskCallback<T> callback) {
        AsyncRequest(url, "GET", null, callback);
    }

    public void PostAsync(String url, T model, IApiTaskCallback<T> callback) {
        JSONObject postData = null;
        if (model != null) {
            postData = model.ToJson();
        }
        AsyncRequest(url, "POST", postData, callback);
    }

    public void PutAsync(String url, T model, IApiTaskCallback<T> callback) {
        JSONObject postData = null;
        if (model != null) {
            postData = model.ToJson();
        }
        AsyncRequest(url, "PUT", postData, callback);
    }

    /**
     * Add a request to the volley queue.
     *
     * @param url - Typically an Api endpoint.
     * @param requestMethod - The 'type' of request (GET, POST, etc.)
     * @param jsonObject - Optional json Object for POST and PUT requests.
     * @param callback - Callback function for when request completes.
     */
    private void AsyncRequest(String url, String requestMethod, JSONObject jsonObject,
                              final IApiTaskCallback<T> callback) {


        ApiTask<T> task = new ApiTask<>(url, requestMethod, modelFactory, jsonObject, callback);
        task.execute(new String[]{});

        /*
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(requestMethod, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        ApiResult<T> apiResult = new ApiResult<T>();

                        // Transform content into model.
                        try {
                            JSONObject responseContent = response.getJSONObject("Content");
                            apiResult.Content.ToModel(responseContent);

                            JSONArray jsonErrors = response.getJSONArray("Errors");
                            for(int i = 0; i < jsonErrors.length(); i++) {
                                JSONObject errorObj = jsonErrors.getJSONObject(i);
                                String id = errorObj.getString("Id");
                                String message = errorObj.getString("Message");
                                ApiError apiError = new ApiError(id, message);
                                apiResult.Errors.add(apiError);
                            }

                            apiResult.IsSuccess = response.getBoolean("IsSuccess");
                            apiResult.Status = 200;

                        } catch (JSONException e) {
                            apiResult.Errors.add(new ApiError("BaseClient_JsonError", ""));
                        }

                        callback.onTaskFinished(apiResult);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        String apiErrorId = "ApiError_GET_VolleyError";

                        int status = 0;
                        String message = null;
                        if (error != null) {
                            message = error.getMessage();
                            if (error.networkResponse != null) {
                                status = error.networkResponse.statusCode;
                            }
                        }

                        ApiResult<T> apiResult = new ApiResult<T>(null, false, apiErrorId, message);
                        apiResult.Status = status;

                        callback.onTaskFinished(apiResult);
                    }
                }
        );

        jsonObjRequest.setTag(TAG);

        RequestQueue requestQueue = getRequestQueue();
        requestQueue.add(jsonObjRequest);
        */
    }
}
