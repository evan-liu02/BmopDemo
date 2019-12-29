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
import com.bmop.demo.manager.UserManager.OnRegisterListener;
import com.bmop.demo.utils.ToastUtil;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private TextInputLayout mUsernameInputLayout;
    private TextInputLayout mPasswordInputLayout;
    private TextInputLayout mCodeInputLayout;
    private EditText mUsername;
    private EditText mPassword;
    private EditText mCode;
    private Button registerBtn;
    private boolean registerClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
    }

    private void initViews() {
        registerBtn = findViewById(R.id.register_btn);
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
                mUsernameInputLayout.setError(getString(R.string.tip_input_phone));
            } else if (TextUtils.isEmpty(code)) {
                mCodeInputLayout.setError(getString(R.string.tip_input_code));
            } else if (TextUtils.isEmpty(password)) {
                mPasswordInputLayout.setError(getString(R.string.tip_input_password));
            } else {
                // TODO 加入验证码
                String reg = "^1[34578][0-9]\\d{8}$";
                if (name.matches(reg)) {
                    String passwordReg = "\\w+";
                    if (password.matches(passwordReg)) {
                        if (registerClicked) {
                            return;
                        }
                        registerBtn.setText(R.string.register_now);
                        registerClicked = true;
                        UserManager.getInstance().register(name, password, new OnRegisterListener() {
                            @Override
                            public void onRegisterSuccess() {
                                intent2Activity(LoginActivity.class);
                                finish();
                            }

                            @Override
                            public void onRegisterFailed(int errorCode) {
                                if (errorCode == OnRegisterListener.ERROR_SERVER) {
                                    ToastUtil.showToast(RegisterActivity.this, getString(R.string.register_failed));
                                } else if (errorCode == OnRegisterListener.ERROR_HAS_USED) {
                                    ToastUtil.showToast(RegisterActivity.this, getString(R.string.phone_used));
                                }
                                registerBtn.setText(R.string.register);
                                registerClicked = false;
                            }
                        });
                    } else {
                        ToastUtil.showToast(RegisterActivity.this, getString(R.string.password_illegal));
                    }
                } else {
                    ToastUtil.showToast(RegisterActivity.this, getString(R.string.wrong_phone));
                }
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
