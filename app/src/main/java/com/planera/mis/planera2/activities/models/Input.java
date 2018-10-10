package com.planera.mis.planera2.activities.models;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Input{

	@SerializedName("InputId")
	private String inputId;

	@SerializedName("DoctorId")
	private String doctorId;

	@SerializedName("StartDate")
	private String startDate;

	@SerializedName("Comment")
	private String comment;

	@SerializedName("IsInLocation")
	private String isInLocation;

	@SerializedName("UserId")
	private String userId;

	@SerializedName("PlanId")
	private String planId;

	@SerializedName("ChemistsId")
	private String chemistsId;

	@SerializedName("Latitude")
	private String latitude;

	@SerializedName("EndDate")
	private String endDate;

	@SerializedName("Longitude")
	private String longitude;

	@SerializedName("EarlierEntryFeedback")
	private String earlierEntryFeedback;

	@SerializedName("VisitDate")
	private String visitDate;

	public String getEarlierEntryFeedback() {
		return earlierEntryFeedback;
	}

	public void setEarlierEntryFeedback(String earlierEntryFeedback) {
		this.earlierEntryFeedback = earlierEntryFeedback;
	}

	public String getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(String visitDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");//yyyy-MM-dd'T'HH:mm:ss
		SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
		Date data = null;
		try {
			data = sdf.parse(visitDate);
		} catch (ParseException e) {
			Log.e("Exp", e.getMessage());
		}
		visitDate = output.format(data);

		this.visitDate = visitDate;
	}

	public String getInputId() {
		return inputId;
	}

	public void setInputId(String inputId) {
		this.inputId = inputId;
	}

	public void setDoctorId(String doctorId){
		this.doctorId = doctorId;
	}

	public String getDoctorId(){
		return doctorId;
	}

	public void setStartDate(String startDate){
		this.startDate = startDate;
	}

	public String getStartDate(){
		return startDate;
	}

	public void setComment(String comment){
		this.comment = comment;
	}

	public String getComment(){
		return comment;
	}

	public void setIsInLocation(String isInLocation){
		this.isInLocation = isInLocation;
	}

	public String getIsInLocation(){
		return isInLocation;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setPlanId(String planId){
		this.planId = planId;
	}

	public String getPlanId(){
		return planId;
	}

	public void setChemistsId(String chemistsId){
		this.chemistsId = chemistsId;
	}

	public String getChemistsId(){
		return chemistsId;
	}

	public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setEndDate(String endDate){
		this.endDate = endDate;
	}

	public String getEndDate(){
		return endDate;
	}

	public void setLongitude(String longitude){
		this.longitude = longitude;
	}

	public String getLongitude(){
		return longitude;
	}

	@Override
 	public String toString(){
		return 
			"Input{" + 
			"doctorId = '" + doctorId + '\'' + 
			",startDate = '" + startDate + '\'' + 
			",comment = '" + comment + '\'' + 
			",isInLocation = '" + isInLocation + '\'' + 
			",userId = '" + userId + '\'' + 
			",planId = '" + planId + '\'' + 
			",chemistsId = '" + chemistsId + '\'' + 
			",latitude = '" + latitude + '\'' + 
			",endDate = '" + endDate + '\'' + 
			",longitude = '" + longitude + '\'' + 
			"}";
		}
}