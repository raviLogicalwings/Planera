package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReportsListResponse extends MainResponse {

    @SerializedName("data")
    List<Reports> data;

    public List<Reports> getData() {
        return data;
    }

    public void setData(List<Reports> data) {
        this.data = data;
    }
}
