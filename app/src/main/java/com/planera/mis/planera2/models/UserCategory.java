package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserCategory {

    @SerializedName("UserDatas")
    List<UserData> userDataList;

    @SerializedName("mr")
    List<MRs> mrDataList;

    @SerializedName("am")
    List<AMs> amDataList;

    public List<UserData> getUserDataList() {
        return userDataList;
    }

    public void setUserDataList(List<UserData> userDataList) {
        this.userDataList = userDataList;
    }

    public List<MRs> getMrDataList() {
        return mrDataList;
    }

    public void setMrDataList(List<MRs> mrDataList) {
        this.mrDataList = mrDataList;
    }

    public List<AMs> getAmDataList() {
        return amDataList;
    }

    public void setAmDataList(List<AMs> amDataList) {
        this.amDataList = amDataList;
    }
}
