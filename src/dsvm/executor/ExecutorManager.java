package dsvm.executor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.codehaus.commons.compiler.CompileException;

import dsvm.dsc.DSC;
import dsvm.dsc.Type;
import dsvm.event.EventRegister;
import dsvm.generator.NaiveGenerator;
import dsvm.generator.Negotiate;
import dsvm.model.IntentModel;
import dsvm.procedure.ExecutionUnit;
import dsvm.procedure.Procedure;
import dsvm.repository.Repository;
import dsvm.selector.NaiveSelector;
import dsvm.selector.NaiveValidator;
import dsvm.statemanager.Attribute;

public class ExecutorManager {
	
	private static ExecutorManager instance = null;
	
	public void executeScript(String controlScript) {
		
		ScriptParser parser = new ScriptParser(controlScript);
		
		// Load state manager with parsed data from script
		dsvm.statemanager.StateManager stateManager = dsvm.statemanager.StateManager.getInstance();
		
		for (int i = 0; i < parser.getParameters().size(); i++ )
			stateManager.putAttribute(parser.getParameters().get(i));
		
		stateManager.putAttribute(new Attribute("plainTextString", "Howdy, partner"));
		
		DSC initialDSC = parser.getCommandClassifier();
		
		DSC validationDSC = new DSC("Encrypt", Type.OPER);

		
		// Find all models which match command
		ArrayList<IntentModel> matchingModels = (new NaiveGenerator()).generateModels(initialDSC);

		// Find valid models based on user preferences
		ArrayList<IntentModel> validModels = (new NaiveValidator()).validateModels(matchingModels, validationDSC);

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
		
		long startTime = 0, runTime = 0;
		
		startTime = System.currentTimeMillis();
		
		try {
			(new Executor()).executeModel(bestModel, new Negotiate("System.out.println(\"Precondition executing...\"); return true;"));
		} catch (CompileException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		runTime = System.currentTimeMillis() - startTime;
		
		System.out.println("Total running time: " + runTime);
	}
	
	public void handleEvent(String event) {
		System.out.println("Event \"" + event + "\" received");
		try {
			(new Executor()).executeModel(EventRegister.getRegisteredEventCallBack(event));
		} catch (CompileException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static ExecutorManager getInstance(){
		if (instance == null)
			instance = new ExecutorManager();
		if (Repository.getInstance().isEmpty())
			loadProcedures();
		
		return instance;
	}

	private static void loadProcedures(){
		
		String boilerplateInclude = "import dsvm.executor.call.*; import dsvm.executor.*; import dsvm.dsc.*; import dsvm.statemanager.*;import dsvm.repository.*;import dsvm.model.*;";
		
		// Set up DSCs

		DSC sendDSC = new DSC("Send", Type.OPER);
		DSC encryptDSC = new DSC("Encrypt", Type.OPER);
		DSC loopDSC = new DSC("Loop", Type.OPER);

		ArrayList<DSC> dependencies1 = new ArrayList<DSC>();	
		dependencies1.add(encryptDSC);
		
		ArrayList<DSC> dependencies2 = new ArrayList<DSC>();	
		
		
		ArrayList<DSC> dependencies3 = new ArrayList<DSC>();	
		dependencies3.add(loopDSC);
		
		Procedure procedure1 = new Procedure("0001", "SendBasic", sendDSC, dependencies3);
		Procedure procedure2 = new Procedure("0002", "Looper", loopDSC, dependencies2);
		Procedure procedure3 = new Procedure("0003", "SendSecure", sendDSC, dependencies1);
		Procedure procedure4 = new Procedure("0004", "BasicEncrypt", encryptDSC, dependencies2);
		
		String p1c1 = boilerplateInclude +
				"System.out.println(5 + 5);" +
				"dsvm.statemanager.StateManager stateManager = dsvm.statemanager.StateManager.getInstance();" +
				"Attribute att = new Attribute(\"testAttribute\", (Object)\"This string is being stored as the value of the attribute\");" +
				"System.out.println(\"Value set in state manager\");" +
				"stateManager.putAttribute(att);" +
				"DSC secondDSC = new DSC(\"Encrypt\", Type.OPER);" +
				"return new DSCCall(secondDSC, \"finalEU\");";
		
		String p1c2 = boilerplateInclude +
				"System.out.println(30 + 30);" +
				"dsvm.statemanager.StateManager stateManager = dsvm.statemanager.StateManager.getInstance();" +
				"Attribute att = new Attribute(\"testAttribute\", (Object)\"This string is being stored as the value of the attribute\");" +
				"System.out.println(\"Checking state manager for value...\");" +
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
				"dsvm.statemanager.StateManager stateManager = dsvm.statemanager.StateManager.getInstance();" +
				"System.out.println((String)stateManager.getAttribute(\"testAttribute\").getValue());" +
				"dsvm.broker.I_Manager_Stub.APICall(\"API Call Data\");" +
				"return new EventWaitCall(\"EVENT_FILE_SENT\", \"third\");";
		
		// return new EUCall(\"third\");
		
		String p2c2 = boilerplateInclude +
				"System.out.println(20 + 10);" +
				"return new EUCall(\"fourth\");";
		
		String p2c3 = boilerplateInclude +
				"System.out.println(30 + 10);" +
				"dsvm.statemanager.StateManager stateManager = dsvm.statemanager.StateManager.getInstance();" +
				"System.out.println(\"Clearing state information\");" +
				"stateManager.clearAttribute(\"testAttribute\");" +
				"return new EUCall(\"loop\");";
		
		String p2c4 = boilerplateInclude +
				"System.out.println(30 + 20);" +
				"dsvm.statemanager.StateManager stateManager = dsvm.statemanager.StateManager.getInstance();" +
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
		
		
		String secure1Code = boilerplateInclude +
				"DSC encryptDSC = new DSC(\"Encrypt\", Type.OPER);" +
				"return new DSCCall(encryptDSC, \"encryptReturn\");";
		String secure2Code = boilerplateInclude +
				"dsvm.statemanager.StateManager stateManager = dsvm.statemanager.StateManager.getInstance();" +
				"String encryptedString = (String)stateManager.getAttribute(\"encryptedTextString\").getValue();" +
				"dsvm.broker.I_Manager_Stub.APICall(encryptedString);" +
				"return new EventWaitCall(\"EVENT_FILE_SENT\", \"fileSent\");";
		String secure3Code = boilerplateInclude +
				"System.out.println(\"File sent sucessfully\");" +
				"return null;";
		ExecutionUnit secure1 = new ExecutionUnit("first", secure1Code);
		ExecutionUnit secure2 = new ExecutionUnit("encryptReturn", secure2Code);
		ExecutionUnit secure3 = new ExecutionUnit("fileSent", secure3Code);
		
		procedure3.setStartEU(secure1);
		procedure3.addExecutionUnit(secure2);
		procedure3.addExecutionUnit(secure3);
		
		
		String encrypt1 = boilerplateInclude + "import org.jasypt.util.text.*;" +
				"BasicTextEncryptor textEncryptor = new BasicTextEncryptor();" +
				"dsvm.statemanager.StateManager stateManager = dsvm.statemanager.StateManager.getInstance();" +
				"textEncryptor.setPassword(\"myencryptionpassword\");" +
				"String encryptedString = encryptedString = textEncryptor.encrypt((String)stateManager.getAttribute(\"plainTextString\").getValue());" +
				"Attribute att = new Attribute(\"encryptedTextString\", (Object) encryptedString);" +
				"stateManager.putAttribute(att);" +
				"return null;";
		
		ExecutionUnit encrypt = new ExecutionUnit("encryptEU", encrypt1);
		procedure4.setStartEU(encrypt);
		
		Repository.getInstance().addProcedure(procedure1);
		Repository.getInstance().addProcedure(procedure2);
		Repository.getInstance().addProcedure(procedure3);
		Repository.getInstance().addProcedure(procedure4);
	}
}
