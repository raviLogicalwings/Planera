package com.planera.mis.planera2.models;



import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MRs implements Serializable {

	@SerializedName("FirstName")
	private String firstName;

	@SerializedName("userId")
	private String userId;

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	@Override
 	public String toString(){
		return 
			"MRs{" + 
			"firstName = '" + firstName + '\'' + 
			",userId = '" + userId + '\'' + 
			"}";
		}
}