package com.planera.mis.planera2.activities.models;


import com.google.gson.annotations.SerializedName;


public class States{

	@SerializedName("StateId")
	private int stateId;

	@SerializedName("Name")
	private String name;

	public void setStateId(int stateId){
		this.stateId = stateId;
	}

	public int getStateId(){
		return stateId;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	@Override
 	public String toString(){
		return 
			"States{" + 
			"stateId = '" + stateId + '\'' + 
			",name = '" + name + '\'' + 
			"}";
		}
}