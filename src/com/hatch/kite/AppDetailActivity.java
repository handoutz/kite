package com.hatch.kite;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hatch.kite.api.ApiConnectionBase;
import com.hatch.kite.api.TesterApplication;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

/**
 * Created by vince on 9/14/13.
 */
public class AppDetailActivity extends Activity {
    ProgressDialog progressDialog;
    TesterApplication ourApplication;
    WebView appIcon;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applist_detail);
        Intent i = getIntent();

        final TesterApplication app = (TesterApplication) i.getSerializableExtra("app");
        ourApplication = app;

        progressDialog = new ProgressDialog(this, android.R.style.Theme_Holo_Light_DarkActionBar);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading apps..");
        progressDialog.show();

        appIcon = ((WebView)findViewById(R.id.wvAppThumb));

        ApiManager.Instance.getJson(new ApiConnectionBase.Action<JSONObject>() {
            @Override
            public void run(JSONObject jsonObject) {
                app.loadFromJson(jsonObject);
                try{
                    appIcon.getSettings().setJavaScriptEnabled(true);
                    String imgUrl = ((TesterApplication.Screen) app.screens.values().toArray()[0]).imageUrl;
                    appIcon.setWebChromeClient(new WebChromeClient());

                    AssetManager mgr = getAssets();
                    Scanner scan = new Scanner(mgr.open("webViewv2.html"));
                    StringBuilder scanBuild = new StringBuilder();
                    while(scan.hasNextLine()) {
                        String line = scan.nextLine();
                        line = line.replace("{{IMAGESRC}}", imgUrl);
                        line = line.replace("{{IMAGEWIDTH}}", "256");
                        line = line.replace("{{IMAGEHEIGHT}}", "256");
                        line = line.replace("{{RENDERHOTSPOTS}}", "true");
                        line = line.replace("{{HOTSPOTDATA}}", "{}");
                        scanBuild.append(line);
                    }
                    appIcon.loadData(scanBuild.toString(), "text/html", "utf-8");
                    /*<img src="{{IMAGESRC}}" style="width: {{IMAGEWIDTH}}px; height: {{IMAGEHEIGHT}}px;"/>
                        <script>
                            window.onload = function() {
                                if({{RENDERHOTSPOTS}}) {
                                    var hotspotData = {{HOTSPOTDATA}};
                                }
                            };*/

                    //appIcon.loadData("<html><head></head><body><img src=\""+imgUrl+"\" width=\"160\" height=\"240\" /></body></html>", "text/html", "utf-8");
                    //appIcon.loadUrl("file:///android_asset/webViewv2.html#" + URLEncoder.encode();
                }catch(Exception e){
                    Log.d("wtf", "fuck you, json library");
                } finally {
                    progressDialog.hide();
                    progressDialog.dismiss();
                }
                /*AsyncTask<TesterApplication, Void, Void> task = new AsyncTask<TesterApplication, Void, Void>() {
                    @Override
                    @Deprecated
                    protected Void doInBackground(TesterApplication... testerApplications) {
                        TesterApplication tapp = testerApplications[0];
                        for (TesterApplication.Screen screen : tapp.screens.values()) {
                            /*ImageView iv = new ImageView(context);

                                try{
                                    String url1 = "http://<my IP>/test/abc.jpg";
                                    URL ulrn = new URL(url1);
                                    HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
                                    InputStream is = con.getInputStream();
                                    Bitmap bmp = BitmapFactory.decodeStream(is);
                                    if (null != bmp)
                                        iv.setImageBitmap(bmp);
                                    else
                                        System.out.println("The Bitmap is NULL");

                                } catch(Exception e) {
                                }

                            try {
                                URL u = new URL(screen.imageUrl);
                                HttpURLConnection con = (HttpURLConnection) u.openConnection();
                                InputStream is = con.getInputStream();
                                Bitmap bmp = BitmapFactory.decodeStream(is);
                                if (bmp != null)
                                    screen.imageBitmap = bmp;
                                con.disconnect();
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        if (app.screens.values().toArray().length > 0) {
                            Bitmap ourBitmap = ((TesterApplication.Screen) app.screens.values().toArray()[0]).imageBitmap;
                            if (ourBitmap != null)
                                ((ImageView) findViewById(R.id.ald_ivAppThumb)).setImageBitmap(ourBitmap);
                        }

                        progressDialog.hide();
                        progressDialog.dismiss();
                    }
                };
                task.execute(app);*/
            }
        }, "apps", Integer.toString(app.id));

        ((TextView) findViewById(R.id.ald_appName)).setText(app.getAppName());
        ((TextView) findViewById(R.id.ald_appDesc)).setText(app.getAppDesc());

        ((Button) findViewById(R.id.ald_btnRate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog d = new Dialog(AppDetailActivity.this, android.R.style.Theme_Holo_Light_DarkActionBar);
                d.setContentView(R.layout.dialog_rateme);
                d.setCancelable(true);
                d.show();
            }
        });
    }
}