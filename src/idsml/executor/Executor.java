package idsml.executor;
import idsml.dsc.DSC;
import idsml.event.Register;
import idsml.model.Model;
import idsml.procedure.ExecutionUnit;
import idsml.procedure.Procedure;
import idsml.repository.connector.Connector;
import idsml.repository.connector.MySQLConnector;
import idsml.statemanager.Attribute;
import idsml.statemanager.StateManager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.UUID;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.*;


public class Executor {
	
	Model currentModel;
	Procedure currentProcedure;
	ArrayList<ExecutionUnit> executionUnits = null;
	
	StateManager stateManager = StateManager.getInstance();
	
	String statement = "";
	
	UUID id;
	
	public Executor(){
		id = UUID.randomUUID();
	}
	
	public void executeModel(Model model) throws CompileException, InvocationTargetException{
		
		currentModel = model;
		
		
		
		Procedure init = model.getInit();
		Procedure currentProcedure = init;
		
		stateManager.putAttribute(new Attribute(id.toString(), init));
		
		//executionUnits = getExecutionUnits(init);
		
		//ExecutionUnit startEU = executionUnits.get(0); // Grab initial EU. Change to Map and init.getStartEU()
		
		ExecutionUnit startEU = init.getStartEU();
		
		Call result = null;
		try {
			result = executeStatement(startEU.getBody());
		} catch (CompileException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		/**
		 * TODO: Each model should have an event listener for the ID of the executing model, where the value is the DSC being executed
		 */

		while ((result != null) && (stateManager.hasAttribute(id.toString()))){
			if (result instanceof DSCCall){
				System.out.println("DSC to call: " + ((DSCCall)result).getDSC().getName());
				currentProcedure = currentModel.getProcedure(((DSCCall)result).getDSC());
				Register.registerEventListener(currentModel.getId(), currentProcedure.getClassifier().getName());
				result = executeStatement(currentProcedure.getStartEU().getBody());
			} else if (result instanceof EUCall){
				System.out.println("EU to call: " + ((EUCall)result).getEUId());
				result = executeStatement(currentProcedure.getExecutionUnit(((EUCall)result).getEUId()).getBody());
			} else if (result instanceof EventWaitCall){
				System.out.println("Will register: " + ((EventWaitCall)result).getEUId() + " in response to " + ((EventWaitCall)result).getEvent());
			}
		}
	}
	
	public Call executeStatement (String statement) throws InvocationTargetException, CompileException{
		this.statement = statement;
		ScriptEvaluator script = new ScriptEvaluator();
		script.setReturnType(Call.class);
		script.cook(statement);
		return (Call) script.evaluate(null);

	}
	
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

}
