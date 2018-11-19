package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;

public class DoctorResponse extends MainResponse {
    @SerializedName("data")
    public Doctors data;

    public Doctors getData() {
        return data;
    }

    public void setData(Doctors data) {
        this.data = data;
    }
}
