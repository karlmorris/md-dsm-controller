package dsvm.executor;

import java.util.ArrayList;

import dsvm.dsc.DSC;
import dsvm.dsc.Type;
import dsvm.statemanager.Attribute;

public class ScriptParser {
	String script;
	String command;
	
	
	public ScriptParser (String script){
		this.script = script;
		
		 
		
	}
	
	public ArrayList<DSC> getCommandClassifiers(){
		DSC sendDSC = new DSC("Send", Type.OPER);
		ArrayList<DSC> al = new ArrayList<DSC>();
		al.add(sendDSC);
		return al;
	}
	
	public ArrayList<ArrayList<Attribute>> getParameters(){
		ArrayList<ArrayList<Attribute>> al = new ArrayList<ArrayList<Attribute>>();
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		attributes.add(new Attribute("fileName", "testFile.txt"));
		al.add(attributes);
		return al;
	}
}
