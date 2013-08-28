package dsvm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import dsvm.dsc.DSC;
import dsvm.procedure.Procedure;

public class IntentModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 566146086579827490L;
	
	UUID id;
	String name;
	Procedure init;
	Map<String, ArrayList<Procedure>> procedures = new HashMap<String, ArrayList<Procedure>>();
	
	
	public IntentModel(){
		id = UUID.randomUUID();
	}
	
	public IntentModel (Procedure init){
		this();
		this.init = init;
	}
	
	public String getId(){
		return id.toString();
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
	
	public void clearDependencies(){
		procedures.clear();
	}
	
	public Procedure getProcedure(DSC dsc){
		//First check if the initial procedure is a match
		if (this.init.getClassifier().equals(dsc))
			return this.init;
		// A model can only have a single procedure of a given DSC, therefore 1 to 1.
		Iterator<String> keyIterator = procedures.keySet().iterator();
		while (keyIterator.hasNext()){
			String currentKey = keyIterator.next();
			for (int i = 0; i < procedures.get(currentKey).size(); i++){
				if (procedures.get(currentKey).get(i).getClassifier().getName().equals(dsc.getName()))
						return procedures.get(currentKey).get(i);
			}
		}
		
		return null;
		
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
