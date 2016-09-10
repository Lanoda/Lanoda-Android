package net.lanoda.app.android.apihelpers;

import java.util.Date;

/**
 * Created by isaac on 8/20/2016.
 */
public class ApiError {

    public String Id;
    public String Message;
    public Date Time;

    public ApiError(){ }

    public ApiError(String id, String message) {
        this.Id = id;
        this.Message = message;
    }

    public ApiError(String id, String message, Date time) {
        this.Id = id;
        this.Message = message;
        this.Time = time;
    }
}
