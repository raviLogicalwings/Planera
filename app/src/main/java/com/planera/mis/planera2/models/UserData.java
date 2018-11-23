package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;

public class UserData {

    @SerializedName("token")
    private String token;

    @SerializedName("UserId")
    private String userId;

	@SerializedName("Latitude")
	private String latitude;

	@SerializedName("Phone1")
	private String phone1;

	@SerializedName("Phone2")
	private String phone2;

	@SerializedName("OTP")
	private String otp;

	@SerializedName("DOB")
	private String dOB;

	@SerializedName("Address4")
	private String address4;

	@SerializedName("Email1")
	private String email1;

	@SerializedName("Email2")
	private String email2;

	@SerializedName("DOJ")
	private String dOJ;

	@SerializedName("Password")
	private String password;

	@SerializedName("Status")
	private int status;

	@SerializedName("CreatedBy")
	private String createdBy;

	@SerializedName("LoginId")
	private String loginId;

	@SerializedName("FirstName")
	private String firstName;

	@SerializedName("Address2")
	private String address2;

	@SerializedName("Address3")
	private String address3;

	@SerializedName("Address1")
	private String address1;

	@SerializedName("City")
	private String city;

	@SerializedName("MiddleName")
	private String middleName;

	@SerializedName("Longitude")
	private String longitude;

	@SerializedName("ConfirmPassword")
	private String confirmPassword;

	@SerializedName("Type")
	private int type;

	@SerializedName("State")
	private String state;

	@SerializedName("IsDelete")
	private String isDelete;

	@SerializedName("LastName")
	private String lastName;

	@SerializedName("PAN")
	private String pAN;

	@SerializedName("District")
	private String district;

	@SerializedName("Pincode")
	private String pincode;

	@SerializedName("Qualifications")
	private String qualifications;

	@SerializedName("ExperienceYear")
	private String experienceYear;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getLatitude(){
		return latitude;
	}

	public void setPhone1(String phone1){
		this.phone1 = phone1;
	}

	public String getPhone1(){
		return phone1;
	}

	public void setPhone2(String phone2){
		this.phone2 = phone2;
	}

	public String getPhone2(){
		return phone2;
	}

	public void setDOB(String dOB){
		this.dOB = dOB;
	}

	public String getDOB(){
		return dOB;
	}

	public void setAddress4(String address4){
		this.address4 = address4;
	}

	public String getAddress4(){
		return address4;
	}

	public void setEmail1(String email1){
		this.email1 = email1;
	}

	public String getEmail1(){
		return email1;
	}

	public void setEmail2(String email2){
		this.email2 = email2;
	}

	public String getEmail2(){
		return email2;
	}

	public void setDOJ(String dOJ){
		this.dOJ = dOJ;
	}

	public String getDOJ(){
		return dOJ;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public int getStatus(){
		return status;
	}

	public void setCreatedBy(String createdBy){
		this.createdBy = createdBy;
	}

	public String getCreatedBy(){
		return createdBy;
	}

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

	public void setConfirmPassword(String confirmPassword){
		this.confirmPassword = confirmPassword;
	}

	public String getConfirmPassword(){
		return confirmPassword;
	}

	public void setType(int type){
		this.type = type;
	}

	public int getType(){
		return type;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setIsDelete(String isDelete){
		this.isDelete = isDelete;
	}

	public String getIsDelete(){
		return isDelete;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setPAN(String pAN){
		this.pAN = pAN;
	}

	public String getPAN(){
		return pAN;
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

	public void setExperienceYear(String experienceYear){
		this.experienceYear = experienceYear;
	}

	public String getExperienceYear(){
		return experienceYear;
	}


	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	@Override
 	public String toString(){
		return 
			"UserData{" +
			"latitude = '" + latitude + '\'' + 
			",phone1 = '" + phone1 + '\'' + 
			",phone2 = '" + phone2 + '\'' + 
			",dOB = '" + dOB + '\'' + 
			",address4 = '" + address4 + '\'' + 
			",email1 = '" + email1 + '\'' + 
			",email2 = '" + email2 + '\'' + 
			",dOJ = '" + dOJ + '\'' + 
			",password = '" + password + '\'' + 
			",status = '" + status + '\'' + 
			",createdBy = '" + createdBy + '\'' + 
			",loginId = '" + loginId + '\'' + 
			",firstName = '" + firstName + '\'' + 
			",address2 = '" + address2 + '\'' + 
			",address3 = '" + address3 + '\'' + 
			",address1 = '" + address1 + '\'' + 
			",city = '" + city + '\'' + 
			",middleName = '" + middleName + '\'' + 
			",longitude = '" + longitude + '\'' + 
			",confirmPassword = '" + confirmPassword + '\'' + 
			",type = '" + type + '\'' + 
			",state = '" + state + '\'' + 
			",isDelete = '" + isDelete + '\'' + 
			",lastName = '" + lastName + '\'' + 
			",pAN = '" + pAN + '\'' + 
			",district = '" + district + '\'' + 
			",pincode = '" + pincode + '\'' + 
			",qualifications = '" + qualifications + '\'' + 
			",experienceYear = '" + experienceYear + '\'' + 
			"}";
		}
}