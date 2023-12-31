package com.example.code05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etPwd;
    private EditText etAccount;
    private CheckBox cbRememberPwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPwd = findViewById(R.id.et_pwd);
        etAccount = findViewById(R.id.et_account);
        cbRememberPwd = findViewById(R.id.cb_remember_pwd);

        Button btLogin = findViewById(R.id.bt_login);
        btLogin.setOnClickListener(this);

        String spFileName = getResources()
                .getString(R.string.shared_preferences_file_name);
        String accountKey = getResources()
                .getString(R.string.login_account_name);
        String passwordKey = getResources()
                .getString(R.string.login_password);
        String rememberPasswordKey = getResources()
                .getString(R.string.login_remember_password);

        SharedPreferences spFile = getSharedPreferences(spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spFile.edit();

//        etAccount.setText(spFile.getString(accountKey, ""));
//        etPwd.setText(spFile.getString(passwordKey, ""));
//        cbRememberPwd.setChecked(spFile.getBoolean(rememberPasswordKey, false));

        String account = spFile.getString(accountKey,null);
        String password = spFile.getString(passwordKey,null);
        Boolean rememberPassword = spFile.getBoolean(rememberPasswordKey, false);
        if (account != null && !TextUtils.isEmpty(account)) {
            etAccount.setText(account);
        }
        if (password != null && !TextUtils.isEmpty (password)) {
            etPwd.setText(password);
        }
        cbRememberPwd.setChecked(rememberPassword);

    }

    @Override
    public void onClick(View view) {
        String spFileName = getResources()
                .getString(R.string.shared_preferences_file_name);
        String accountKey = getResources()
                .getString(R.string.login_account_name);
        String passwordKey = getResources()
                .getString(R.string.login_password);
        String rememberPasswordKey = getResources()
                .getString(R.string.login_remember_password);

        SharedPreferences spFile = getSharedPreferences(
                spFileName,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = spFile.edit();

        if (cbRememberPwd.isChecked()) {
            String account = etAccount.getText().toString();
            String password = etPwd.getText().toString();

            editor.putString(accountKey, account);
            editor.putString(passwordKey, password);
            editor.putBoolean(rememberPasswordKey, true);
            editor.apply();
        }else{
            editor.remove(accountKey);
            editor.remove(passwordKey);
            editor.remove(rememberPasswordKey);
            editor.apply();
        }
        Toast.makeText(this,  "Hello! " + etAccount.getText(), Toast.LENGTH_SHORT).show();
    }
}