package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DoctorImport {

    @SerializedName("is_override")
    private int override;

    @SerializedName("import_doctor_list")
    private List<DoctorImportItems> doctorImportList;

    public int getOverride() {
        return override;
    }

    public void setOverride(int override) {
        this.override = override;
    }

    public List<DoctorImportItems> getDoctorImportList() {
        return doctorImportList;
    }

    public void setDoctorImportList(List<DoctorImportItems> doctorImportList) {
        this.doctorImportList = doctorImportList;
    }
}
