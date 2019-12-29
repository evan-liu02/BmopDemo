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
import com.bmop.demo.manager.UserManager.OnLoginListener;
import com.bmop.demo.utils.Logger;
import com.bmop.demo.utils.ToastUtil;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private TextInputLayout mUsernameInputLayout;
    private TextInputLayout mPasswordInputLayout;
    private EditText mUsername;
    private EditText mPassword;
    private Button loginBtn;
    private boolean loginClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews() {
        loginBtn = findViewById(R.id.login_btn);
        TextView resetPassword = findViewById(R.id.reset_password);
        TextView register = findViewById(R.id.register);
        mUsernameInputLayout = findViewById(R.id.til_username);
        mPasswordInputLayout = findViewById(R.id.til_password);
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
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
                mUsernameInputLayout.setError(getString(R.string.tip_input_phone));
            } else if (TextUtils.isEmpty(password)) {
                mPasswordInputLayout.setError(getString(R.string.tip_input_password));
            } else {
                String reg = "^1[34578][0-9]\\d{8}$"; // 简单验证手机号
                if (name.matches(reg)) {
                    if (loginClicked) {
                        return;
                    }
                    loginBtn.setText(R.string.logging);
                    loginClicked = true;
                    userManager.login(name, password, new OnLoginListener() {
                        @Override
                        public void onLoginSuccess() {
                            intent2Activity(MainActivity.class);
                            finish();
                        }

                        @Override
                        public void onLoginFailed(int errorCode) {
                            if (errorCode == OnLoginListener.ERROR_SERVER) {
                                ToastUtil.showToast(LoginActivity.this, getString(R.string.login_failed));
                            } else if (errorCode == OnLoginListener.ERROR_NO_USER) {
                                ToastUtil.showToast(LoginActivity.this, getString(R.string.no_user));
                            } else if (errorCode == OnLoginListener.ERROR_WRONG_PASSWORD) {
                                ToastUtil.showToast(LoginActivity.this, getString(R.string.wrong_password));
                            }
                            loginBtn.setText(R.string.login);
                            loginClicked = false;
                        }
                    });
                } else {
                    ToastUtil.showToast(LoginActivity.this, getString(R.string.wrong_phone));
                }
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
