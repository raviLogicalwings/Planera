package com.planera.mis.planera2.activities.models;

import com.google.gson.annotations.SerializedName;

public class InputGift{

	@SerializedName("GiftId")
	private String giftId;

	@SerializedName("InputGiftId")
	private String inputGiftId;

	@SerializedName("InputId")
	private String inputId;

	@SerializedName("Quantity")
	private String quantity;

	@SerializedName("GiftName")
	private String giftName;

	@SerializedName("GiftQty")
	private String giftQuantity;


	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public String getGiftQuantity() {
		return giftQuantity;
	}

	public void setGiftQuantity(String giftQuantity) {
		this.giftQuantity = giftQuantity;
	}

	public void setGiftId(String giftId){
		this.giftId = giftId;
	}

	public String getGiftId(){
		return giftId;
	}

	public void setInputGiftId(String inputGiftId){
		this.inputGiftId = inputGiftId;
	}

	public String getInputGiftId(){
		return inputGiftId;
	}

	public void setInputId(String inputId){
		this.inputId = inputId;
	}

	public String getInputId(){
		return inputId;
	}

	public void setQuantity(String quantity){
		this.quantity = quantity;
	}

	public String getQuantity(){
		return quantity;
	}

	@Override
 	public String toString(){
		return 
			"InputGift{" + 
			"giftId = '" + giftId + '\'' + 
			",inputGiftId = '" + inputGiftId + '\'' + 
			",inputId = '" + inputId + '\'' + 
			",quantity = '" + quantity + '\'' + 
			"}";
		}
}