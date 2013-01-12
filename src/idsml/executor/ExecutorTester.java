package idsml.executor;

import idsml.dsc.DSC;
import idsml.dsc.Type;
import idsml.event.Register;
import idsml.generator.NaiveGenerator;
import idsml.generator.Negotiate;
import idsml.model.IntentModel;
import idsml.procedure.ExecutionUnit;
import idsml.procedure.Procedure;
import idsml.repository.Repository;
import idsml.selector.NaiveSelector;
import idsml.selector.NaiveValidator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.codehaus.commons.compiler.CompileException;

public class ExecutorTester {
	public static void main (String[] args){
		
		String boilerplateInclude = "import idsml.executor.call.*; import idsml.executor.*; import idsml.dsc.*; import idsml.statemanager.*;import idsml.repository.*;import idsml.model.*;";
		
		// Set up DSCs

		DSC firstDSC = new DSC("First", Type.OPER);
		DSC secondDSC = new DSC("Second", Type.OPER);

		ArrayList<DSC> dependencies1 = new ArrayList<DSC>();	
		dependencies1.add(secondDSC);
		
		ArrayList<DSC> dependencies2 = new ArrayList<DSC>();	
		
		Procedure procedure1 = new Procedure("0001", "SendBasic1", firstDSC, dependencies1);
		Procedure procedure2 = new Procedure("0002", "Encrypt1", secondDSC, dependencies2);
		
		String p1c1 = boilerplateInclude +
				"System.out.println(5 + 5);" +
				"idsml.statemanager.StateManager stateManager = idsml.statemanager.StateManager.getInstance();" +
				"Attribute att = new Attribute(\"testAttribute\", (Object)\"This string is being stored as the value of the attribute\");" +
				"System.out.println(\"Value set in state manager\");" +
				"stateManager.putAttribute(att);" +
				"DSC secondDSC = new DSC(\"Second\", Type.OPER);" +
				"return new DSCCall(secondDSC, \"finalEU\");";
		
		String p1c2 = boilerplateInclude +
				"System.out.println(30 + 30);" +
				"idsml.statemanager.StateManager stateManager = idsml.statemanager.StateManager.getInstance();" +
				"Attribute att = new Attribute(\"testAttribute\", (Object)\"This string is being stored as the value of the attribute\");" +
				"System.out.println(\"Checking state manager for value\");" +
				"if (stateManager.hasAttribute(\"testAttribute\"))" +
				"System.out.println(\"Failure: Value not cleared\");" +
				" else " +
				"System.out.println(\"Success: Value cleared\");" +
				"return null;";
		
		ExecutionUnit start = new ExecutionUnit("start", p1c1);
		ExecutionUnit finalEU = new ExecutionUnit("finalEU", p1c2);
		
		procedure1.setStartEU(start);
		procedure1.addExecutionUnit(finalEU);
		
		
		String p2c1 = boilerplateInclude +
				"System.out.println(10 + 10);" +
				"idsml.statemanager.StateManager stateManager = idsml.statemanager.StateManager.getInstance();" +
				"System.out.println((String)stateManager.getAttribute(\"testAttribute\").getValue());" +
				"return new EventWaitCall(\"testevent\", \"third\");";
		
		// return new EUCall(\"third\");
		
		String p2c2 = boilerplateInclude +
				"System.out.println(20 + 10);" +
				"return new EUCall(\"fourth\");";
		
		String p2c3 = boilerplateInclude +
				"System.out.println(30 + 10);" +
				"idsml.statemanager.StateManager stateManager = idsml.statemanager.StateManager.getInstance();" +
				"System.out.println(\"Clearing state information\");" +
				"stateManager.clearAttribute(\"testAttribute\");" +
				"return new EUCall(\"loop\");";
		
		String p2c4 = boilerplateInclude +
				"System.out.println(30 + 20);" +
				"idsml.statemanager.StateManager stateManager = idsml.statemanager.StateManager.getInstance();" +
				"int counter = 0;" +
				"if (stateManager.hasAttribute(\"counter\")) " +
				" counter = ((Integer)stateManager.getAttribute(\"counter\").getValue()) + 1;" +
				"if (counter > 50) " +
				"return null;" +
				" else " +
				"System.out.println(\"Counting...: \" + counter);" +
				"stateManager.putAttribute(new Attribute(\"counter\", new Integer(counter)));" +
				"return new EUCall(\"loop\");";
		
		ExecutionUnit second = new ExecutionUnit("second", p2c1);
		ExecutionUnit third = new ExecutionUnit("third", p2c2);
		ExecutionUnit fourth = new ExecutionUnit("fourth", p2c3);
		ExecutionUnit fifth = new ExecutionUnit("loop", p2c4);
		
		procedure2.setStartEU(second);
		procedure2.addExecutionUnit(third);
		procedure2.addExecutionUnit(fourth);
		procedure2.addExecutionUnit(fifth);
		
		/*
		public String getLastStatement(){
			return statement;
		}
		
		private ArrayList<ExecutionUnit> getExecutionUnits(Procedure procedure){
			String server = "textr.us", db = "fiu_test", user = "fiu_test", password = "fiu_test";
			Connector connector = new MySQLConnector();
			
			connector.connect(user, password, server, db);
			ArrayList<ExecutionUnit> executionUnits = connector.geExecutionUnits(procedure);
			connector.disconnect();
			return executionUnits;
		}
		
		public void executeProcedure(DSC dsc){
			currentProcedure = Mapper.getProcedureForDSC(dsc);
			executionUnits = getExecutionUnits(currentProcedure);
			statement = executionUnits.get(0).getBody();
			try {
				executeStatement(statement);
			} catch (CompileException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
		}
		*/
		
		Repository.addProcedure(procedure1);

		Repository.addProcedure(procedure2);
		
		//Set up an initial DSC matching a command.
		DSC initialDSC = firstDSC;
		
		// Find all models which match command
		ArrayList<IntentModel> matchingModels = (new NaiveGenerator()).generateModels(initialDSC);

		// Find valid models based on user preferences
		ArrayList<IntentModel> validModels = (new NaiveValidator()).validateModels(matchingModels, firstDSC);

		// Find the best model based on cost
		IntentModel bestModel = (new NaiveSelector()).getBestModel(validModels);
		
		
		System.out.println(bestModel.getAllDependencies());
		
		// Format and print report
		System.out.println("We generated " + matchingModels.size() + " models");
		System.out.println();
		System.out.println(validModels.size() + " are valid based on user preferences");
		System.out.println();
		if (validModels.size() > 0)
			System.out.println("The best model is:\n" + bestModel.prettyPrinter());

		
		System.out.println("\n\nThe full list of models:\n");
		for (int i = 0; i < matchingModels.size(); i++){
			System.out.println(matchingModels.get(i).prettyPrinter());
		}
		
		System.out.println("Beginnning Model Execution");
		
		try {
			(new Executor()).executeModel(bestModel, new Negotiate("System.out.println(\"Precondition executing...\"); return true;"));
		} catch (CompileException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		
		// Simulate event received.
		System.out.println("Event \"testevent\" received");
		try {
			(new Executor()).executeModel(Register.getRegisteredEventCallBack("testevent"));
		} catch (CompileException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
