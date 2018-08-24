package com.planera.mis.planera2.activities.models;

import com.google.gson.annotations.SerializedName;

public class Brands{

	@SerializedName("IsBrand")
	private String isBrand;

	@SerializedName("IsDelete")
	private String isDelete;

	@SerializedName("ProductId")
	private int productId;

	@SerializedName("Name")
	private String name;

	public void setIsBrand(String isBrand){
		this.isBrand = isBrand;
	}

	public String getIsBrand(){
		return isBrand;
	}

	public void setIsDelete(String isDelete){
		this.isDelete = isDelete;
	}

	public String getIsDelete(){
		return isDelete;
	}

	public void setProductId(int productId){
		this.productId = productId;
	}

	public int getProductId(){
		return productId;
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
			"Brands{" + 
			"isBrand = '" + isBrand + '\'' + 
			",isDelete = '" + isDelete + '\'' + 
			",productId = '" + productId + '\'' + 
			",name = '" + name + '\'' + 
			"}";
		}
}