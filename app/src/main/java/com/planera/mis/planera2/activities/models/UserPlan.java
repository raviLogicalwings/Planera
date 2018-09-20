package com.planera.mis.planera2.activities.models;


import com.google.gson.annotations.SerializedName;

public class UserPlan{


	@SerializedName("DoctorLatitude")
	private double doctorLatitude;

	@SerializedName("DoctorLongitude")
	private double doctorLongitude;

	@SerializedName("ChemistLatitude")
	private double chemistLatitude;

	@SerializedName("ChemistLongitude")
	private double chemistLongitude;

	@SerializedName("Status")
	private String status;

	@SerializedName("TerritoryId")
	private int territoryId;

	@SerializedName("TerritoryName")
	private String territoryName;

	@SerializedName("DoctorFirstName")
	private String doctorFirstName;

	@SerializedName("PatchName")
	private String patchName;

	@SerializedName("PlanId")
	private int planId;

	@SerializedName("Calls")
	private String calls;

	@SerializedName("ChemistFirstName")
	private String chemistFirstName;

	@SerializedName("UserLastName")
	private String userLastName;

	@SerializedName("PatchId")
	private int patchId;

	@SerializedName("ChemistsId")
	private String chemistsId;

	@SerializedName("UserFirstName")
	private String userFirstName;

	@SerializedName("DoctorMiddleName")
	private String doctorMiddleName;

	@SerializedName("Remark")
	private String remark;

	@SerializedName("DoctorId")
	private int doctorId;

	@SerializedName("Month")
	private String month;

	@SerializedName("UserMiddleName")
	private String userMiddleName;

	@SerializedName("ChemistMiddleName")
	private String chemistMiddleName;

	@SerializedName("Year")
	private String year;

	@SerializedName("UserId")
	private int userId;

	@SerializedName("DoctorLastName")
	private String doctorLastName;

	@SerializedName("ChemistLastName")
	private String chemistLastName;

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

	public void setTerritoryName(String territoryName){
		this.territoryName = territoryName;
	}

	public String getTerritoryName(){
		return territoryName;
	}

	public void setDoctorFirstName(String doctorFirstName){
		this.doctorFirstName = doctorFirstName;
	}

	public String getDoctorFirstName(){
		return doctorFirstName;
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

	public void setChemistFirstName(String chemistFirstName){
		this.chemistFirstName = chemistFirstName;
	}

	public String getChemistFirstName(){
		return chemistFirstName;
	}

	public void setUserLastName(String userLastName){
		this.userLastName = userLastName;
	}

	public String getUserLastName(){
		return userLastName;
	}

	public void setPatchId(int patchId){
		this.patchId = patchId;
	}

	public int getPatchId(){
		return patchId;
	}

	public void setChemistsId(String chemistsId){
		this.chemistsId = chemistsId;
	}

	public String getChemistsId(){
		return chemistsId;
	}

	public void setUserFirstName(String userFirstName){
		this.userFirstName = userFirstName;
	}

	public String getUserFirstName(){
		return userFirstName;
	}

	public void setDoctorMiddleName(String doctorMiddleName){
		this.doctorMiddleName = doctorMiddleName;
	}

	public String getDoctorMiddleName(){
		return doctorMiddleName;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getRemark(){
		return remark;
	}

	public void setDoctorId(int doctorId){
		this.doctorId = doctorId;
	}

	public int getDoctorId(){
		return doctorId;
	}

	public void setMonth(String month){
		this.month = month;
	}

	public String getMonth(){
		return month;
	}

	public void setUserMiddleName(String userMiddleName){
		this.userMiddleName = userMiddleName;
	}

	public String getUserMiddleName(){
		return userMiddleName;
	}

	public void setChemistMiddleName(String chemistMiddleName){
		this.chemistMiddleName = chemistMiddleName;
	}

	public String getChemistMiddleName(){
		return chemistMiddleName;
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

	public void setDoctorLastName(String doctorLastName){
		this.doctorLastName = doctorLastName;
	}

	public String getDoctorLastName(){
		return doctorLastName;
	}

	public void setChemistLastName(String chemistLastName){
		this.chemistLastName = chemistLastName;
	}

	public String getChemistLastName(){
		return chemistLastName;
	}

	public double getDoctorLatitude() {
		return doctorLatitude;
	}

	public void setDoctorLatitude(long doctorLatitude) {
		this.doctorLatitude = doctorLatitude;
	}

	public double getDoctorLongitude() {
		return doctorLongitude;
	}

	public void setDoctorLongitude(long doctorLongitude) {
		this.doctorLongitude = doctorLongitude;
	}

	public double getChemistLatitude() {
		return chemistLatitude;
	}

	public void setChemistLatitude(long chemistLatitude) {
		this.chemistLatitude = chemistLatitude;
	}

	public double getChemistLongitude() {
		return chemistLongitude;
	}

	public void setChemistLongitude(long chemistLongitude) {
		this.chemistLongitude = chemistLongitude;
	}

	@Override
 	public String toString(){
		return 
			"UserPlan{" + 
			"status = '" + status + '\'' + 
			",territoryId = '" + territoryId + '\'' + 
			",territoryName = '" + territoryName + '\'' + 
			",doctorFirstName = '" + doctorFirstName + '\'' + 
			",patchName = '" + patchName + '\'' + 
			",planId = '" + planId + '\'' + 
			",calls = '" + calls + '\'' + 
			",chemistFirstName = '" + chemistFirstName + '\'' + 
			",userLastName = '" + userLastName + '\'' + 
			",patchId = '" + patchId + '\'' + 
			",chemistsId = '" + chemistsId + '\'' + 
			",userFirstName = '" + userFirstName + '\'' + 
			",doctorMiddleName = '" + doctorMiddleName + '\'' + 
			",remark = '" + remark + '\'' + 
			",doctorId = '" + doctorId + '\'' + 
			",month = '" + month + '\'' + 
			",userMiddleName = '" + userMiddleName + '\'' + 
			",chemistMiddleName = '" + chemistMiddleName + '\'' + 
			",year = '" + year + '\'' + 
			",userId = '" + userId + '\'' + 
			",doctorLastName = '" + doctorLastName + '\'' + 
			",chemistLastName = '" + chemistLastName + '\'' + 
			"}";
		}
}