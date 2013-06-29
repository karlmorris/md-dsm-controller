package dsvm.procedure;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dsvm.dsc.DSC;

public class Procedure implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5184762094889726348L;
	
	
	String id;
	String name;
	DSC classifier;
	ExecutionUnit startEU;
	Map<String, ExecutionUnit> executionUnits = new HashMap<String, ExecutionUnit>();
	ArrayList<DSC> dependency = new ArrayList<DSC>();
	
	public Procedure(String id, String name, DSC classifier) {
		super();
		this.id = id;
		this.name = name;
		this.classifier = classifier;
	}
	
	public Procedure(String id, String name, DSC classifier, ArrayList<DSC> dependency) {
		super();
		this.id = id;
		this.name = name;
		this.classifier = classifier;
		this.dependency = dependency;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DSC getClassifier() {
		return classifier;
	}
	public void setClassifier(DSC classifier) {
		this.classifier = classifier;
	}
	public ExecutionUnit getStartEU() {
		return startEU;
	}
	public void setStartEU(ExecutionUnit startEU) {
		this.startEU = startEU;
	}
	public ArrayList<DSC> getDependency() {
		return dependency;
	}
	
	public void setDependency(ArrayList<DSC> dependency) {
		this.dependency = dependency;
	}
	
	public void addExecutionUnit(ExecutionUnit executionUnit){
		addExecutionUnit(executionUnit.id, executionUnit);
	}
	
	public void addExecutionUnit(String id, ExecutionUnit executionUnit){
		executionUnits.put(id, executionUnit);
	}
	
	public void addExecutionUnits(Map<String, ExecutionUnit> executionUnits){
		this.executionUnits = executionUnits;
	}
	
	public Map<String, ExecutionUnit> getExecutionUnits(){
		return executionUnits;
	}
	
	public ExecutionUnit getExecutionUnit(String id){
		if (startEU.getId().equals(id)) 
			return startEU;
		else
			return executionUnits.get(id);
	}

}