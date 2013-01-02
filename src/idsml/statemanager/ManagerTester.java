package idsml.statemanager;

public class ManagerTester {
	public static void main(String args[]){
		StateManager stateManager = StateManager.getInstance();
		Attribute att = new Attribute("testAttribute", (Object)"This string is being stored as the value of the attribute");
		stateManager.putAttribute(att);
	}
}
