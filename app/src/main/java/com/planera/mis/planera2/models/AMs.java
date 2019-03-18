package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;

public class AMs {
    @SerializedName("FirstName")
    private String firstName;

    @SerializedName("userId")
    private String userId;

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserId(){
        return userId;
    }

    @Override
    public String toString(){
        return
                "AMs{" +
                        "firstName = '" + firstName + '\'' +
                        ",userId = '" + userId + '\'' +
                        "}";
    }
}

