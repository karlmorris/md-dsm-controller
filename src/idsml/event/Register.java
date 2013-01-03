package idsml.event;

import idsml.executor.EventCallBack;

import java.util.HashMap;
import java.util.Map;

public class Register {
	
	
	static Map<String, EventCallBack> register = new HashMap<String, EventCallBack>();
	
	public static void registerEventListener(String event, EventCallBack callback){
		register.put(event, callback);
	}
	
	public static void deregisterEventListener(String event){
		register.remove(event);
	}
	
	public static boolean hasEventListenerRegistered(String event){
		return register.containsKey(event);
	}
}
