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
	
	final String boilerplateInclude = "import dsvm.executor.call.*; import dsvm.executor.*; import dsvm.dsc.*; import dsvm.statemanager.*;import dsvm.repository.*;import dsvm.model.*;";
	
	String id;
	String body = null;
	ScriptEvaluator script = new ScriptEvaluator();
	
	public ExecutionUnit(String id, String body){
		setId(id);
		setBody(boilerplateInclude + body);
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
			script.setReturnType(Call.class);
			script.cook(body);
		} catch (CompileException e) {
			e.printStackTrace();
		}
	}
}
