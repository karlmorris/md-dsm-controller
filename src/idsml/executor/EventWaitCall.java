package idsml.executor;

public class EventWaitCall extends Call {
	String eUId;
	String event;
	
	public EventWaitCall(String event, String eUId){
		this.event = event;
		this.eUId = eUId;
	}
	
	public String getEUId(){
		return eUId;
	}
	
	public String getEvent(){
		return event;
	}
}