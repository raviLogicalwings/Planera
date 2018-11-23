package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChemistImport {

    @SerializedName("is_override")
    private int isOverride;

    @SerializedName("import_chemist_list")
    private List<ChemistImportItems> chemistImportList;

    public int getIsOverride() {
        return isOverride;
    }

    public void setIsOverride(int isOverride) {
        this.isOverride = isOverride;
    }

    public List<ChemistImportItems> getChemistImportList() {
        return chemistImportList;
    }

    public void setChemistImportList(List<ChemistImportItems> chemistImportList) {
        this.chemistImportList = chemistImportList;
    }
}
