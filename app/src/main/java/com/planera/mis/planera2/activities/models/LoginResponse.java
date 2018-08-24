package com.planera.mis.planera2.activities.models;


import com.google.gson.annotations.SerializedName;


public class LoginResponse extends MainResponse {
  @SerializedName("data")
    private UserData data;

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }
}
