package com.hatch.kite;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by vince on 9/15/13.
 */
public class UserProfileActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        try {
            ((TextView) findViewById(R.id.profile_tvUsername)).setText(ApiManager.userSession.getUserEmail());
        } catch (Exception e) {
        }
        try {
            ((TextView) findViewById(R.id.profile_tvFullName)).setText(ApiManager.userSession.getUserName());
        } catch (Exception e) {
        }
    }
}