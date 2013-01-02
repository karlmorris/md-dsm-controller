package idsml.executor;

import idsml.dsc.DSC;

public class DSCCall extends Call {

	DSC dsc;
	String eUId;
	
	public DSCCall (DSC dsc, String eUId){
		this.dsc = dsc;
		this.eUId = eUId;
	}
	
	public DSC getDSC(){
		return dsc;
	}
	
	public String getEUId(){
		return eUId;
	}
}
