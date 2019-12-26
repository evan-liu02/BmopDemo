package com.bmop.demo.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bmop.demo.R;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private TextInputLayout mUsernameInputLayout;
    private TextInputLayout mPasswordInputLayout;
    private EditText mUsername;
    private EditText mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews() {
        Button loginBtn = findViewById(R.id.login_btn);
        TextView resetPassword = findViewById(R.id.reset_password);
        TextView register =  findViewById(R.id.register);
        mUsernameInputLayout =  findViewById(R.id.til_username);
        mPasswordInputLayout = findViewById(R.id.til_password);
        mUsername =  findViewById(R.id.username);
        mPassword =  findViewById(R.id.password);
        loginBtn.setOnClickListener(this);
        resetPassword.setOnClickListener(this);
        register.setOnClickListener(this);
        mUsername.addTextChangedListener(this);
        mPassword.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        if (R.id.login_btn == v.getId()) {
            String name = mUsername.getText().toString();
            String password = mPassword.getText().toString();
            if (TextUtils.isEmpty(name)) {
                mUsernameInputLayout.setError(getString(R.string.tip_input_username));
            } else if (TextUtils.isEmpty(password)) {
                mPasswordInputLayout.setError(getString(R.string.tip_input_password));
            } else {
            }
        } else if (R.id.reset_password == v.getId()) {
//            intent2Activity(ResetPasswordActivity.class);
        } else if (R.id.register == v.getId()) {
            intent2Activity(RegisterActivity.class);
            finish();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mUsernameInputLayout.isErrorEnabled()) {
            mUsernameInputLayout.setErrorEnabled(false);
        }
        if (mPasswordInputLayout.isErrorEnabled()) {
            mPasswordInputLayout.setErrorEnabled(false);
        }
    }
}
