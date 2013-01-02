package idsml.executor;

import idsml.dsc.DSC;
import idsml.dsc.Type;
import idsml.generator.NaiveGenerator;
import idsml.model.Model;
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
		
		
		Executor executor = new Executor();
		String boilerplateInclude = "import idsml.executor.call.*; import idsml.executor.*; import idsml.dsc.*; import idsml.statemanager.*;import idsml.repository.*;import idsml.model.*;";
		/*
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
				"return new DSCCall(new DSC(\"dummyDSC\", Type.OPER), \"EUIdToExecuteOnReturn\");";
		try {
			executor.executeStatement(command);
		} catch (CompileException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		*/
		
		// Set up DSCs

				DSC firstDSC = new DSC("First", Type.OPER);
				DSC secondDSC = new DSC("Second", Type.OPER);
				//DSC thirdDSC = new DSC("Third", Type.OPER);

				ArrayList<DSC> dependencies1 = new ArrayList<DSC>();	
				dependencies1.add(secondDSC);
				
				ArrayList<DSC> dependencies2 = new ArrayList<DSC>();	
				//dependencies2.add(thirdDSC);
				
				Procedure procedure1 = new Procedure("0001", "SendBasic1", firstDSC, dependencies1);
				//Procedure procedure2 = new Procedure("0002", "SendBasic2", firstDSC, new ExecutionUnit("send0001", ""), dependencies1);
				//Procedure procedure3 = new Procedure("0003", "SendBasic3", firstDSC, new ExecutionUnit("send0001", ""), dependencies1);
				Procedure procedure2 = new Procedure("0004", "Encrypt1", secondDSC, dependencies2);
				//Procedure procedure5 = new Procedure("0005", "Encrypt2", secondDSC, new ExecutionUnit("send0001", ""), dependencies2);
				//Procedure procedure6 = new Procedure("0006", "Encrypt3", secondDSC, new ExecutionUnit("send0001", ""), dependencies2);
				
				
				String command = boilerplateInclude +
						"System.out.println(5 + 5);" +
						"idsml.statemanager.StateManager stateManager = idsml.statemanager.StateManager.getInstance();" +
						"Attribute att = new Attribute(\"testAttribute\", (Object)\"This string is being stored as the value of the attribute\");" +
						"System.out.println(\"Value set in state manager\");" +
						"stateManager.putAttribute(att);" +
						"DSC secondDSC = new DSC(\"Second\", Type.OPER);" +
						"return new DSCCall(secondDSC, \"finalEU\");";
				
				String finalCommand = boilerplateInclude +
						"System.out.println(20 + 20);" +
						"idsml.statemanager.StateManager stateManager = idsml.statemanager.StateManager.getInstance();" +
						"Attribute att = new Attribute(\"testAttribute\", (Object)\"This string is being stored as the value of the attribute\");" +
						"System.out.println(\"Value set in state manager\");" +
						"if (stateManager.hasAttribute(\"testAttribute\"))" +
						"System.out.println(\"Failure: Value not cleared\");" +
						" else " +
						"System.out.println(\"Sucess: Value cleared\");" +
						"return null;";
				
				ExecutionUnit start = new ExecutionUnit("start", command);
				ExecutionUnit finalEU = new ExecutionUnit("finalEU", finalCommand);
				
				procedure1.setStartEU(start);
				procedure1.addExecutionUnit(finalEU);
				
				
				String secondCommand = boilerplateInclude +
						"System.out.println(10 + 10);" +
						"idsml.statemanager.StateManager stateManager = idsml.statemanager.StateManager.getInstance();" +
						"System.out.println((String)stateManager.getAttribute(\"testAttribute\").getValue());" +
						"return new EUCall(\"third\");";
				
				String thirdCommand = boilerplateInclude +
						"System.out.println(20 + 10);" +
						"idsml.statemanager.StateManager stateManager = idsml.statemanager.StateManager.getInstance();" +
						"System.out.println(\"Clearing state information\");" +
						"stateManager.clearAttribute(\"testAttribute\");" +
						"return null;";
				
				ExecutionUnit second = new ExecutionUnit("second", secondCommand);
				
				ExecutionUnit third = new ExecutionUnit("third", thirdCommand);
				
				procedure2.setStartEU(second);
				procedure2.addExecutionUnit(third.getId(), third);
				
				Repository.addProcedure(procedure1);

				Repository.addProcedure(procedure2);
				
				//Set up an initial DSC matching a command.
				DSC initialDSC = firstDSC;
				
				ArrayList<Model> matchingModels = null;
				ArrayList<Model> validModels = null;
				Model bestModel = null;
				
				// Find all models which match command
				matchingModels = (new NaiveGenerator()).generateModels(initialDSC);

				// Find valid models based on user preferences
				validModels = (new NaiveValidator()).validateModels(matchingModels, firstDSC);

				// Find the best model based on cost
				bestModel = (new NaiveSelector()).getBestModel(validModels);
				
				
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
					executor.executeModel(bestModel);
				} catch (CompileException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
	}
}
