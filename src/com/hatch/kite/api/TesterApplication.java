package com.hatch.kite.api;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

import static com.hatch.kite.api.TesterApplication.Screen.*;

/**
 * Created by vince on 9/14/13.
 */
@SuppressWarnings("serial")
public class TesterApplication implements Serializable {
    public HashMap<Integer, Screen> screens;
    public int id;
    private String appName;
    private String appDesc;
    private Drawable appIcon;

    /*{"app":{"id":1,"name":"Practical Rubber Shirt","description":" esse.","screens":[{"id":4,"name":"Messages","image_url":"http://lorempixel.com/320/480/technics",
    "hotspots":[{"id":1,"coordinates":"80,80,90,40","destination":2}]},{"id":3,"name":"Profile","image_url":"http://lorempixel.com/320/480/technics","hotspots":[{"id":2,"coordinates":"80,80,90,40","destination":4}]},{"id":2,"name":"Friends","image_url":"http://lorempixel.com/320/480/technics","hotspots":[{"id":3,"coordinates":"10,30,20,20","destination":3}]},{"id":1,"name":"Home","image_url":"http://lorempixel.com/320/480/technics","hotspots":[{"id":4,"coordinates":"80,80,90,40","destination":2}]}]}}*/
    public TesterApplication() {
        this.screens = new HashMap<Integer, Screen>();
    }

    public TesterApplication(String _name, String _desc, Drawable _icon) {
        this();
        this.appName = _name;
        this.appDesc = _desc;
        this.appIcon = _icon;
    }

    public TesterApplication(JSONObject o) {
        this();
        loadFromJson(o);
    }

    public void loadFromJson(JSONObject o) {
        try {
            JSONObject app = o.getJSONObject("app");
            this.appName = app.getString("name");
            this.appDesc = app.getString("description");
            JSONArray screens_wtf = app.getJSONArray("screens");
            for (int i = 0; i < screens_wtf.length() - 1; i++) {
                try {
                    JSONObject jsScreen = screens_wtf.optJSONObject(i);
                    if(jsScreen == null)
                        continue;
                    Screen screen = new Screen();
                    screen.id = jsScreen.getInt("id");
                    screen.imageUrl = jsScreen.getString("image_url");
                    screen.name = jsScreen.getString("name");
                    JSONArray hotspots = jsScreen.getJSONArray("hotspots");
                    for (int j = 0; j < hotspots.length(); j++) {
                        JSONObject jsHotspot = hotspots.getJSONObject(j);
                        Hotspot hotspot = new Hotspot();
                        hotspot.id = jsHotspot.getInt("id");
                        hotspot.destination = jsHotspot.getInt("destination");
                        String[] coords = jsHotspot.getString("coordinates").split(",");
                        int a = 0, b = 0, c = 0, d = 0;
                        try {
                            a = Integer.parseInt(coords[0]);
                            b = Integer.parseInt(coords[1]);
                            c = Integer.parseInt(coords[2]);
                            d = Integer.parseInt(coords[3]);
                        } catch (Exception e) {
                            Log.e("lol", "hmm", e);
                        }
                        hotspot.hotspotArea = new Rect(a, b, c, d);
                        screen.hotspots.put(hotspot.id, hotspot);
                    }
                    screens.put(screen.id, screen);
                } catch (JSONException ee) {
                    ee.printStackTrace();
                    Log.e("KiteImages", "error occured while loading this shit", ee);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public class Screen implements Serializable {
        public int id;
        public String name;
        public String imageUrl;
        public HashMap<Integer, Hotspot> hotspots;
        public Bitmap imageBitmap;

        public Screen() {
            this.hotspots = new HashMap<Integer, Hotspot>();
        }
    }

    public class Hotspot implements Serializable {
        public int id;
        public Rect hotspotArea;
        public int destination;
    }
}
