package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;

public class InputResponce extends MainResponse {
    @SerializedName("data")
    private Input data;

    public Input getData() {
        return data;
    }

    public void setData(Input data) {
        this.data = data;
    }
}
