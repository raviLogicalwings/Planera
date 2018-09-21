package com.planera.mis.planera2.activities.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReportsListReposnce extends MainResponse {

    @SerializedName("data")
    List<Reports> data;

    public List<Reports> getData() {
        return data;
    }

    public void setData(List<Reports> data) {
        this.data = data;
    }
}
