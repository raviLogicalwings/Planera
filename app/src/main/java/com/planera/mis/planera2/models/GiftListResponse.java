package com.planera.mis.planera2.models;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GiftListResponse extends MainResponse{
        @SerializedName("data")
        List<GiftsData> giftsData;

    public List<GiftsData> getGiftsData() {
        return giftsData;
    }

    public void setGiftsData(List<GiftsData> giftsData) {
        this.giftsData = giftsData;
    }
}
