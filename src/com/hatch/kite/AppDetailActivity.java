package com.hatch.kite;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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

/**
 * Created by vince on 9/14/13.
 */
public class AppDetailActivity extends Activity {
    ProgressDialog progressDialog;
    TesterApplication ourApplication;

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

        ApiManager.Instance.getJson(new ApiConnectionBase.Action<JSONObject>() {
            @Override
            public void run(JSONObject jsonObject) {
                app.loadFromJson(jsonObject);
                AsyncTask<TesterApplication, Void, Void> task = new AsyncTask<TesterApplication, Void, Void>() {
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
                                */
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
                task.execute(app);
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