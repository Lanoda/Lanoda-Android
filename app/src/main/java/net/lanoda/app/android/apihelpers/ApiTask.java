package net.lanoda.app.android.apihelpers;

import android.os.AsyncTask;

import net.lanoda.app.android.models.BaseModel;
import net.lanoda.app.android.modelfactories.IModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by isaac on 8/27/2016.
 *
 * Handles asynchronous calls to the Api.
 */
public class ApiTask<T extends BaseModel> extends AsyncTask<String[], Integer, ApiResult<T>> {

    private HttpURLConnection connHttp;
    private HttpsURLConnection connHttps;
    private String UrlString;
    private String RequestMethod;
    private String ApiToken;
    private int ResponseCode = 0;
    private String ResponseMsg;
    private IModelFactory<T> modelFactory;
    private JSONObject data;
    private IApiTaskCallback<T> Callback;

    public ApiTask(String url, String method, String apiToken, IModelFactory<T> factory,
                   JSONObject jsonObject, IApiTaskCallback<T> callback) {
        UrlString = url;
        RequestMethod = method;
        ApiToken = apiToken;
        modelFactory = factory;
        data = jsonObject;
        Callback = callback;
    }

    @Override
    protected ApiResult<T> doInBackground(String[]... params) {
        ApiResult<T> apiResult = new ApiResult<>();
        try {
            String returnedData = downloadUrl(UrlString, RequestMethod);
            if (returnedData.length() <= 0) {

                String errorId = "ReturnedData_NoContent";
                if (ResponseCode != 0) {
                    errorId = Integer.toString(ResponseCode);
                }
                String errorMsg = "No content.";
                if (ResponseMsg != null) {
                    errorMsg = ResponseMsg;
                }

                apiResult.Errors = new ArrayList<>();
                apiResult.Errors.add(new ApiError(errorId, errorMsg));
                apiResult.IsSuccess = false;
                return apiResult;
            }

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
                        errId = error.getString("Id");
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
                    apiResult.Errors = new ArrayList<>();
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

            if (url.getProtocol().equals("https")) {
                connHttps = (HttpsURLConnection) url.openConnection();

                connHttps.setReadTimeout(10 * 1000);
                connHttps.setConnectTimeout(15 * 1000);
                connHttps.setRequestMethod(requestMethod);
                connHttps.setDoInput(true);
                if (requestMethod.equals("POST") || requestMethod.equals("PUT")) {
                    connHttps.setDoOutput(true);
                    connHttps.getOutputStream().write(data.toString().getBytes("UTF-8"));
                }
                connHttps.setRequestProperty("Content-Type", "application/json;charset=utf-8");

                if (ApiToken != null) {
                    connHttps.setRequestProperty("lanoda-api-token", ApiToken);
                }

                // Starts the query
                connHttps.connect();

                int responseCode = connHttps.getResponseCode();

                if (responseCode == 200) {
                    InputStream in = new BufferedInputStream(connHttps.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();
                    in.close();
                } else {
                    ResponseCode = responseCode;
                    ResponseMsg = connHttps.getResponseMessage();
                    return (String) connHttps.getContent();
                }
            } else {
                connHttp = (HttpURLConnection) url.openConnection();

                connHttp.setReadTimeout(10 * 100000);
                connHttp.setConnectTimeout(15 * 100000);
                connHttp.setRequestMethod(requestMethod);
                connHttp.setDoInput(true);
                if (requestMethod.equals("POST")) {
                    connHttp.setDoOutput(true);
                }
                connHttp.setRequestProperty("Content-Type", "application/json;charset=utf-8");

                if (ApiToken != null) {
                    connHttp.setRequestProperty("lanoda-api-token", ApiToken);
                }

                // Starts the query
                connHttp.connect();

                int responseCode = connHttp.getResponseCode();

                if (responseCode == 200) {
                    InputStream in = new BufferedInputStream(connHttp.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    reader.close();
                    in.close();
                } else {
                    ResponseCode = responseCode;
                    ResponseMsg = connHttp.getResponseMessage();
                    return (String) connHttp.getContent();
                }
            }

        } catch (ConnectException e) {
            //e.printStackTrace();
            String conErrId = "ConnectionFailed";
            String conErrMsg = "Connection could not be established.";
            String errObj = String.format("[{'Id': '%s', 'Message': '%s'}]", conErrId, conErrMsg);
            return String.format("{'IsSuccess': false, 'Content': {}, 'Errors': %s}", errObj);
        } finally {
            if (connHttp != null) {
                connHttp.disconnect();
            }

            if (connHttps != null) {
                connHttps.disconnect();
            }
        }

        return result.toString();
    }
}
