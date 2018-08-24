package com.planera.mis.planera2.activities.models;

import com.google.gson.annotations.SerializedName;

public class Territories{

	@SerializedName("TerritoryId")
	private int territoryId;

	@SerializedName("TerritoryName")
	private String territoryName;

	@SerializedName("StateName")
	private String stateName;

	@SerializedName("StateId")
	private int stateId;

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

	public void setStateName(String stateName){
		this.stateName = stateName;
	}

	public String getStateName(){
		return stateName;
	}

	public void setStateId(int stateId){
		this.stateId = stateId;
	}

	public int getStateId(){
		return stateId;
	}

	@Override
 	public String toString(){
		return 
			"Territories{" + 
			"territoryId = '" + territoryId + '\'' + 
			",territoryName = '" + territoryName + '\'' + 
			",stateName = '" + stateName + '\'' + 
			",stateId = '" + stateId + '\'' + 
			"}";
		}
}