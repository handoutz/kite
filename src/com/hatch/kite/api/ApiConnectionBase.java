package com.hatch.kite.api;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by vince on 9/14/13.
 */
public abstract class ApiConnectionBase {
    public static String baseUrl = "http://kite-api.herokuapp.com/";
    //public static String baseUrl = "http://localhost/";

    public void postJsonApache(final ActionWithError<JSONObject> cb, final NameValuePair[] kvps, String... parts) {
        StringBuilder bld = new StringBuilder(baseUrl);
        for (String part : parts) {
            bld.append(part);
            bld.append("/");
        }
        try {
            AsyncTask<String, Void, JSONObject> task = new AsyncTask<String, Void, JSONObject>() {
                @Override
                protected JSONObject doInBackground(String... objects) {
                    try {
                        HttpPost post = new HttpPost(objects[0]);

                        ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
                        Collections.addAll(nvps, kvps);
                        UrlEncodedFormEntity postForm = new UrlEncodedFormEntity(nvps, "UTF-8");
                        post.setEntity(postForm);

                        CloseableHttpClient client = HttpClients.createDefault();
                        CloseableHttpResponse response = client.execute(post);

                        HttpEntity ent = response.getEntity();
                        JSONObject result = null;
                        if (ent != null) {
                            result = new JSONObject(EntityUtils.toString(ent));
                        }

                        if (response != null)
                            response.close();
                        return result;
                    } catch (ClientProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(JSONObject jsonObject) {
                    if (jsonObject == null)
                        cb.error(new NullPointerException("jsonObject was null on post"));
                    else
                        cb.run(jsonObject);
                }
            };
            task.execute(bld.toString());
        } catch (Exception e) {
            cb.error(e);
        }
    }

    public void getJsonApache(final ActionWithError<JSONObject> cb, String... parts) {
        StringBuilder bld = new StringBuilder(baseUrl);
        for (String part : parts) {
            bld.append(part);
            bld.append("/");
        }
        try {
            AsyncTask<String, Void, JSONObject> task = new AsyncTask<String, Void, JSONObject>() {
                @Override
                protected JSONObject doInBackground(String... strings) {
                    HttpGet get = new HttpGet(strings[0]);
                    CloseableHttpClient client = HttpClients.createDefault();
                    CloseableHttpResponse response = null;
                    try {
                        response = client.execute(get);

                        JSONObject result = null;
                        HttpEntity ent = response.getEntity();
                        if (ent != null) {
                            String resp = EntityUtils.toString(ent);
                            result = new JSONObject(resp);
                        }

                        if (response != null)
                            response.close();

                        return result;
                    } catch (IOException e) {
                        return null;
                    } catch (JSONException e) {
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(JSONObject jsonObject) {
                    if (jsonObject == null) {
                        cb.error(new NullPointerException("jsonObject was null!"));
                    } else {
                        cb.run(jsonObject);
                    }
                }
            };
            task.execute(bld.toString());
        } catch (Exception e) {
            cb.error(e);
        }
    }

    @Deprecated
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

    @Deprecated
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

    public interface ActionWithError<T> {
        void run(T t);

        void error(Exception e);
    }
}
