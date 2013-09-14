package com.hatch.kite;

import com.hatch.kite.api.ApiConnectionBase;
import com.hatch.kite.api.User;

/**
 * Created by vince on 9/14/13.
 */
public class ApiManager extends ApiConnectionBase {
    public static ApiManager Instance = new ApiManager();

    @Override
    public boolean TryLogin(User u) {
        return true;
    }

    @Override
    public boolean TryRegister(User u) {
        return true;
    }
}
