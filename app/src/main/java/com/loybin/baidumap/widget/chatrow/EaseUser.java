package com.loybin.baidumap.widget.chatrow;

import java.io.Serializable;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/06/09 下午3:36
 * 描   述: 添加设备view
 */
public class EaseUser implements Serializable {
    protected String mUsername;
    protected String mNick;
    private String mAvatar;

    public String getAvatar() {
        return mAvatar;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public String getNick() {
        return mNick;
    }

    public void setNick(String nick) {
        this.mNick = nick;
    }


    @Override
    public String toString() {
        return "EaseUser{" +
                "mUsername='" + mUsername + '\'' +
                ", mNick='" + mNick + '\'' +
                '}';
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }
}
