package com.hatch.kite;

import android.app.Activity;
import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

/**
 * Created by vince on 9/14/13.
 */
public class RegisterActivity extends Activity {
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
        rgGender = (RadioGroup)findViewById(R.id.register_rgGender);
        btnRegister = (Button)findViewById(R.id.register_btnRegister);
    }
}