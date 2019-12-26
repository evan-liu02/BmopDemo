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
import com.bmop.demo.manager.UserManager;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private TextInputLayout mUsernameInputLayout;
    private TextInputLayout mPasswordInputLayout;
    private TextInputLayout mCodeInputLayout;
    private EditText mUsername;
    private EditText mPassword;
    private EditText mCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
    }

    private void initViews() {
        Button registerBtn = findViewById(R.id.register_btn);
        TextView login = findViewById(R.id.login);
        mUsernameInputLayout = findViewById(R.id.til_phone);
        mCodeInputLayout = findViewById(R.id.til_code);
        mPasswordInputLayout = findViewById(R.id.til_password);
        mUsername = findViewById(R.id.phone);
        mCode = findViewById(R.id.code);
        mPassword = findViewById(R.id.password);
        registerBtn.setOnClickListener(this);
        login.setOnClickListener(this);
        mUsername.addTextChangedListener(this);
        mCode.addTextChangedListener(this);
        mPassword.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        if (R.id.register_btn == v.getId()) {
            String name = mUsername.getText().toString();
            String code = mCode.getText().toString();
            String password = mPassword.getText().toString();
            if (TextUtils.isEmpty(name)) {
                mUsernameInputLayout.setError(getString(R.string.tip_input_username));
            } else if (TextUtils.isEmpty(code)) {
                mCodeInputLayout.setError(getString(R.string.tip_input_code));
            } else if (TextUtils.isEmpty(password)) {
                mPasswordInputLayout.setError(getString(R.string.tip_input_password));
            } else {
                UserManager.getInstance().register("13852261412", "123456");
            }
        } else if (R.id.login == v.getId()) {
            intent2Activity(LoginActivity.class);
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
        if (mCodeInputLayout.isErrorEnabled()) {
            mCodeInputLayout.setErrorEnabled(false);
        }
        if (mPasswordInputLayout.isErrorEnabled()) {
            mPasswordInputLayout.setErrorEnabled(false);
        }
    }
}
