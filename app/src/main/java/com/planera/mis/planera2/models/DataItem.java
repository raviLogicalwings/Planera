package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataItem{

	@SerializedName("Comment")
	private String comment;

	@SerializedName("IsInLocation")
	private String isInLocation;

	@SerializedName("ProductName")
	private String productName;

	@SerializedName("IsBrand")
	private int isBrand;

	@SerializedName("PlanId")
	private String planId;

	@SerializedName("InputGiftId")
	private Object inputGiftId;

	@SerializedName("ProductQty")
	private int productQty;

	@SerializedName("UserName")
	private String userName;

	@SerializedName("DoctorName")
	private String doctorName;

	@SerializedName("ChemistsId")
	private String chemistsId;

	@SerializedName("Latitude")
	private String latitude;

	@SerializedName("ProductId")
	private int productId;

	@SerializedName("ChemistName")
	private String chemistName;

	@SerializedName("GiftName")
	private String giftName;

	@SerializedName("EndDate")
	private String endDate;

	@SerializedName("Longitude")
	private String longitude;

	@SerializedName("DoctorId")
	private int doctorId;

	@SerializedName("StartDate")
	private String startDate;

	@SerializedName("InputProductId")
	private int inputProductId;

	@SerializedName("GiftId")
	private int giftId;

	@SerializedName("UserId")
	private String userId;

	@SerializedName("InputId")
	private String inputId;

	@SerializedName("CreatedDate")
	private String createdDate;

	@SerializedName("InterestedLevel")
	private String interestedLevel;

	@SerializedName("IsSample")
	private int isSample;

	@SerializedName("GiftQty")
	private int giftQty;

	@SerializedName("VisitedRank")
	private int visitedRank;

	@SerializedName("VisitDate")
	private String visitDate;

	@SerializedName("StartTime")
	private String startTime;

	@SerializedName("EndTime")
	private String endTime;

	@SerializedName("product_detail")
    private List<InputOrders> productDetails;

	@SerializedName("gift_detail")
	private List<InputGift> giftDetails;

	public List<InputOrders> getProductDetails() {
		return productDetails;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setProductDetails(List<InputOrders> productDetails) {
		this.productDetails = productDetails;
	}

	public List<InputGift> getGiftDetails() {
		return giftDetails;
	}

	public void setGiftDetails(List<InputGift> giftDetails) {
		this.giftDetails = giftDetails;
	}

	public int getVisitedRank() {
		return visitedRank;
	}

	public void setVisitedRank(int visitedRank) {
		this.visitedRank = visitedRank;
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

	public void setProductName(String productName){
		this.productName = productName;
	}

	public String getProductName(){
		return productName;
	}

	public void setIsBrand(int isBrand){
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public void setInputGiftId(Object inputGiftId){
		this.inputGiftId = inputGiftId;
	}

	public Object getInputGiftId(){
		return inputGiftId;
	}

	public void setProductQty(int productQty){
		this.productQty = productQty;
	}

	public int getProductQty(){
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

	public void setProductId(int productId){
		this.productId = productId;
	}

	public int getProductId(){
		return productId;
	}

	public void setChemistName(String chemistName){
		this.chemistName = chemistName;
	}

	public String getChemistName(){
		return chemistName;
	}

	public void setGiftName(String giftName){
		this.giftName = giftName;
	}

	public String getGiftName(){
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

	public void setDoctorId(int doctorId){
		this.doctorId = doctorId;
	}

	public int getDoctorId(){
		return doctorId;
	}

	public void setStartDate(String startDate){
		this.startDate = startDate;
	}

	public String getStartDate(){
		return startDate;
	}

	public void setInputProductId(int inputProductId){
		this.inputProductId = inputProductId;
	}

	public Object getInputProductId(){
		return inputProductId;
	}

	public void setGiftId(int giftId){
		this.giftId = giftId;
	}

	public int getGiftId(){
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

	public void setInterestedLevel(String interestedLevel){
		this.interestedLevel = interestedLevel;
	}

	public String getInterestedLevel(){
		return interestedLevel;
	}

	public void setIsSample(int isSample){
		this.isSample = isSample;
	}

	public int getIsSample(){
		return isSample;
	}

	public void setGiftQty(int giftQty){
		this.giftQty = giftQty;
	}

	public Object getGiftQty(){
		return giftQty;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
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

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

	public String getVisitDate() {
		return visitDate;
	}
}