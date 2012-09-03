package idsml.model;

import idsml.procedure.Procedure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Model {
	String name;
	Procedure init;
	Map<String, ArrayList<Procedure>> procedures = new HashMap<String, ArrayList<Procedure>>();
	
	
	public Model(){}
	
	public Model (Procedure init){
		this.init = init;
	}
	
	public void setInit(Procedure p){
		this.init = p;
	}
	
	public Procedure getInit(){
		return this.init;
	}
	
	public ArrayList<Procedure> getDependencies(String node){
		return procedures.get(node);
	}
	
	public void addDependency(String node, Procedure dependency){
		if (procedures.containsKey(node)) {
			procedures.get(node).add(dependency);
		} else {
			ArrayList<Procedure> dependencies = new ArrayList<Procedure>();
			dependencies.add(dependency);
			procedures.put(node, dependencies);
		}
	}
	
	public Map<String, ArrayList<Procedure>> getAllDependencies(){
		return procedures;
	}
	
	public void addAllDependencies(Map<String, ArrayList<Procedure>> dependencies){
		procedures.putAll(dependencies);
	}
	
	public String prettyPrinter(){
		String modelString = "";
		modelString += "Starting Procedure: " + this.getInit().getId() + " : " + this.getInit().getName() + "\n";
		Iterator<String> keyIterator = procedures.keySet().iterator();
		while (keyIterator.hasNext()){
			String currentKey = keyIterator.next();
			modelString += "\n\t Procedure " + currentKey + " has dependencies:\n";
			for (int i = 0; i < procedures.get(currentKey).size(); i++){
				modelString += "\t\t" + procedures.get(currentKey).get(i).getId() + ":" + procedures.get(currentKey).get(i).getName()
						+ " which is of type " + procedures.get(currentKey).get(i).getClassifier().getName() + "\n";
			}
			modelString += "\n";
		}
		return modelString;
	}
}
