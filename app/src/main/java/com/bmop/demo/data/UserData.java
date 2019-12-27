package com.bmop.demo.data;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

public class UserData extends BmobObject {
    private String phone;
    private String password;

    private BmobRelation speeches; // 发布的说说数据

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public BmobRelation getSpeeches() {
        return speeches;
    }

    public void setSpeeches(BmobRelation speeches) {
        this.speeches = speeches;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", speeches=" + speeches +
                '}';
    }
}
