package com.planera.mis.planera2.activities.models;

import com.google.gson.annotations.SerializedName;

public class Chemists{

	@SerializedName("Email")
	private String email;

	@SerializedName("Rating")
	private String rating;

	@SerializedName("Latitude")
	private String latitude;

	@SerializedName("CompanyName")
	private String companyName;

	@SerializedName("Phone")
	private String phone;

	@SerializedName("Address4")
	private String address4;

	@SerializedName("Status")
	private String status;

	@SerializedName("FirstName")
	private String firstName;

	@SerializedName("MonthlyVolumePotential")
	private String monthlyVolumePotential;

	@SerializedName("Address2")
	private String address2;

	@SerializedName("Address3")
	private String address3;

	@SerializedName("PatchId")
	private String patchId;

	@SerializedName("Address1")
	private String address1;

	@SerializedName("City")
	private String city;

	@SerializedName("MiddleName")
	private String middleName;

	@SerializedName("Longitude")
	private String longitude;

	@SerializedName("ShopSize")
	private String shopSize;

	@SerializedName("PreferredMeetTime")
	private String preferredMeetTime;

	@SerializedName("State")
	private String state;

	@SerializedName("LastName")
	private String lastName;

	@SerializedName("District")
	private String district;

	@SerializedName("Pincode")
	private String pincode;

	@SerializedName("BillingPhone2")
	private String billingPhone2;

	@SerializedName("BillingEmail")
	private String billingEmail;

	@SerializedName("BillingPhone1")
	private String billingPhone1;

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setRating(String rating){
		this.rating = rating;
	}

	public String getRating(){
		return rating;
	}

	public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setCompanyName(String companyName){
		this.companyName = companyName;
	}

	public String getCompanyName(){
		return companyName;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setAddress4(String address4){
		this.address4 = address4;
	}

	public String getAddress4(){
		return address4;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setMonthlyVolumePotential(String monthlyVolumePotential){
		this.monthlyVolumePotential = monthlyVolumePotential;
	}

	public String getMonthlyVolumePotential(){
		return monthlyVolumePotential;
	}

	public void setAddress2(String address2){
		this.address2 = address2;
	}

	public String getAddress2(){
		return address2;
	}

	public void setAddress3(String address3){
		this.address3 = address3;
	}

	public String getAddress3(){
		return address3;
	}

	public void setPatchId(String patchId){
		this.patchId = patchId;
	}

	public String getPatchId(){
		return patchId;
	}

	public void setAddress1(String address1){
		this.address1 = address1;
	}

	public String getAddress1(){
		return address1;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setMiddleName(String middleName){
		this.middleName = middleName;
	}

	public String getMiddleName(){
		return middleName;
	}

	public void setLongitude(String longitude){
		this.longitude = longitude;
	}

	public String getLongitude(){
		return longitude;
	}

	public void setShopSize(String shopSize){
		this.shopSize = shopSize;
	}

	public String getShopSize(){
		return shopSize;
	}

	public void setPreferredMeetTime(String preferredMeetTime){
		this.preferredMeetTime = preferredMeetTime;
	}

	public String getPreferredMeetTime(){
		return preferredMeetTime;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
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

	public void setPincode(String pincode){
		this.pincode = pincode;
	}

	public String getPincode(){
		return pincode;
	}

	public void setBillingPhone2(String billingPhone2){
		this.billingPhone2 = billingPhone2;
	}

	public String getBillingPhone2(){
		return billingPhone2;
	}

	public void setBillingEmail(String billingEmail){
		this.billingEmail = billingEmail;
	}

	public String getBillingEmail(){
		return billingEmail;
	}

	public void setBillingPhone1(String billingPhone1){
		this.billingPhone1 = billingPhone1;
	}

	public String getBillingPhone1(){
		return billingPhone1;
	}

	@Override
 	public String toString(){
		return 
			"Chemists{" + 
			"email = '" + email + '\'' + 
			",rating = '" + rating + '\'' + 
			",latitude = '" + latitude + '\'' + 
			",companyName = '" + companyName + '\'' + 
			",phone = '" + phone + '\'' + 
			",address4 = '" + address4 + '\'' + 
			",status = '" + status + '\'' + 
			",firstName = '" + firstName + '\'' + 
			",monthlyVolumePotential = '" + monthlyVolumePotential + '\'' + 
			",address2 = '" + address2 + '\'' + 
			",address3 = '" + address3 + '\'' + 
			",patchId = '" + patchId + '\'' + 
			",address1 = '" + address1 + '\'' + 
			",city = '" + city + '\'' + 
			",middleName = '" + middleName + '\'' + 
			",longitude = '" + longitude + '\'' + 
			",shopSize = '" + shopSize + '\'' + 
			",preferredMeetTime = '" + preferredMeetTime + '\'' + 
			",state = '" + state + '\'' + 
			",lastName = '" + lastName + '\'' + 
			",district = '" + district + '\'' + 
			",pincode = '" + pincode + '\'' + 
			",billingPhone2 = '" + billingPhone2 + '\'' + 
			",billingEmail = '" + billingEmail + '\'' + 
			",billingPhone1 = '" + billingPhone1 + '\'' + 
			"}";
		}
}