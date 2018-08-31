package com.planera.mis.planera2.activities.models;


import com.google.gson.annotations.SerializedName;

public class Plans{

	@SerializedName("DoctorId")
	private int doctorId;

	@SerializedName("Status")
	private String status;

	@SerializedName("TerritoryId")
	private int territoryId;

	@SerializedName("Month")
	private String month;

	@SerializedName("Year")
	private String year;

	@SerializedName("UserId")
	private int userId;

	@SerializedName("PatchName")
	private String patchName;

	@SerializedName("PlanId")
	private int planId;

	@SerializedName("Calls")
	private String calls;

	@SerializedName("PatchId")
	private int patchId;

	@SerializedName("ChemistsId")
	private int chemistsId;

	@SerializedName("Remark")
	private String remark;

	public void setDoctorId(int doctorId){
		this.doctorId = doctorId;
	}

	public int getDoctorId(){
		return doctorId;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setTerritoryId(int territoryId){
		this.territoryId = territoryId;
	}

	public int getTerritoryId(){
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

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setPatchName(String patchName){
		this.patchName = patchName;
	}

	public String getPatchName(){
		return patchName;
	}

	public void setPlanId(int planId){
		this.planId = planId;
	}

	public int getPlanId(){
		return planId;
	}

	public void setCalls(String calls){
		this.calls = calls;
	}

	public String getCalls(){
		return calls;
	}

	public void setPatchId(int patchId){
		this.patchId = patchId;
	}

	public int getPatchId(){
		return patchId;
	}

	public void setChemistsId(int chemistsId){
		this.chemistsId = chemistsId;
	}

	public int getChemistsId(){
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