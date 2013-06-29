package dsvm.procedure;


import java.io.Serializable;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.ScriptEvaluator;

import dsvm.executor.call.Call;

public class ExecutionUnit implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 554738223676867815L;
	
	String id;
	String body = null;
	ScriptEvaluator script = new ScriptEvaluator();
	
	public ExecutionUnit(String id, String body){
		setId(id);
		setBody(body);
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
	
	public ScriptEvaluator getCompiledBody(){
		return script;
	}
	
	public void setCompiledBody(ScriptEvaluator script){
		this.script = script;
	}

	public void setBody(String body) {
		this.body = body;
		try {
			script.cook(body);
			script.setReturnType(Call.class);
		} catch (CompileException e) {
			//e.printStackTrace();
		}
	}
	
}
