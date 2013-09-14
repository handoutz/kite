package com.hatch.kite.api;

import android.graphics.drawable.Drawable;

/**
 * Created by vince on 9/14/13.
 */
public class TesterApplication {
    private String appName;
    private String appDesc;
    private Drawable appIcon;

    public TesterApplication(String _name, String _desc, Drawable _icon) {
        this.appName = _name;
        this.appDesc = _desc;
        this.appIcon = _icon;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public String getAppName() {
        return appName;
    }

}
