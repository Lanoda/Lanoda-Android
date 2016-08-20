package net.lanoda.app.android.apiagents;

import android.content.Context;
import android.content.ContextWrapper;

/**
 * Created by isaac on 8/20/2016.
 */
public class BaseAgent extends ContextWrapper {

    public BaseAgent(Context base) {
        super(base);
    }

}
