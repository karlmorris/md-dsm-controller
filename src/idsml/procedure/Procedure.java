package idsml.procedure;

import idsml.dsc.DSC;

import java.util.ArrayList;

public class Procedure {
	String id;
	String name;
	DSC classifier;
	String startEU;
	ArrayList<DSC> dependency = new ArrayList<DSC>();
	
	public Procedure(String id, String name, DSC classifier, String startEU,
			ArrayList<DSC> dependency) {
		super();
		this.id = id;
		this.name = name;
		this.classifier = classifier;
		this.startEU = startEU;
		this.dependency = dependency;
	}
	
	public Procedure(String id, String name, DSC classifier, String startEU) {
		super();
		this.id = id;
		this.name = name;
		this.classifier = classifier;
		this.startEU = startEU;
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
	public String getStartEU() {
		return startEU;
	}
	public void setStartEU(String startEU) {
		this.startEU = startEU;
	}
	public ArrayList<DSC> getDependency() {
		return dependency;
	}
	public void setDependency(ArrayList<DSC> dependency) {
		this.dependency = dependency;
	}

}