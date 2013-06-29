package dsvm.statemanager;

import java.util.HashMap;


/**
 * 
 * @author karl
 * 
 * Basic state manager. No facilities for permission or role access. 
 */

public class StateManager {
	
	private HashMap<String, Attribute> attributes = new HashMap<String, Attribute>();
	
	private static StateManager manager;
	
	public void putAttribute(Attribute attribute){
		putAttribute(attribute.getName(), attribute);
	}
	
	public boolean hasAttribute(String name){
		return attributes.containsKey(name);
	}
	
	public void putAttribute(String name, Attribute attribute){
		attributes.put(name, attribute);
	}
	
	public Attribute getAttribute (String name){
		return attributes.get(name);
	}
	
	public void clearAttribute (String name){
		attributes.remove(name);
	}
	
	private StateManager() {};
	
	public static synchronized StateManager getInstance(){
		if (manager == null)
			manager = new StateManager();
		return manager;
	}
}
