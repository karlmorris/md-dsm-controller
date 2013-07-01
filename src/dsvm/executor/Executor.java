package dsvm.executor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.*;

import dsvm.dsc.DSC;
import dsvm.event.EventCallBack;
import dsvm.event.EventRegister;
import dsvm.executor.call.Call;
import dsvm.executor.call.DSCCall;
import dsvm.executor.call.EUCall;
import dsvm.executor.call.EventWaitCall;
import dsvm.generator.PostModelGenerationOperation;
import dsvm.model.IntentModel;
import dsvm.procedure.ExecutionUnit;
import dsvm.procedure.Procedure;
import dsvm.statemanager.Attribute;
import dsvm.statemanager.StateManager;


public class Executor {
	
	IntentModel model;
	Procedure currentProcedure;
	StateManager stateManager = StateManager.getInstance();
	
	UUID id;
	
	public Executor(){
		id = UUID.randomUUID();
	}
	
	public void executeModel(IntentModel intentModel, PostModelGenerationOperation preCond) throws CompileException, InvocationTargetException, ExecutionException, PreConditionException{
		if (preCond.execute())
			executeModel(intentModel);
		else
			throw new PreConditionException("Model execution precondition failed");
	}
	
	
	public void executeModel(IntentModel intentModel) throws CompileException, InvocationTargetException{
		stateManager.putAttribute(new Attribute(intentModel.getId(), intentModel));
		executeModel(new EventCallBack(intentModel.getId(), intentModel.getInit().getClassifier(), intentModel.getInit().getStartEU().getId()));
	}
	
	@SuppressWarnings("unchecked")
	public void executeModel(EventCallBack modelCallback) throws CompileException, InvocationTargetException{
		
		this.model = (IntentModel)stateManager.getAttribute(modelCallback.getModelId()).getValue();
		
		String stateManagerCallbackPrefix = "callbacks:";
		
		Procedure init = model.getInit();
		Procedure currentProcedure = model.getProcedure(modelCallback.getCallbackProcedureDSC());;
		
		stateManager.putAttribute(new Attribute(id.toString(), init));

		ExecutionUnit startEU = currentProcedure.getExecutionUnit(modelCallback.geteUId());
		
		Call result = executeStatement(startEU.getCompiledBody());
		
		/**
		 * TODO: Each model should have an event listener for the ID of the executing model, where the value is the DSC being executed
		 */

		while (((result != null) || init.getId() != currentProcedure.getId()) && (stateManager.hasAttribute(id.toString()))){

			if (result instanceof DSCCall){
				// If the result of the last EU execution was a DSC call
				System.out.println("DSC to call: " + ((DSCCall)result).getDSC().getName());
				ArrayList<CallBack> callBacks;
				if (stateManager.hasAttribute(stateManagerCallbackPrefix + model.getId()))
					callBacks = (ArrayList<CallBack>) stateManager.getAttribute(stateManagerCallbackPrefix + model.getId()).getValue();
				else
					callBacks = new ArrayList<CallBack>();
				
				// Register callback in statemanager
				callBacks.add(new CallBack(currentProcedure.getClassifier(), ((DSCCall)result).getDSC(), ((DSCCall)result).getEUId()));
				stateManager.putAttribute(new Attribute(stateManagerCallbackPrefix + model.getId(), callBacks));
				currentProcedure = model.getProcedure(((DSCCall)result).getDSC());
				// Execute initial EU in called procedure
				result = executeStatement(currentProcedure.getStartEU().getCompiledBody());
			} else if (result instanceof EUCall){
				// If the result of the last EU execution was a EU call
				System.out.println("EU to call: " + ((EUCall)result).getEUId());
				result = executeStatement(currentProcedure.getExecutionUnit(((EUCall)result).getEUId()).getCompiledBody());
			} else if (result instanceof EventWaitCall){
				// If the result of the last EU execution was a call to wait for a specific event
				System.out.println("Will register: " + ((EventWaitCall)result).getEUId() + " in response to " + ((EventWaitCall)result).getEvent());
				// Store model in state manager for recall when event occurs
				stateManager.putAttribute(new Attribute(model.getId(), model));
				// Register requested event in event listener with current procedure and stated EU as responsible model elements
				EventRegister.registerEventListener(((EventWaitCall)result).getEvent(), new EventCallBack(model.getId(), currentProcedure.getClassifier(), ((EventWaitCall)result).getEUId()));
				// Set values to exit execution loop.
				result = null;
				currentProcedure = init;
			} else {
				// If the result of the last EU execution was null, indicating completion of the procedure
				ArrayList<CallBack> currentCallbacks = ((ArrayList<CallBack>)stateManager.getAttribute(stateManagerCallbackPrefix + model.getId()).getValue());
				for (int i = 0; i < currentCallbacks.size(); i++){
					CallBack callback = currentCallbacks.get(i);
					if (callback.getCalledProcedureDSC().equals(currentProcedure.getClassifier())){
						// If a callback exists for the specified procedure completion
						// If none exists, the initial procedure has completed execution
						
						// Remove the callback to ensure it cannot be called again
						currentCallbacks.remove(i);
						// Set the current procedure to the calling procedure 
						currentProcedure = model.getProcedure(callback.callingProcedureDSC);
						// Execute specified callback EU in the calling procedure
						result = executeStatement(model.getProcedure(callback.getCallingProcedureDSC()).getExecutionUnit(callback.getEUId()).getCompiledBody());
					}
				}
			}
		}
	}
	
	public void executeProcedure(Procedure procedure) throws CompileException, InvocationTargetException{
		IntentModel model = new IntentModel(procedure);
		executeModel(model);	
	}
	
	// Compile and execute code
	public Call executeStatement_Bak (String statement) throws InvocationTargetException, CompileException{
		ScriptEvaluator script = new ScriptEvaluator();
		script.setReturnType(Call.class);
		script.cook(statement);
		return executeStatement(script);
	}
	
	// Execute precompiled code
	public Call executeStatement (ScriptEvaluator script) throws InvocationTargetException, CompileException{
		return (Call) script.evaluate(null);
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
