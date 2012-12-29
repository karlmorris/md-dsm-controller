package idsml.statemanager;

import java.util.HashMap;


/**
 * 
 * @author karl
 * 
 * Basic state manager. No facilities for permission or role access. 
 */

public class Manager {
	
	private HashMap<String, Attribute> attributes = new HashMap<String, Attribute>();
	
	private static Manager manager;
	
	public void putAttribute(Attribute attribute){
		putAttribute(attribute.getName(), attribute);
	}
	
	public void putAttribute(String name, Attribute attribute){
		attributes.put(name, attribute);
	}
	
	public Attribute getAttribute (String name){
		return attributes.get(name);
	}
	
	private Manager() {};
	
	public static synchronized Manager getInstance(){
		if (manager == null)
			manager = new Manager();
		return manager;
	}
}
