package com.planera.mis.planera2.activities.models;

import com.google.gson.annotations.SerializedName;

public class ObtainReport{

	@SerializedName("StartDate")
	private String startDate;

	@SerializedName("Type")
	private String type;

	@SerializedName("EndDate")
	private String endDate;

	@SerializedName("Id")
	private String id;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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