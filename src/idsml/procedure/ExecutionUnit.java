package idsml.procedure;

public class ExecutionUnit {
	int id;
	String body = null;
	
	public ExecutionUnit(int id, String body){
		this.id = id;
		this.body = body;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
}
