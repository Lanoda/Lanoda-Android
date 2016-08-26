package net.lanoda.app.android.apiclients;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.lanoda.app.android.R;
import net.lanoda.app.android.apihelpers.ApiError;
import net.lanoda.app.android.apihelpers.ApiResult;
import net.lanoda.app.android.models.BaseModel;
import net.lanoda.app.android.models.factories.IModelFactory;

import org.json.JSONObject;

/**
 * Created by isaac on 8/20/2016.
 */
public class BaseClient<T extends BaseModel> {

    private Context baseContext;
    private RequestQueue volleyRequestQueue;
    private IModelFactory<T> modelFactory;

    public BaseClient(Context base, IModelFactory<T> factory) {
        baseContext = base;
        volleyRequestQueue = Volley.newRequestQueue(base);
        modelFactory = factory;
    }

    public String GetBaseApiUrl() {
        return baseContext.getString(R.string.api_root_url);
    }

    private ApiResult<T> AsyncRequest(String url, String TAG, int requestMethod,
                                      JSONObject jsonObject) {

        final ApiResult<T> apiResult = new ApiResult<T>(modelFactory.create());
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(requestMethod, url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Transform content into model.
                        apiResult.Content.ToModel(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String apiErrorId = "ApiError_GET_VolleyError";
                        ApiError apiError = new ApiError(apiErrorId, error.getMessage());
                        apiResult.Errors.add(apiError);
                    }

                }
        );
        jsonObjRequest.setTag(TAG);
        volleyRequestQueue.add(jsonObjRequest);

        return apiResult;
    }

    public ApiResult<T> DeleteAsync(String url, String TAG) {
        return AsyncRequest(url, TAG, Request.Method.DELETE, null);
    }

    public ApiResult<T> GetAsync(String url, String TAG) {
        return AsyncRequest(url, TAG, Request.Method.GET, null);
    }

    public ApiResult<T> PostAsync(String url, String TAG, T model) {
        return AsyncRequest(url, TAG, Request.Method.POST, model.ToJson());
    }

    public ApiResult<T> PutAsync(String url, String TAG, T model) {
        return AsyncRequest(url, TAG, Request.Method.PUT, model.ToJson());
    }

}
