package com.planera.mis.planera2.models.GooglePlacesModel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GooglePlaces{

	@SerializedName("candidates")
	private List<CandidatesItem> candidates;

	@SerializedName("debug_log")
	private DebugLog debugLog;

	@SerializedName("status")
	private String status;

	public void setCandidates(List<CandidatesItem> candidates){
		this.candidates = candidates;
	}

	public List<CandidatesItem> getCandidates(){
		return candidates;
	}

	public void setDebugLog(DebugLog debugLog){
		this.debugLog = debugLog;
	}

	public DebugLog getDebugLog(){
		return debugLog;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"GooglePlaces{" + 
			"candidates = '" + candidates + '\'' + 
			",debug_log = '" + debugLog + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}