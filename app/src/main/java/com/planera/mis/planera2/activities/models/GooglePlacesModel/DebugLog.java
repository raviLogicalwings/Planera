package com.planera.mis.planera2.activities.models.GooglePlacesModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DebugLog{

	@SerializedName("line")
	private List<Object> line;

	public void setLine(List<Object> line){
		this.line = line;
	}

	public List<Object> getLine(){
		return line;
	}

	@Override
 	public String toString(){
		return 
			"DebugLog{" + 
			"line = '" + line + '\'' + 
			"}";
		}
}