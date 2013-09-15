package com.hatch.kite;

import android.util.Log;

import com.hatch.kite.api.ApiConnectionBase;
import com.hatch.kite.api.HttpKeyValuePair;
import com.hatch.kite.api.User;

import org.json.JSONObject;

/**
 * Created by vince on 9/14/13.
 */
public class ApiManager extends ApiConnectionBase {
    public static ApiManager Instance = new ApiManager();
    public static User userSession;

    @Override
    public boolean TryLogin(final User u) {
        postJson(new Action<JSONObject>() {
            @Override
            public void run(JSONObject jsonObject) {
                if (jsonObject == null) {
                    Log.d("KiteLogin", "jsonObject was null");
                } else {
                    Log.d("KiteLogin", jsonObject.toString());
                    //login success
                    // 09-14 23:54:59.836  10649-10649/? D/KiteLogin: {"user":{"id":14,"email":"innatepirate@gmail.com","name":"vmatonis"}}
                    u.setUserPassword("");
                    userSession = u;
                }
            }
        }, new HttpKeyValuePair[]{new HttpKeyValuePair("email", u.getUserEmail()),
                new HttpKeyValuePair("password", u.getUserPassword())}, "authentications");
        return true;
    }

    @Override
    public boolean TryRegister(User u) {
        postJson(new Action<JSONObject>() {
            @Override
            public void run(JSONObject jsonObject) {
                if (jsonObject != null) {
                    Log.d("KiteLogin", jsonObject.toString());
                }
            }
        }, new HttpKeyValuePair[]{new HttpKeyValuePair("name", u.getUserName()),
                new HttpKeyValuePair("email", u.getUserEmail()),
                new HttpKeyValuePair("password", u.getUserPassword())}, "users");
        return true;
    }
    public void GetUsers(Action<User[]> callback){

    }
}
