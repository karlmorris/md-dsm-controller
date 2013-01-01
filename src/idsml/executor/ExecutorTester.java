package idsml.executor;

import java.lang.reflect.InvocationTargetException;

import org.codehaus.commons.compiler.CompileException;

public class ExecutorTester {
	public static void main (String[] args){
		Executor executor = new Executor();
		
		
		String command = "import idsml.executor.*; import idsml.dsc.*; import idsml.statemanager.*;import idsml.repository.*;import idsml.model.*;" +
				"System.out.println(5 + 5);" +
				"idsml.statemanager.Manager stateManager = idsml.statemanager.Manager.getInstance();" +
				"Attribute att = new Attribute(\"testAttribute\", (Object)\"This string is being stored as the value of the attribute\");" +
				"stateManager.putAttribute(att);" +
				"return new EventWaitCall(\"idOfEvent\", \"idOfNextEU\");";
		
		try {
			executor.executeStatement(command);
		} catch (CompileException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		command = "import idsml.executor.*; import idsml.statemanager.*;import idsml.repository.*; import idsml.model.*; import idsml.dsc.*;" +
				"System.out.println(10 + 10);" +
				"idsml.statemanager.Manager stateManager = idsml.statemanager.Manager.getInstance();" +
				"System.out.println((String)stateManager.getAttribute(\"testAttribute\").getValue());" +
				"return new DSCCall(new DSC(\"dummyDSC\", Type.OPER));";
		
		executor = new Executor();
		try {
			executor.executeStatement(command);
		} catch (CompileException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
