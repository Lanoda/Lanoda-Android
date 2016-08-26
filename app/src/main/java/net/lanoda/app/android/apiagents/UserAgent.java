package net.lanoda.app.android.apiagents;

import android.content.Context;

import net.lanoda.app.android.apiclients.UserClient;

/**
 * Created by isaac on 8/25/2016.
 */
public class UserAgent extends BaseAgent {

    public UserClient userClient;

    public UserAgent(Context base) {
        super(base);
        userClient = new UserClient(base);
    }


}
