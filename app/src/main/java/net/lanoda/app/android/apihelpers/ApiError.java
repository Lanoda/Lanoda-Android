package net.lanoda.app.android.apihelpers;

/**
 * Created by isaac on 8/20/2016.
 */
public class ApiError {

    public String Id;
    public String Message;

    public ApiError(){ }

    public ApiError(String id, String message) {
        this.Id = id;
        this.Message = message;
    }
}
