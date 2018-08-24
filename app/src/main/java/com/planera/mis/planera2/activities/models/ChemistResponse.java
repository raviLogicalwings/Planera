package com.planera.mis.planera2.activities.models;

import com.google.gson.annotations.SerializedName;

public class ChemistResponse extends MainResponse {

    public Chemists getData() {
        return data;
    }

    public void setData(Chemists data) {
        this.data = data;
    }

    @SerializedName("data")
    private Chemists data;
}
