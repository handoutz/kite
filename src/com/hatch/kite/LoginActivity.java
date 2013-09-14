package com.hatch.kite;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hatch.kite.api.User;

import static android.view.View.OnClickListener;

/**
 * Created by vince on 9/14/13.
 */
public class LoginActivity extends Activity {
    EditText txtEmail;
    EditText txtPassword;
    Button btnLogin;
    Button btnRegister;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = (EditText) findViewById(R.id.login_txtEmail);
        txtPassword = (EditText) findViewById(R.id.login_txtPassword);
        btnLogin = (Button) findViewById(R.id.login_btnSignIn);
        btnRegister = (Button) findViewById(R.id.login_btnSignUp);

        btnLogin.setOnClickListener(new ButtonListener());
        btnRegister.setOnClickListener(new ButtonListener());
    }

    private class ButtonListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            if (view == btnLogin) {
                String email = txtEmail.getText().toString(), pass = txtPassword.getText().toString();
                //Log.d("Login", "Login attempt: " + email + ":" + pass);
                if (ApiManager.Instance.TryLogin(new User(email, pass))) {
                    Log.d("Login", "Login was successful!");
                    Intent i = new Intent(LoginActivity.this, AppListActivity.class);
                    startActivity(i);
                } else {
                    Log.d("Login", "Login unsuccessful.");
                }
            }
        }
    }
}