package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;

public class AdminPlan{

	@SerializedName("TerritoryId")
	private int territoryId;

	@SerializedName("num_of_patchID_count")
	private String numOfPatchIDCount;

	@SerializedName("patch_name")
	private String patchName;

	@SerializedName("UserId")
	private String userId;

	@SerializedName("PlanId")
	private int planId;

	@SerializedName("territory_name")
	private String territoryName;

	@SerializedName("CreatedDate")
	private String createdDate;

	@SerializedName("userName")
	private String userName;

	@SerializedName("PatchId")
	private int patchId;

	@SerializedName("Remark")
	private String remark;

	public void setTerritoryId(int territoryId){
		this.territoryId = territoryId;
	}

	public int getTerritoryId(){
		return territoryId;
	}

	public void setNumOfPatchIDCount(String numOfPatchIDCount){
		this.numOfPatchIDCount = numOfPatchIDCount;
	}

	public String getNumOfPatchIDCount(){
		return numOfPatchIDCount;
	}

	public void setPatchName(String patchName){
		this.patchName = patchName;
	}

	public String getPatchName(){
		return patchName;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setPlanId(int planId){
		this.planId = planId;
	}

	public int getPlanId(){
		return planId;
	}

	public void setTerritoryName(String territoryName){
		this.territoryName = territoryName;
	}

	public String getTerritoryName(){
		return territoryName;
	}

	public void setCreatedDate(String createdDate){
		this.createdDate = createdDate;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public void setPatchId(int patchId){
		this.patchId = patchId;
	}

	public int getPatchId(){
		return patchId;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getRemark(){
		return remark;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
 	public String toString(){
		return 
			"AdminPlan{" + 
			"territoryId = '" + territoryId + '\'' + 
			",num_of_patchID_count = '" + numOfPatchIDCount + '\'' + 
			",patch_name = '" + patchName + '\'' + 
			",userId = '" + userId + '\'' + 
			",planId = '" + planId + '\'' + 
			",territory_name = '" + territoryName + '\'' + 
			",createdDate = '" + createdDate + '\'' + 
			",patchId = '" + patchId + '\'' + 
			",remark = '" + remark + '\'' + 
			"}";
		}
}