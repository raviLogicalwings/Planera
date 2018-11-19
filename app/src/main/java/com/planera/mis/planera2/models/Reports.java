package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;

public class Reports{

	@SerializedName("Comment")
	private String comment;

	@SerializedName("IsInLocation")
	private String isInLocation;

	@SerializedName("ProductName")
	private Object productName;

	@SerializedName("IsBrand")
	private Object isBrand;

	@SerializedName("PlanId")
	private String planId;

	@SerializedName("InputGiftId")
	private Object inputGiftId;

	@SerializedName("ProductQty")
	private Object productQty;

	@SerializedName("ChemistsId")
	private String chemistsId;

	@SerializedName("Latitude")
	private String latitude;

	@SerializedName("ProductId")
	private Object productId;

	@SerializedName("ChemistName")
	private String chemistName;

	@SerializedName("GiftName")
	private Object giftName;

	@SerializedName("EndDate")
	private String endDate;

	@SerializedName("Longitude")
	private String longitude;

	@SerializedName("DoctorId")
	private Object doctorId;

	@SerializedName("StartDate")
	private String startDate;

	@SerializedName("InputProductId")
	private Object inputProductId;

	@SerializedName("GiftId")
	private Object giftId;

	@SerializedName("UserId")
	private String userId;

	@SerializedName("InputId")
	private String inputId;

	@SerializedName("CreatedDate")
	private String createdDate;

	@SerializedName("InterestedLevel")
	private Object interestedLevel;

	@SerializedName("IsSample")
	private Object isSample;

	@SerializedName("GiftQty")
	private Object giftQty;

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

	public void setProductName(Object productName){
		this.productName = productName;
	}

	public Object getProductName(){
		return productName;
	}

	public void setIsBrand(Object isBrand){
		this.isBrand = isBrand;
	}

	public Object getIsBrand(){
		return isBrand;
	}

	public void setPlanId(String planId){
		this.planId = planId;
	}

	public String getPlanId(){
		return planId;
	}

	public void setInputGiftId(Object inputGiftId){
		this.inputGiftId = inputGiftId;
	}

	public Object getInputGiftId(){
		return inputGiftId;
	}

	public void setProductQty(Object productQty){
		this.productQty = productQty;
	}

	public Object getProductQty(){
		return productQty;
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

	public void setProductId(Object productId){
		this.productId = productId;
	}

	public Object getProductId(){
		return productId;
	}

	public void setChemistName(String chemistName){
		this.chemistName = chemistName;
	}

	public String getChemistName(){
		return chemistName;
	}

	public void setGiftName(Object giftName){
		this.giftName = giftName;
	}

	public Object getGiftName(){
		return giftName;
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

	public void setDoctorId(Object doctorId){
		this.doctorId = doctorId;
	}

	public Object getDoctorId(){
		return doctorId;
	}

	public void setStartDate(String startDate){
		this.startDate = startDate;
	}

	public String getStartDate(){
		return startDate;
	}

	public void setInputProductId(Object inputProductId){
		this.inputProductId = inputProductId;
	}

	public Object getInputProductId(){
		return inputProductId;
	}

	public void setGiftId(Object giftId){
		this.giftId = giftId;
	}

	public Object getGiftId(){
		return giftId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setInputId(String inputId){
		this.inputId = inputId;
	}

	public String getInputId(){
		return inputId;
	}

	public void setCreatedDate(String createdDate){
		this.createdDate = createdDate;
	}

	public String getCreatedDate(){
		return createdDate;
	}

	public void setInterestedLevel(Object interestedLevel){
		this.interestedLevel = interestedLevel;
	}

	public Object getInterestedLevel(){
		return interestedLevel;
	}

	public void setIsSample(Object isSample){
		this.isSample = isSample;
	}

	public Object getIsSample(){
		return isSample;
	}

	public void setGiftQty(Object giftQty){
		this.giftQty = giftQty;
	}

	public Object getGiftQty(){
		return giftQty;
	}

	@Override
 	public String toString(){
		return 
			"Reports{" + 
			"comment = '" + comment + '\'' + 
			",isInLocation = '" + isInLocation + '\'' + 
			",productName = '" + productName + '\'' + 
			",isBrand = '" + isBrand + '\'' + 
			",planId = '" + planId + '\'' + 
			",inputGiftId = '" + inputGiftId + '\'' + 
			",productQty = '" + productQty + '\'' + 
			",chemistsId = '" + chemistsId + '\'' + 
			",latitude = '" + latitude + '\'' + 
			",productId = '" + productId + '\'' + 
			",chemistName = '" + chemistName + '\'' + 
			",giftName = '" + giftName + '\'' + 
			",endDate = '" + endDate + '\'' + 
			",longitude = '" + longitude + '\'' + 
			",doctorId = '" + doctorId + '\'' + 
			",startDate = '" + startDate + '\'' + 
			",inputProductId = '" + inputProductId + '\'' + 
			",giftId = '" + giftId + '\'' + 
			",userId = '" + userId + '\'' + 
			",inputId = '" + inputId + '\'' + 
			",createdDate = '" + createdDate + '\'' + 
			",interestedLevel = '" + interestedLevel + '\'' + 
			",isSample = '" + isSample + '\'' + 
			",giftQty = '" + giftQty + '\'' + 
			"}";
		}
}