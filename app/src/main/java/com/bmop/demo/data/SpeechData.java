package com.bmop.demo.data;

import cn.bmob.v3.BmobObject;

public class SpeechData extends BmobObject {
    private String content;
    private UserData author;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserData getAuthor() {
        return author;
    }

    public void setAuthor(UserData author) {
        this.author = author;
    }
}
