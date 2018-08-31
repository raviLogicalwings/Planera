package com.planera.mis.planera2.activities.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlansListResponce extends MainResponse {
    @SerializedName("data")
    private List<Plans> data;

    public List<Plans> getData() {
        return data;
    }

    public void setData(List<Plans> data) {
        this.data = data;
    }
}
