package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StateListResponse extends MainResponse {
    @SerializedName("data")
    private List<States> data;

    public List<States> getData() {
        return data;
    }

    public void setData(List<States> data) {
        this.data = data;
    }
}
