package idsml.event;

import java.util.HashMap;
import java.util.Map;

public class Register {
	
	
	static Map<String, String> register = new HashMap<String, String>();
	
	public static void registerEventListener(String event, String eUId){
		register.put(event, eUId);
	}
	
	public static void deregisterEventListener(String event){
		register.remove(event);
	}
	
	public static boolean hasEventListenerRegistered(String event){
		return register.containsKey(event);
	}
}
