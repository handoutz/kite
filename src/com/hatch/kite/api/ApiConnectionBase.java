package com.hatch.kite.api;

/**
 * Created by vince on 9/14/13.
 */
public abstract class ApiConnectionBase {
    public abstract boolean TryLogin(User u);
    public abstract boolean TryRegister(User u);
}
