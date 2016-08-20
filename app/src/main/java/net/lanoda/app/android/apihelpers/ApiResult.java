package net.lanoda.app.android.apihelpers;

import net.lanoda.app.android.models.BaseModel;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by isaac on 8/20/2016.
 */
public class ApiResult<T extends BaseModel> {

    public T Content;
    public List<ApiError> Errors;
    public boolean IsSuccess;
    public String Message;

    public ApiResult () {}

    public ApiResult (T content) {
        this.Content = content;
        this.IsSuccess = true;
        this.Message = null;
        this.Errors = null;
    }

    public ApiResult (T content, boolean isSuccess) {
        this.Content = content;
        this.IsSuccess = isSuccess;
        this.Message = null;
        this.Errors = null;
    }

    public ApiResult (T content, boolean isSuccess, String errorId, String errorMessage) {
        this.Content = content;
        this.IsSuccess = isSuccess;
        this.Errors.add(new ApiError(errorId, errorMessage));
    }

    public ApiResult (T content, boolean isSuccess, List<ApiError> errors) {
        this.Content = content;
        this.IsSuccess = isSuccess;
        this.Errors = errors;
    }
}