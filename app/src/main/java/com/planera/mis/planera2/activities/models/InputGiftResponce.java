package com.planera.mis.planera2.activities.models;

import com.google.gson.annotations.SerializedName;

public class InputGiftResponce extends MainResponse {
    @SerializedName("data")
    private InputGift data;

    public InputGift getData() {
        return data;
    }

    public void setData(InputGift data) {
        this.data = data;
    }
}
