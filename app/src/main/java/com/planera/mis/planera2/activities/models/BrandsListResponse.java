package com.planera.mis.planera2.activities.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BrandsListResponse extends MainResponse {
    @SerializedName("data")
    private List<Brands> data;

    public List<Brands> getData() {
        return data;
    }

    public void setData(List<Brands> data) {
        this.data = data;
    }
}
