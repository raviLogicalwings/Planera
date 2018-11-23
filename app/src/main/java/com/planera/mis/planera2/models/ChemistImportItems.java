package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;

public class ChemistImportItems {

	@SerializedName("First Name")
	private String firstName;

	@SerializedName("Middle Name")
	private String middleName;

	@SerializedName("PatchId")
	private String patchId;

	@SerializedName("City")
	private String city;

	@SerializedName("Address Line - 2")
	private String addressLine2;

	@SerializedName("Address Line - 1")
	private String addressLine1;

	@SerializedName("ChemistId")
	private String chemistId;

	@SerializedName("Company Name")
	private String companyName;

	@SerializedName("Active")
	private String active;

	@SerializedName("Address Line - 3")
	private String addressLine3;

	@SerializedName("PIN")
	private String pIN;

	@SerializedName("State")
	private String state;

	@SerializedName("Preferred Meet Time")
	private String preferredMeetTime;

	@SerializedName("Last Name")
	private String lastName;

	@SerializedName("District")
	private String district;

	@SerializedName("Owner Phone")
	private String onwerPhone;

	@SerializedName("Owner Email")
	private String onwerEmail;

	@SerializedName("Monthly Volume Potentia")
	private String monthlyVolumePotential;

	@SerializedName("Shop Size")
	private String shopSize;

	@SerializedName("Billing Phone1")
	private String billingPhone1;

	@SerializedName("Billing Phone2")
	private String billingPhone2;

	@SerializedName("Billing Email")
	private String billingEmail;


	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setPatchId(String patchId){
		this.patchId = patchId;
	}

	public String getPatchId(){
		return patchId;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setAddressLine2(String addressLine2){
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine2(){
		return addressLine2;
	}

	public void setAddressLine1(String addressLine1){
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine1(){
		return addressLine1;
	}

	public void setChemistId(String chemistId){
		this.chemistId = chemistId;
	}

	public String getChemistId(){
		return chemistId;
	}

	public void setCompanyName(String companyName){
		this.companyName = companyName;
	}

	public String getCompanyName(){
		return companyName;
	}

	public void setActive(String active){
		this.active = active;
	}

	public String getActive(){
		return active;
	}

	public void setAddressLine3(String addressLine3){
		this.addressLine3 = addressLine3;
	}

	public String getAddressLine3(){
		return addressLine3;
	}

	public void setPIN(String pIN){
		this.pIN = pIN;
	}

	public String getPIN(){
		return pIN;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setPreferredMeetTime(String preferredMeetTime){
		this.preferredMeetTime = preferredMeetTime;
	}

	public String getPreferredMeetTime(){
		return preferredMeetTime;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setDistrict(String district){
		this.district = district;
	}

	public String getDistrict(){
		return district;
	}


	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getpIN() {
		return pIN;
	}

	public void setpIN(String pIN) {
		this.pIN = pIN;
	}

	public String getOnwerPhone() {
		return onwerPhone;
	}

	public void setOnwerPhone(String onwerPhone) {
		this.onwerPhone = onwerPhone;
	}

	public String getOnwerEmail() {
		return onwerEmail;
	}

	public void setOnwerEmail(String onwerEmail) {
		this.onwerEmail = onwerEmail;
	}

	public String getMonthlyVolumePotential() {
		return monthlyVolumePotential;
	}

	public void setMonthlyVolumePotential(String monthlyVolumePotential) {
		this.monthlyVolumePotential = monthlyVolumePotential;
	}

	public String getShopSize() {
		return shopSize;
	}

	public void setShopSize(String shopSize) {
		this.shopSize = shopSize;
	}

	public String getBillingPhone1() {
		return billingPhone1;
	}

	public void setBillingPhone1(String billingPhone1) {
		this.billingPhone1 = billingPhone1;
	}

	public String getBillingPhone2() {
		return billingPhone2;
	}

	public void setBillingPhone2(String billingPhone2) {
		this.billingPhone2 = billingPhone2;
	}

	public String getBillingEmail() {
		return billingEmail;
	}

	public void setBillingEmail(String billingEmail) {
		this.billingEmail = billingEmail;
	}

	@Override
 	public String toString(){
		return 
			"ChemistImportItems{" +
			"first Name = '" + firstName + '\'' + 
			",patchId = '" + patchId + '\'' + 
			",city = '" + city + '\'' + 
			",address Line - 2 = '" + addressLine2 + '\'' + 
			",address Line - 1 = '" + addressLine1 + '\'' + 
			",chemistId = '" + chemistId + '\'' + 
			",company Name = '" + companyName + '\'' + 
			",active = '" + active + '\'' + 
			",address Line - 3 = '" + addressLine3 + '\'' + 
			",pIN = '" + pIN + '\'' + 
			",state = '" + state + '\'' + 
			",preferred Meet Time = '" + preferredMeetTime + '\'' + 
			",last Name = '" + lastName + '\'' + 
			",district = '" + district + '\'' + 
			"}";
		}
}