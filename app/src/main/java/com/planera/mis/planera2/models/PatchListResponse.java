package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PatchListResponse extends MainResponse {
    @SerializedName("data")
    private List<Patches> patchesList;

    public List<Patches> getPatchesList() {
        return patchesList;
    }

    public void setPatchesList(List<Patches> patchesList) {
        this.patchesList = patchesList;
    }
}
