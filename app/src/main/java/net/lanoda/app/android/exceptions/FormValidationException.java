package net.lanoda.app.android.exceptions;

import android.view.View;

/**
 * Created by isaac on 8/20/2016.
 */
public class FormValidationException extends Exception {

    public View Field;
    public String Message;

    public FormValidationException(View field, String message) {
        this.Field = field;
        this.Message = message;
    }

}
