package idsml.executor;

import idsml.dsc.DSC;

public class DSCCall extends Call {

	DSC dsc;
	
	public DSCCall (DSC dsc){
		this.dsc = dsc;
	}
	
	public DSC getDSC(){
		return dsc;
	}
}
