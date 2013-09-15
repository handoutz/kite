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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hatch.kite.api.ApiConnectionBase;
import com.hatch.kite.api.HttpKeyValuePair;
import com.hatch.kite.api.TesterApplication;
import com.hatch.kite.api.UserFeedback;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import flexjson.JSONSerializer;

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
                }catch(Exception e){
                    Log.d("wtf", "fuck you, json library");
                } finally {
                    progressDialog.hide();
                    progressDialog.dismiss();
                }
            }
        }, "apps", Integer.toString(app.id));

        ((TextView) findViewById(R.id.ald_appName)).setText(app.getAppName());
        ((TextView) findViewById(R.id.ald_appDesc)).setText(app.getAppDesc());

        ((Button) findViewById(R.id.ald_btnRate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog d = new Dialog(AppDetailActivity.this, android.R.style.Theme_Holo_Light_DarkActionBar);
                d.setContentView(R.layout.dialog_rateme);
                d.setCancelable(true);

                d.findViewById(R.id.rating_btnRate).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RadioButton checked = ((RadioButton) d.findViewById(((RadioGroup) d.findViewById(R.id.rating_rbRatings)).getCheckedRadioButtonId()));
                        CheckBox cb = (CheckBox) d.findViewById(R.id.rating_cbEmail);
                        UserFeedback.Level level = UserFeedback.Level.Scry;
                        switch (checked.getId()) {
                            case R.id.rating_rbBuyIt:
                                level = UserFeedback.Level.Buy;
                                break;
                            case R.id.rating_rbDownloadIt:
                                level = UserFeedback.Level.Try;
                                break;
                            case R.id.rating_rbIgnoreIt:
                                level = UserFeedback.Level.Scry;
                                break;
                            default:
                                break;
                        }
                        ApiManager.Instance.postJson(new ApiConnectionBase.Action<JSONObject>() {
                            @Override
                            public void run(JSONObject jsonObject) {
                                Log.i("Rating", jsonObject == null ? "fuck it was null" : jsonObject.toString());
                                d.dismiss();
                                d.hide();
                            }
                        }, new HttpKeyValuePair[]{new HttpKeyValuePair("share_email", Boolean.toString(cb.isChecked())),
                                new HttpKeyValuePair("option_id", level.toString())}, "apps", Integer.toString(app.id), "feedbacks");
                    }
                });
                d.show();
            }
        });
        findViewById(R.id.ald_btnMockup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppDetailActivity.this, AppMockupActivity.class);
                String appSerial = "";
                JSONSerializer serial = new JSONSerializer();
                appSerial = serial.deepSerialize(app);
                intent.putExtra("app", appSerial);
                startActivity(intent);
            }
        });
    }
}