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
        ((TextView)findViewById(R.id.profile_tvUsername)).setText(ApiManager.userSession.getUserEmail());
        ((TextView)findViewById(R.id.profile_tvFullName)).setText(ApiManager.userSession.getUserName());
    }
}