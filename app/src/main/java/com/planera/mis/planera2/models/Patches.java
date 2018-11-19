package com.planera.mis.planera2.models;


import com.google.gson.annotations.SerializedName;

public class Patches{

	@SerializedName("TerritoryId")
	private int territoryId;

	@SerializedName("TerritoryName")
	private String territoryName;

	@SerializedName("PatchName")
	private String patchName;

	@SerializedName("StateId")
	private int stateId;

	@SerializedName("PatchId")
	private int patchId;

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

	public void setPatchName(String patchName){
		this.patchName = patchName;
	}

	public String getPatchName(){
		return patchName;
	}

	public void setStateId(int stateId){
		this.stateId = stateId;
	}

	public int getStateId(){
		return stateId;
	}

	public void setPatchId(int patchId){
		this.patchId = patchId;
	}

	public int getPatchId(){
		return patchId;
	}

	@Override
 	public String toString(){
		return 
			"Patches{" + 
			"territoryId = '" + territoryId + '\'' + 
			",territoryName = '" + territoryName + '\'' + 
			",patchName = '" + patchName + '\'' + 
			",stateId = '" + stateId + '\'' + 
			",patchId = '" + patchId + '\'' + 
			"}";
		}
}