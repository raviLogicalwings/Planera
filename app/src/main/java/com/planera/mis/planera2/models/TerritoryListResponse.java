package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TerritoryListResponse extends MainResponse {
    @SerializedName("data")
    private List<Territories> territorysList;

    public List<Territories> getTerritorysList() {
        return territorysList;
    }

    public void setTerritorysList(List<Territories> territorysList) {
        this.territorysList = territorysList;
    }
}
