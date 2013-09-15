package com.hatch.kite;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.hatch.kite.api.TesterApplication;

import junit.framework.Test;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by vince on 9/15/13.
 */
public class AppMockupActivity extends Activity {
    WebView wvMockup;
    String jsonTaApp;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_mockup);
        wvMockup = (WebView) findViewById(R.id.mockup_wvMain);
        /*appIcon.getSettings().setJavaScriptEnabled(true);
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
                    appIcon.loadData(scanBuild.toString(), "text/html", "utf-8");*/
        jsonTaApp = getIntent().getStringExtra("app");
        wvMockup.getSettings().setJavaScriptEnabled(true);
        wvMockup.setWebChromeClient(new WebChromeClient());

        try {
            AssetManager mgr = getAssets();
            Scanner scan = new Scanner(mgr.open("appMockup.html"));
            StringBuilder scanBuild = new StringBuilder();

            while(scan.hasNextLine()) {
                String line = scan.nextLine();
                line = line.replace("{{IMAGESRC}}", "");
                line = line.replace("{{IMAGEWIDTH}}", "256");
                line = line.replace("{{IMAGEHEIGHT}}", "256");
                line = line.replace("{{RENDERHOTSPOTS}}", "true");
                line = line.replace("{{APPDATA}}", jsonTaApp);
                scanBuild.append(line);
            }
            String built = scanBuild.toString();
            Log.i("jsonshit", jsonTaApp);
            wvMockup.loadDataWithBaseURL("file:///android_asset/", built, "text/html", "utf-8", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}