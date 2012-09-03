package idsml.executor;
import java.lang.reflect.InvocationTargetException;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.*;


public class Executor {
	ExpressionEvaluator expression = new ExpressionEvaluator();
	String statement = "";
	
	public void executeStatement (String statement) throws InvocationTargetException, CompileException{
		this.statement = statement;
		expression.cook(statement);
		expression.evaluate(null);
	}
	
	public String getLastStatement(){
		return statement;
	}

}
