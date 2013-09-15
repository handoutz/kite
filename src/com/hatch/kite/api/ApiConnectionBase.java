package com.hatch.kite.api;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpRequest;
import org.json.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by vince on 9/14/13.
 */
public abstract class ApiConnectionBase {
    public static String baseUrl = "http://kite-api.herokuapp.com/";
    //public static String baseUrl = "http://localhost/";

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
                        while (scan.hasNextLine()) {
                            blder.append(scan.nextLine());
                        }
                        return new JSONObject(blder.toString());
                    } catch (Exception e) {
                        return null;
                    } finally {
                        if (scan != null)
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

    public void postJson(final Action<JSONObject> callback, final HttpKeyValuePair[] params, String... parts) {
        try {
            StringBuilder bld = new StringBuilder(baseUrl);
            for (String part : parts) {
                bld.append(part + "/");
            }
            AsyncTask<Object, Void, JSONObject> task = new AsyncTask<Object, Void, JSONObject>() {
                @Override
                protected JSONObject doInBackground(Object... objects) {
                    HttpURLConnection con = null;
                    DataOutputStream out = null;
                    InputStream in = null;
                    JSONObject result = null;
                    try {
                        URL url = new URL(objects[0].toString());

                        StringBuilder urlParamBuilder = new StringBuilder();
                        for (HttpKeyValuePair kvp : params) {
                            urlParamBuilder.append(kvp.getKey());
                            urlParamBuilder.append("=");
                            urlParamBuilder.append(kvp.getValue());
                            urlParamBuilder.append("&");
                        }
                        urlParamBuilder.deleteCharAt(urlParamBuilder.length() - 1);
                        String urlParam = urlParamBuilder.toString();

                        con = (HttpURLConnection) url.openConnection();
                        con.setDoInput(true);
                        con.setDoOutput(true);
                        con.setRequestMethod("POST");
                        con.setUseCaches(false);
                        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        con.setRequestProperty("charset", "utf-8");
                        con.setRequestProperty("Content-Length", Integer.toString(urlParam.getBytes().length));

                        out = new DataOutputStream(con.getOutputStream());
                        out.writeBytes(urlParam);
                        out.flush();
                        out.close();

                        in = con.getInputStream();
                        Scanner scan = new Scanner(in);
                        StringBuilder resultBuilder = new StringBuilder();
                        while (scan.hasNextLine()) {
                            resultBuilder.append(scan.nextLine());
                        }
                        String resultStr = resultBuilder.toString();
                        Log.d("kite", resultStr);
                        result = new JSONObject(resultBuilder.toString());

                        scan.close();
                        con.disconnect();
                        return result;
                    } catch (Exception e) {
                        return null;
                    } finally {

                    }

                }

                @Override
                protected void onPostExecute(JSONObject jsonObject) {
                    callback.run(jsonObject);
                }
            };
            task.execute(bld.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract boolean TryLogin(User u);

    public abstract boolean TryRegister(User u);

    public interface Action<T> {
        void run(T t);
    }
}
