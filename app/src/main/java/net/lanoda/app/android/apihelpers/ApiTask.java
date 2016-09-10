package net.lanoda.app.android.apihelpers;

import android.os.AsyncTask;
import android.util.Log;

import net.lanoda.app.android.models.BaseModel;
import net.lanoda.app.android.models.factories.IModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by isaac on 8/27/2016.
 */
public class ApiTask<T extends BaseModel> extends AsyncTask<String[], Integer, ApiResult<T>> {

    HttpsURLConnection conn;
    String UrlString;
    String RequestMethod;
    IModelFactory<T> modelFactory;
    JSONObject data;
    IApiTaskCallback<T> Callback;

    public ApiTask(String url, String method, IModelFactory<T> factory, JSONObject jsonObject, IApiTaskCallback<T> callback) {
        UrlString = url;
        RequestMethod = method;
        modelFactory = factory;
        data = jsonObject;
        Callback = callback;
    }

    @Override
    protected ApiResult<T> doInBackground(String[]... params) {
        ApiResult<T> apiResult = new ApiResult<>();
        try {
            String returnedData = downloadUrl(UrlString, RequestMethod);
            JSONObject jsonObj = new JSONObject(returnedData);

            if (jsonObj.has("Content")) {
                JSONObject content = jsonObj.getJSONObject("Content");
                if (content != null) {
                    apiResult.Content = modelFactory.create();
                    apiResult.Content.ToModel(content);
                }
            }

            if (jsonObj.has("IsSuccess")) {
                apiResult.IsSuccess = jsonObj.getBoolean("IsSuccess");
            }

            if (jsonObj.has("Errors")) {
                JSONArray errors = jsonObj.getJSONArray("Errors");
                for (int i = 0; i < errors.length(); i++) {
                    JSONObject error = (JSONObject) errors.get(i);
                    String errId = null;
                    String errMsg = null;
                    Date errTime = null;

                    if (error.has("Id")) {
                        errId = error.getString("id");
                    }
                    if (error.has("Message")) {
                        errMsg = error.getString("Message");
                    }
                    if (error.has("Time")) {
                        DateFormat df = DateFormat.getDateInstance();
                        try {
                            errTime = df.parse(error.getString("Time"));
                        } catch(ParseException e) {
                            // Couldn't get Time
                        }
                    }

                    apiResult.Errors.add(new ApiError(errId, errMsg, errTime));
                }
            }

        } catch (JSONException|IOException e) {
            apiResult.Errors = new ArrayList<>();
            apiResult.Errors.add(new ApiError("JSON_Or_IOException", e.getMessage()));
            apiResult.IsSuccess = false;
        }

        return apiResult;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(ApiResult<T> result) {
        Callback.onTaskFinished(result);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Callback.onTaskUpdate(values);
    }

    @Override
    protected void onCancelled(ApiResult<T> result) {
        Callback.onTaskCancelled();
    }


    private String downloadUrl(String urlString, String requestMethod) throws IOException {

        StringBuilder result = new StringBuilder();

        try {

            URL url = new URL(urlString);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(10 * 1000);
            conn.setConnectTimeout(15 * 1000);
            conn.setRequestMethod(requestMethod);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // Starts the query
            conn.connect();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }

        return result.toString();
    }
}
