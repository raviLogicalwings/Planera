package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChemistListResponse extends MainResponse{

    @SerializedName("data")
    private List<Chemists> data;

    public List<Chemists> getData() {
        return data;
    }

    public void setData(List<Chemists> data) {
        this.data = data;
    }
}
