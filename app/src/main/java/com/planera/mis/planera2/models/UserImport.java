package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserImport {

    @SerializedName("is_override")
    private int override;

    @SerializedName("import_user_list")
    private List<UserImportItems> userList;

    public int getOverride() {
        return override;
    }

    public void setOverride(int override) {
        this.override = override;
    }

    public List<UserImportItems> getUserList() {
        return userList;
    }

    public void setUserList(List<UserImportItems> userList) {
        this.userList = userList;
    }
}
