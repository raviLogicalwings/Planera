package com.planera.mis.planera2.activities.models;

import com.google.gson.annotations.SerializedName;

public class Orders{

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

	public void setInputId(int inputId){
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

	@Override
 	public String toString(){
		return 
			"Orders{" + 
			"inputId = '" + inputId + '\'' + 
			",interestedLevel = '" + interestedLevel + '\'' + 
			",quantity = '" + quantity + '\'' + 
			",productId = '" + productId + '\'' + 
			",isSample = '" + isSample + '\'' + 
			"}";
		}
}