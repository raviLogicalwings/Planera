package com.planera.mis.planera2.activities.models;

import com.google.gson.annotations.SerializedName;


public class MainResponse {

    @SerializedName("statuscode")
	private int statusCode;

    @SerializedName("message")
	private String message;

	public void setStatusCode(int statusCode){
		this.statusCode = statusCode;
	}

	public int getStatusCode(){
		return statusCode;
	}


	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	@Override
 	public String toString(){
		return
			"MainResponse{" +
			"statuscode = '" + statusCode + '\'' +
			",message = '" + message + '\'' +
			"}";
		}
}
