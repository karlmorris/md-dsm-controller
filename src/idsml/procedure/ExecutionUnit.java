package idsml.procedure;

import java.io.Serializable;

public class ExecutionUnit implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 554738223676867815L;
	
	String id;
	String body = null;
	
	public ExecutionUnit(String id, String body){
		this.id = id;
		this.body = body;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
}
