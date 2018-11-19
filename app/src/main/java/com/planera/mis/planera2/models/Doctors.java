package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;

public class Doctors{


	@SerializedName("Email")
	private String email;

	@SerializedName("PatchName")
	private String patchName;

	@SerializedName("Latitude")
	private String latitude;

	@SerializedName("DoctorId")
	private int doctorId;

	@SerializedName("Specializations")
	private String specializations;

	@SerializedName("DOB")
	private String dOB;

	@SerializedName("Phone")
	private String phone;

	@SerializedName("Address4")
	private String address4;

	@SerializedName("Status")
	private String status;

	@SerializedName("FirstName")
	private String firstName;

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

	@SerializedName("PreferredMeetTime")
	private String preferredMeetTime;

	@SerializedName("MeetFrequency")
	private String meetFrequency;

	@SerializedName("State")
	private String state;

	@SerializedName("LastName")
	private String lastName;

	@SerializedName("District")
	private String district;

	@SerializedName("Pincode")
	private String pincode;

	@SerializedName("Qualifications")
	private String qualifications;

	@SerializedName("CRM")
	private String cRM;


	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setPatchName(String patchName){
		this.patchName = patchName;
	}

	public String getPatchName(){
		return patchName;
	}

	public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setDoctorId(int doctorId){
		this.doctorId = doctorId;
	}

	public int getDoctorId(){
		return doctorId;
	}

	public void setSpecializations(String specializations){
		this.specializations = specializations;
	}

	public String getSpecializations(){
		return specializations;
	}

	public void setDOB(String dOB){
		this.dOB = dOB;
	}

	public String getDOB(){
		return dOB;
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

	public void setPreferredMeetTime(String preferredMeetTime){
		this.preferredMeetTime = preferredMeetTime;
	}

	public String getPreferredMeetTime(){
		return preferredMeetTime;
	}

	public void setMeetFrequency(String meetFrequency){
		this.meetFrequency = meetFrequency;
	}

	public String getMeetFrequency(){
		return meetFrequency;
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

	public void setQualifications(String qualifications){
		this.qualifications = qualifications;
	}

	public String getQualifications(){
		return qualifications;
	}

	public void setCRM(String cRM){
		this.cRM = cRM;
	}

	public String getCRM(){
		return cRM;
	}

	@Override
 	public String toString(){
		return 
			"Doctors{" +
			",email = '" + email + '\'' + 
			",patchName = '" + patchName + '\'' + 
			",latitude = '" + latitude + '\'' + 
			",doctorId = '" + doctorId + '\'' + 
			",specializations = '" + specializations + '\'' + 
			",dOB = '" + dOB + '\'' + 
			",phone = '" + phone + '\'' + 
			",address4 = '" + address4 + '\'' + 
			",status = '" + status + '\'' + 
			",firstName = '" + firstName + '\'' + 
			",address2 = '" + address2 + '\'' + 
			",address3 = '" + address3 + '\'' + 
			",patchId = '" + patchId + '\'' + 
			",address1 = '" + address1 + '\'' + 
			",city = '" + city + '\'' + 
			",middleName = '" + middleName + '\'' + 
			",longitude = '" + longitude + '\'' + 
			",preferredMeetTime = '" + preferredMeetTime + '\'' + 
			",meetFrequency = '" + meetFrequency + '\'' + 
			",state = '" + state + '\'' + 
			",lastName = '" + lastName + '\'' + 
			",district = '" + district + '\'' + 
			",pincode = '" + pincode + '\'' + 
			",qualifications = '" + qualifications + '\'' + 
			",cRM = '" + cRM + '\'' + 
			"}";
		}
}