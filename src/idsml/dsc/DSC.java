package idsml.dsc;

import java.util.ArrayList;

public class DSC {
	String name;
	Type type;
	String prefix = "";
	ArrayList<DSC> parameters = new ArrayList<DSC>();

	public DSC(String name, Type type, String prefix, ArrayList<DSC> parameters){
		this.name = name;
		this.type = type;
		this.prefix = prefix;
		this.parameters = parameters;
	}
	
	public DSC(String name, Type type, String prefix){
		this.name = name;
		this.type = type;
		this.prefix = prefix;
	}
	
	public DSC(String name, Type type){
		this.name = name;
		this.type = type;
		this.prefix = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public ArrayList<DSC> getParameters() {
		return parameters;
	}

	public void setParameters(ArrayList<DSC> parameters) {
		this.parameters = parameters;
	}
	
	@Override
	public boolean equals(Object o){
		
		DSC currentDSC = (DSC) o;
		if ((this.prefix.equals(currentDSC.getPrefix())) && (this.name.equals(currentDSC.getName())))
			return true;
		else
			return false;
	}

}
