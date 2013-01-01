package idsml.executor;
import idsml.dsc.DSC;
import idsml.model.Model;
import idsml.procedure.ExecutionUnit;
import idsml.procedure.Procedure;
import idsml.repository.connector.Connector;
import idsml.repository.connector.MySQLConnector;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.*;


public class Executor {
	
	Model currentModel;
	Procedure currentProcedure;
	ArrayList<ExecutionUnit> executionUnits = null;
	
	ExpressionEvaluator expression = new ExpressionEvaluator();
	ScriptEvaluator script = new ScriptEvaluator();
	
	String statement = "";
	
	public void executeModel(Model model){
		currentModel = model;
		
		Procedure init = model.getInit();
		
		executionUnits = getExecutionUnits(init);
		
		ExecutionUnit startEU = executionUnits.get(0); // Grab initial EU. Change to Map and init.getStartEU()
		
		try {
			executeStatement(startEU.getBody());
		} catch (CompileException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public Call executeStatement (String statement) throws InvocationTargetException, CompileException{
		this.statement = statement;
		//expression.cook(statement);
		//expression.evaluate(null);
		script.setReturnType(Call.class);
		script.cook(statement);
		Call result = (Call) script.evaluate(null);
		
		
		if (result instanceof DSCCall){
			System.out.println("DSC to call: " + ((DSCCall)result).getDSC().getName());
		} else if (result instanceof EUCall){
			System.out.println("EU to call: " + ((EUCall)result).getEUId());
		} else if (result instanceof EventWaitCall){
			System.out.println("Will register: " + ((EventWaitCall)result).getEUId() + " in response to " + ((EventWaitCall)result).getEvent());
		}
		return result;
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
