package com.bmop.demo.manager;

import android.util.Log;

import com.bmop.demo.data.UserData;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class UserManager {
    private static UserManager self;

    public static UserManager getInstance() {
        if (self == null) {
            synchronized (UserManager.class) {
                if (self == null) {
                    self = new UserManager();
                }
            }
        }
        return self;
    }

    private UserManager() {

    }

    public void register(String phone, String password) {
        UserData userData = new UserData();
        userData.setPhone(phone);
        userData.setPassword(password);
        userData.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                Log.e("Test", objectId);
                if (e == null) {

                } else {

                }
            }
        });
    }
}
