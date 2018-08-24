package com.planera.mis.planera2.activities.models;

import com.google.gson.annotations.SerializedName;

public class GiftsData {

	@SerializedName("GiftId")
	private int giftId;

	@SerializedName("IsDelete")
	private String isDelete;

	@SerializedName("Name")
	private String name;

	public void setGiftId(int giftId){
		this.giftId = giftId;
	}

	public int getGiftId(){
		return giftId;
	}

	public void setIsDelete(String isDelete){
		this.isDelete = isDelete;
	}

	public String getIsDelete(){
		return isDelete;
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
			"GiftsData{" +
			"giftId = '" + giftId + '\'' + 
			",isDelete = '" + isDelete + '\'' + 
			",name = '" + name + '\'' + 
			"}";
		}
}