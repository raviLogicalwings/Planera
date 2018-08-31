package com.planera.mis.planera2.activities.models;


import com.google.gson.annotations.SerializedName;

public class Plans{

	@SerializedName("DoctorId")
	private String doctorId;

	@SerializedName("Status")
	private String status;

	@SerializedName("TerritoryId")
	private String territoryId;

	@SerializedName("Month")
	private String month;

	@SerializedName("Year")
	private String year;

	@SerializedName("UserId")
	private String userId;

	@SerializedName("PatchName")
	private String patchName;

	@SerializedName("PlanId")
	private String planId;

	@SerializedName("Calls")
	private String calls;

	@SerializedName("PatchId")
	private String patchId;

	@SerializedName("ChemistsId")
	private String chemistsId;

	@SerializedName("Remark")
	private String remark;

	public void setDoctorId(String doctorId){
		this.doctorId = doctorId;
	}

	public String getDoctorId(){
		return doctorId;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setTerritoryId(String territoryId){
		this.territoryId = territoryId;
	}

	public String getTerritoryId(){
		return territoryId;
	}

	public void setMonth(String month){
		this.month = month;
	}

	public String getMonth(){
		return month;
	}

	public void setYear(String year){
		this.year = year;
	}

	public String getYear(){
		return year;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setPatchName(String patchName){
		this.patchName = patchName;
	}

	public String getPatchName(){
		return patchName;
	}

	public void setPlanId(String planId){
		this.planId = planId;
	}

	public String getPlanId(){
		return planId;
	}

	public void setCalls(String calls){
		this.calls = calls;
	}

	public String getCalls(){
		return calls;
	}

	public void setPatchId(String patchId){
		this.patchId = patchId;
	}

	public String getPatchId(){
		return patchId;
	}

	public void setChemistsId(String chemistsId){
		this.chemistsId = chemistsId;
	}

	public String getChemistsId(){
		return chemistsId;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getRemark(){
		return remark;
	}

	@Override
 	public String toString(){
		return 
			"Plans{" + 
			"doctorId = '" + doctorId + '\'' + 
			",status = '" + status + '\'' + 
			",territoryId = '" + territoryId + '\'' + 
			",month = '" + month + '\'' + 
			",year = '" + year + '\'' + 
			",userId = '" + userId + '\'' + 
			",patchName = '" + patchName + '\'' + 
			",planId = '" + planId + '\'' + 
			",calls = '" + calls + '\'' + 
			",patchId = '" + patchId + '\'' + 
			",chemistsId = '" + chemistsId + '\'' + 
			",remark = '" + remark + '\'' + 
			"}";
		}
}