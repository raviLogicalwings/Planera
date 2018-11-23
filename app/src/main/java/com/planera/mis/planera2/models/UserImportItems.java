package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;

public class UserImportItems{

	@SerializedName("LoginId")
	private String loginId;

	@SerializedName("First Name")
	private String firstName;

	@SerializedName("Experience Years")
	private String experienceYears;

	@SerializedName("Middle Name")
	private String middleName;

	@SerializedName("City")
	private String city;

	@SerializedName("Address Line - 2")
	private String addressLine2;

	@SerializedName("Address Line - 1")
	private String addressLine1;

	@SerializedName("Type")
	private String type;

	@SerializedName("Active")
	private String active;

	@SerializedName("Phone1")
	private String phone1;

	@SerializedName("Address Line - 3")
	private String addressLine3;

	@SerializedName("PIN")
	private String pIN;

	@SerializedName("Phone2")
	private String phone2;

	@SerializedName("UserId")
	private String userId;

	@SerializedName("DOB")
	private String dOB;

	@SerializedName("State")
	private String state;

	@SerializedName("PAN Number")
	private String pANNumber;

	@SerializedName("Email1")
	private String email1;

	@SerializedName("Last Name")
	private String lastName;

	@SerializedName("District")
	private String district;

	@SerializedName("DOJ")
	private String dOJ;

	@SerializedName("Qualifications")
	private String qualifications;

	public void setLoginId(String loginId){
		this.loginId = loginId;
	}

	public String getLoginId(){
		return loginId;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setExperienceYears(String experienceYears){
		this.experienceYears = experienceYears;
	}

	public String getExperienceYears(){
		return experienceYears;
	}

	public void setMiddleName(String middleName){
		this.middleName = middleName;
	}

	public String getMiddleName(){
		return middleName;
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

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setActive(String active){
		this.active = active;
	}

	public String getActive(){
		return active;
	}

	public void setPhone1(String phone1){
		this.phone1 = phone1;
	}

	public String getPhone1(){
		return phone1;
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

	public void setPhone2(String phone2){
		this.phone2 = phone2;
	}

	public String getPhone2(){
		return phone2;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setDOB(String dOB){
		this.dOB = dOB;
	}

	public String getDOB(){
		return dOB;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setPANNumber(String pANNumber){
		this.pANNumber = pANNumber;
	}

	public String getPANNumber(){
		return pANNumber;
	}

	public void setEmail1(String email1){
		this.email1 = email1;
	}

	public String getEmail1(){
		return email1;
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

	public void setDOJ(String dOJ){
		this.dOJ = dOJ;
	}

	public String getDOJ(){
		return dOJ;
	}

	public void setQualifications(String qualifications){
		this.qualifications = qualifications;
	}

	public String getQualifications(){
		return qualifications;
	}

	@Override
 	public String toString(){
		return 
			"UserImportItems{" + 
			"loginId = '" + loginId + '\'' + 
			",first Name = '" + firstName + '\'' + 
			",experience Years = '" + experienceYears + '\'' + 
			",middle Name = '" + middleName + '\'' + 
			",city = '" + city + '\'' + 
			",address Line - 2 = '" + addressLine2 + '\'' + 
			",address Line - 1 = '" + addressLine1 + '\'' + 
			",type = '" + type + '\'' + 
			",active = '" + active + '\'' + 
			",phone1 = '" + phone1 + '\'' + 
			",address Line - 3 = '" + addressLine3 + '\'' + 
			",pIN = '" + pIN + '\'' + 
			",phone2 = '" + phone2 + '\'' + 
			",userId = '" + userId + '\'' + 
			",dOB = '" + dOB + '\'' + 
			",state = '" + state + '\'' + 
			",pAN Number = '" + pANNumber + '\'' + 
			",email1 = '" + email1 + '\'' + 
			",last Name = '" + lastName + '\'' + 
			",district = '" + district + '\'' + 
			",dOJ = '" + dOJ + '\'' + 
			",qualifications = '" + qualifications + '\'' + 
			"}";
		}
}