package com.hatch.kite;

import android.app.Activity;
import android.content.Intent;
import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hatch.kite.api.User;

/**
 * Created by vince on 9/14/13.
 */
public class RegisterActivity extends Activity {
    EditText txtUsername;
    EditText txtEmail;
    EditText txtPassword;
    EditText txtBirthday;
    RadioGroup rgGender;
    EditText txtZip;
    Button btnRegister;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtEmail = (EditText)findViewById(R.id.register_txtEmail);
        txtPassword = (EditText)findViewById(R.id.register_txtPassword);
        txtBirthday = (EditText)findViewById(R.id.register_txtBirthday);
        txtZip = (EditText)findViewById(R.id.register_location);
        txtUsername = (EditText) findViewById(R.id.register_txtUsername);
        rgGender = (RadioGroup)findViewById(R.id.register_rgGender);
        btnRegister = (Button)findViewById(R.id.register_btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User u = new User();
                u.setUserName(txtUsername.getText().toString());
                try {
                    u.setUserAge(Integer.parseInt(txtBirthday.getText().toString()));
                } catch (Exception e) {
                    u.setUserAge(-1);
                }
                u.setUserEmail(txtEmail.getText().toString());
                u.setUserLocation(txtZip.getText().toString());
                u.setUserPassword(txtPassword.getText().toString());

                RadioButton rbChecked = (RadioButton) findViewById(rgGender.getCheckedRadioButtonId());
                if (rbChecked.getId() == R.id.register_rbMale)
                    u.setUserGender(User.Gender.Male);
                else
                    u.setUserGender(User.Gender.Female);

                if (ApiManager.Instance.TryRegister(u)) {
                    startActivity(new Intent(RegisterActivity.this, AppListActivity.class));
                } else {
                    Toast.makeText(RegisterActivity.this, "Could not register", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}