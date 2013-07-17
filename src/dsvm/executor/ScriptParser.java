package dsvm.executor;

import java.util.ArrayList;

import dsvm.dsc.DSC;
import dsvm.dsc.Type;
import dsvm.statemanager.Attribute;

public class ScriptParser {
	String script;
	
	public ScriptParser (String script){
		this.script = script;
	}
	
	public DSC getCommandClassifier(){
		DSC sendDSC = new DSC("Send", Type.OPER);
		return sendDSC;
	}
	
	public ArrayList<Attribute> getParameters(){
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute("fileName", "testFile.txt"));
		return attributes;
	}
}
