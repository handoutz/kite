package com.hatch.kite.api;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by vince on 9/14/13.
 */
@SuppressWarnings("serial")
public class TesterApplication implements Serializable {
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
