package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;


public class RoleWiseUsersResponse extends MainResponse {

    @SerializedName("data")
    UserCategory data;

    public UserCategory getData() {
        return data;
    }

    public void setData(UserCategory data) {
        this.data = data;
    }
}

