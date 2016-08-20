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

    private RequestQueue volleyRequestQueue;
    private Context baseContext;
    private String baseApiUrl;
    private IModelFactory<T> modelFactory;

    public BaseClient(Context base, IModelFactory<T> factory) {
        baseContext = base;
        baseApiUrl = base.getString(R.string.api_root_url);
        volleyRequestQueue = Volley.newRequestQueue(base);
        modelFactory = factory;
    }

    public ApiResult<T> GetAsync(String apiPath, String TAG) {

        final ApiResult<T> apiResult = new ApiResult<T>(modelFactory.create());
        String url = baseApiUrl + apiPath;

        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    // Display the first 500 characters of the response string.
                    apiResult.Content.TransformJson(response);
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

    public ApiResult PutAsync(String apiPath, String TAG) {

        final ApiResult apiResult = new ApiResult();
        String url = baseApiUrl + apiPath;

        return apiResult;
    }

}
