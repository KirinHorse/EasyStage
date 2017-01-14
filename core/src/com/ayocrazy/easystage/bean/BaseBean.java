package com.ayocrazy.easystage.bean;

import java.io.Serializable;

/**
 * Created by 26286 on 2017/1/14.
 */

public abstract class BaseBean implements Serializable {
    private int id;
    private UserBean user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}
