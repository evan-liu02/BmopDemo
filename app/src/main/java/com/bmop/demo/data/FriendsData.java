package com.bmop.demo.data;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

public class FriendsData extends BmobObject {

    private UserData user;
    private BmobRelation friends;

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public BmobRelation getFriends() {
        return friends;
    }

    public void setFriends(BmobRelation friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "FriendsData{" +
                "user=" + user +
                ", friends=" + friends +
                '}';
    }
}
