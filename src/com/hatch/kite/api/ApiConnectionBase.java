package com.hatch.kite.api;

import android.os.AsyncTask;

import org.apache.http.HttpRequest;
import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Created by vince on 9/14/13.
 */
public abstract class ApiConnectionBase {
    public static String baseUrl = "http://kite-api.herokuapp.com/";

    public void getJson(final Action<JSONObject> callback, String... parts) {
        //URI uri = URI.create(baseUrl + parts.)
        StringBuilder bld = new StringBuilder(baseUrl);
        for (String part : parts) {
            bld.append(part + "/");
        }
        try {
            AsyncTask<String, Void, JSONObject> tsk = new AsyncTask<String, Void, JSONObject>() {
                @Override
                protected JSONObject doInBackground(String... strings) {
                    URL url = null;
                    Scanner scan = null;
                    try {
                        url = new URL(strings[0]);
                        InputStream stream = url.openStream();
                        scan = new Scanner(stream);
                        StringBuilder blder = new StringBuilder();
                        while(scan.hasNextLine()){
                            blder.append(scan.nextLine());
                        }
                        return new JSONObject(blder.toString());
                    } catch (Exception e) {
                        return null;
                    } finally {
                        if(scan != null)
                            scan.close();
                    }
                }

                @Override
                protected void onPostExecute(JSONObject jsonObject) {
                    callback.run(jsonObject);
                }
            };
            tsk.execute(bld.toString());
        } catch (Exception e) {

        }
    }

    public abstract boolean TryLogin(User u);

    public abstract boolean TryRegister(User u);

    public interface Action<T> {
        void run(T t);
    }
}
