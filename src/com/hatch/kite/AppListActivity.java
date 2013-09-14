package com.hatch.kite;

import android.app.Activity;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public AppListActivity() {
        this.loadedViews = new HashMap<TesterApplication, View>();
    }

    //Buy It, Download It, Ignore It, Checkbox for allow developer to email user
    public View inflateListItem(TesterApplication app) {
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.app_list_item, null);

        ImageView ivIcon = (ImageView) v.findViewById(R.id.ali_appIcon);
        TextView tvName = (TextView) v.findViewById(R.id.ali_AppName);
        TextView tvDesc = (TextView) v.findViewById(R.id.ali_AppDesc);

        //ivIcon.setImageDrawable(app.getAppIcon() == null ? getResources().getDrawable(R.drawable.ic_launcher) : app.getAppIcon());
        tvName.setText(app.getAppName());
        tvDesc.setText(app.getAppDesc());

        loadedViews.put(app, v);

        return v;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        lvItems = (ListView) findViewById(R.id.applist_mainList);
        ApiManager.Instance.getJson(new ApiConnectionBase.Action<JSONObject>() {
            @Override
            public void run(JSONObject jsonObject) {
                try {
                    JSONArray array = jsonObject.getJSONArray("apps");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        TesterApplication app = new TesterApplication(o.getString("name"), o.getString("description"), null);
                        inflateListItem(app);
                    }
                    doSetup();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "apps");
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
                return loadedViews.values().toArray()[i];
            }

            @Override
            public long getItemId(int i) {
                return ((View) getItem(i)).getId();
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                return (View) getItem(i);
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
    }
}