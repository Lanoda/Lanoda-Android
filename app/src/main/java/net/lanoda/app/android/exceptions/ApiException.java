package net.lanoda.app.android.exceptions;

import net.lanoda.app.android.apihelpers.ApiError;

import java.util.List;

/**
 * Created by isaac on 8/20/2016.
 */
public class ApiException extends Exception {

    public List<ApiError> ErrorList;

    public ApiException(String message, List<ApiError> errors) {
        super(message);
        ErrorList = errors;
    }

}
