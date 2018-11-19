package com.planera.mis.planera2.models;

import com.google.gson.annotations.SerializedName;

public class InputOrders {
	@SerializedName("InputProductId")
	private int inputProductId;

	@SerializedName("ProductName")
	private String productName;
//
	@SerializedName("IsBrand")
	private int isBrand;

	@SerializedName("InputId")
	private String inputId;

	@SerializedName("InterestedLevel")
	private String interestedLevel;

	@SerializedName("Quantity")
	private String quantity;

	@SerializedName("ProductId")
	private String productId;

	@SerializedName("IsSample")
	private String isSample;

	public void setInputId(String inputId){
		this.inputId = inputId;
	}

	public String getInputId(){
		return inputId;
	}

	public void setInterestedLevel(String interestedLevel){
		this.interestedLevel = interestedLevel;
	}

	public String getInterestedLevel(){
		return interestedLevel;
	}

	public void setQuantity(String quantity){
		this.quantity = quantity;
	}

	public String getQuantity(){
		return quantity;
	}

	public void setProductId(String productId){
		this.productId = productId;
	}

	public String getProductId(){
		return productId;
	}

	public void setIsSample(String isSample){
		this.isSample = isSample;
	}

	public String getIsSample(){
		return isSample;
	}

	public int getInputProductId() {
		return inputProductId;
	}

	public void setInputProductId(int inputProductId) {
		this.inputProductId = inputProductId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getIsBrand() {
		return isBrand;
	}

	public void setIsBrand(int isBrand) {
		this.isBrand = isBrand;
	}


	@Override
 	public String toString(){
		return 
			"InputOrders{" +
			"inputId = '" + inputId + '\'' + 
			",interestedLevel = '" + interestedLevel + '\'' + 
			",quantity = '" + quantity + '\'' + 
			",productId = '" + productId + '\'' + 
			",isSample = '" + isSample + '\'' + 
			"}";
		}
}