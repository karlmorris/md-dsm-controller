package idsml.executor.call;

public class EUCall extends Call {
	
	String eUId;
	
	public EUCall(String eUId){
		this.eUId = eUId;
	}
	
	public String getEUId(){
		return eUId;
	}
}
