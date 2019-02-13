package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdminPlanResponse extends MainResponse {

    @SerializedName("data")
        List<AdminPlan> data;

    public List<AdminPlan> getData() {
        return data;
    }

    public void setData(List<AdminPlan> data) {
        this.data = data;
    }
}
