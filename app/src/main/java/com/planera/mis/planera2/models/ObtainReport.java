package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;

public class ObtainReport{

	@SerializedName("StartDate")
	private String startDate;

	@SerializedName("Type")
	private String type;

	@SerializedName("EndDate")
	private String endDate;

	@SerializedName("ChemistsId")
	private String chemistId = null;

	@SerializedName("DoctorId")
	private String doctorId = null;

	@SerializedName("UserId")
	private String userId = null ;

	@SerializedName("EmailId")
	private String emailSendTo;

	@SerializedName("isJoint")
	private int isJoint;

	public String getEmailSendTo() {
		return emailSendTo;
	}

	public void setEmailSendTo(String emailSendTo) {
		this.emailSendTo = emailSendTo;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setStartDate(String startDate){
		this.startDate = startDate;
	}

	public String getStartDate(){
		return startDate;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setEndDate(String endDate){
		this.endDate = endDate;
	}

	public String getEndDate(){
		return endDate;
	}

	public String getChemistId() {
		return chemistId;
	}

	public void setChemistId(String id) {
		this.chemistId = id;
	}

	public int getIsJoint() {
		return isJoint;
	}

	public void setIsJoint(int isJoint) {
		this.isJoint = isJoint;
	}

	@Override
 	public String toString(){
		return 
			"ObtainReport{" + 
			"startDate = '" + startDate + '\'' + 
			",type = '" + type + '\'' + 
			",endDate = '" + endDate + '\'' + 
			"}";
		}
}