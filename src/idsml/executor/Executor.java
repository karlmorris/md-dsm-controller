package idsml.executor;
import idsml.dsc.DSC;
import idsml.executor.call.Call;
import idsml.executor.call.DSCCall;
import idsml.executor.call.EUCall;
import idsml.executor.call.EventWaitCall;
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
	
	Model model;
	Procedure currentProcedure;
	ArrayList<ExecutionUnit> executionUnits = null;
	
	StateManager stateManager = StateManager.getInstance();
	
	String statement = "";
	
	UUID id;
	
	public Executor(){
		id = UUID.randomUUID();
	}
	
	public void executeModel(Model intentModel) throws CompileException, InvocationTargetException{
		
		this.model = intentModel;
		
		
		
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

		while (((result != null) || init.getId() != currentProcedure.getId()) && (stateManager.hasAttribute(id.toString()))){
			if (result instanceof DSCCall){
				System.out.println("DSC to call: " + ((DSCCall)result).getDSC().getName());
				ArrayList<CallBack> callBacks;
				if (stateManager.hasAttribute(model.getId()))
					callBacks = (ArrayList<CallBack>) stateManager.getAttribute(model.getId()).getValue();
				else
					callBacks = new ArrayList<CallBack>();
				callBacks.add(new CallBack(currentProcedure.getClassifier(), ((DSCCall)result).getDSC(), ((DSCCall)result).getEUId()));
				stateManager.putAttribute(new Attribute(model.getId(), callBacks));
				currentProcedure = model.getProcedure(((DSCCall)result).getDSC());
				result = executeStatement(currentProcedure.getStartEU().getBody());
			} else if (result instanceof EUCall){
				System.out.println("EU to call: " + ((EUCall)result).getEUId());
				result = executeStatement(currentProcedure.getExecutionUnit(((EUCall)result).getEUId()).getBody());
			} else if (result instanceof EventWaitCall){
				System.out.println("Will register: " + ((EventWaitCall)result).getEUId() + " in response to " + ((EventWaitCall)result).getEvent());
			} else {
				
				ArrayList<CallBack> currentCallbacks = ((ArrayList<CallBack>)stateManager.getAttribute(model.getId()).getValue());
				for (int i = 0; i < currentCallbacks.size(); i++){
					CallBack callback = currentCallbacks.get(i);
					if (callback.getCalledProcedureDSC().equals(currentProcedure.getClassifier())){
						currentCallbacks.remove(i);
						currentProcedure = model.getProcedure(callback.callingProcedureDSC);
						result = executeStatement(model.getProcedure(callback.getCallingProcedureDSC()).getExecutionUnit(callback.getEUId()).getBody());
					}
				}
				
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

class CallBack{
	String eUId;
	DSC callingProcedureDSC, calledProcedureDSC;
	
	public CallBack(DSC callingdsc, DSC calleddsc, String eUId){
		this.callingProcedureDSC = callingdsc;
		this.calledProcedureDSC = calleddsc;
		this.eUId = eUId;
	}

	public String getEUId() {
		return eUId;
	}

	public void setEUId(String eUId) {
		this.eUId = eUId;
	}

	public DSC getCallingProcedureDSC() {
		return callingProcedureDSC;
	}

	public void setCallingProcedureDSC(DSC procedureDSC) {
		this.callingProcedureDSC = procedureDSC;
	}

	public DSC getCalledProcedureDSC() {
		return calledProcedureDSC;
	}

	public void setCalledProcedureDSC(DSC calledProcedureDSC) {
		this.calledProcedureDSC = calledProcedureDSC;
	}
	
}
