package com.hatch.kite;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.widget.*;

import com.hatch.kite.api.ApiConnectionBase;
import com.hatch.kite.api.TesterApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by vince on 9/14/13.
 */
public class AppListActivity extends Activity {
    private HashMap<TesterApplication, View> loadedViews;
    private ListAdapter adapt;
    private ListView lvItems;
    private ProgressDialog progressDialog;

    public AppListActivity() {
        this.loadedViews = new HashMap<TesterApplication, View>();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_app_list);

        if(ApiManager.userSession == null){
            Log.d("KiteLogin", "login failed. fail");
        }

        lvItems = (ListView) findViewById(R.id.applist_mainList);

        progressDialog = new ProgressDialog(this, android.R.style.Theme_Holo_Light_DarkActionBar);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading apps..");
        progressDialog.show();
        

        ApiManager.Instance.getJson(new ApiConnectionBase.Action<JSONObject>() {
            @Override
            public void run(JSONObject jsonObject) {
                try {
                    if(jsonObject == null) {
                        Log.d("fuck", "json object was null in applistactivity. fuck.");
                        return;
                    }
                    JSONArray array = jsonObject.getJSONArray("apps");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        TesterApplication app = new TesterApplication(o.getString("name"), o.getString("description"), null);
                        app.id = o.getInt("id");
                        inflateListItem(app);
                    }
                    doSetup();
                    progressDialog.dismiss();
                    progressDialog.hide();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "apps");

        ActionBar ab = getActionBar();
        ab.setTitle("Kite Marketplace");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        menu.findItem(R.id.action_profile).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                startActivity(new Intent(AppListActivity.this, UserProfileActivity.class));
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    //Buy It, Download It, Ignore It, Checkbox for allow developer to email user
    public View inflateListItem(TesterApplication app) {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.app_list_item, null);

        TextView tvName = (TextView) v.findViewById(R.id.ali_AppName);
        TextView tvDesc = (TextView) v.findViewById(R.id.ali_AppDesc);

        //ivIcon.setImageDrawable(app.getAppIcon() == null ? getResources().getDrawable(R.drawable.ic_launcher) : app.getAppIcon());
        tvName.setText(app.getAppName());
        tvDesc.setText(app.getAppDesc());

        loadedViews.put(app, v);

        return v;
    }

    public void doSetup(){
        adapt = new ListAdapter() {
            @Override
            public boolean areAllItemsEnabled() {
                return false;
            }

            @Override
            public boolean isEnabled(int i) {
                return true;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

            }

            @Override
            public int getCount() {
                return loadedViews.size();
            }

            @Override
            public Object getItem(int i) {
                return loadedViews.keySet().toArray()[i];
            }

            @Override
            public long getItemId(int i) {
                return ((View) loadedViews.values().toArray()[i]).getId();
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                return ((View) loadedViews.values().toArray()[i]);
            }

            @Override
            public int getItemViewType(int i) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        };
        lvItems.setAdapter(adapt);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TesterApplication app = (TesterApplication) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(AppListActivity.this, AppDetailActivity.class);
                intent.putExtra("app", app);
                intent.putExtra("id", app.id);
                startActivity(intent);
            }
        });
    }
}