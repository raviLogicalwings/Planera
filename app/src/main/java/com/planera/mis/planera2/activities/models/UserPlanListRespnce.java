package com.planera.mis.planera2.activities.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserPlanListRespnce extends MainResponse {
    @SerializedName("data")
    List<UserPlan> data;

    public List<UserPlan> getData() {
        return data;
    }

    public void setData(List<UserPlan> data) {
        this.data = data;
    }
}
