package com.planera.mis.planera2.activities.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DoctorsListResponce extends MainResponse {
    @SerializedName("data")
    private List<Doctors> data;

    public List<Doctors> getData() {
        return data;
    }

    public void setData(List<Doctors> data) {
        this.data = data;
    }
}
