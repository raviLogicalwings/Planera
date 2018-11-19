package com.planera.mis.planera2.models.GooglePlacesModel;

import com.google.gson.annotations.SerializedName;

public class CandidatesItem{

	@SerializedName("formatted_address")
	private String formattedAddress;

	@SerializedName("name")
	private String name;

	@SerializedName("opening_hours")
	private OpeningHours openingHours;

	@SerializedName("rating")
	private int rating;

	@SerializedName("geometry")
	private Geometry geometry;

	public void setFormattedAddress(String formattedAddress){
		this.formattedAddress = formattedAddress;
	}

	public String getFormattedAddress(){
		return formattedAddress;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setOpeningHours(OpeningHours openingHours){
		this.openingHours = openingHours;
	}

	public OpeningHours getOpeningHours(){
		return openingHours;
	}

	public void setRating(int rating){
		this.rating = rating;
	}

	public int getRating(){
		return rating;
	}

	public void setGeometry(Geometry geometry){
		this.geometry = geometry;
	}

	public Geometry getGeometry(){
		return geometry;
	}

	@Override
 	public String toString(){
		return 
			"CandidatesItem{" + 
			"formatted_address = '" + formattedAddress + '\'' + 
			",name = '" + name + '\'' + 
			",opening_hours = '" + openingHours + '\'' + 
			",rating = '" + rating + '\'' + 
			",geometry = '" + geometry + '\'' + 
			"}";
		}
}