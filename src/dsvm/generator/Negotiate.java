package dsvm.generator;

import java.lang.reflect.InvocationTargetException;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.ScriptEvaluator;

public class Negotiate extends PostModelGenerationOperation {
	
	public Negotiate(Object data){
		super(data);
	}
	public boolean execute (){
		boolean result = false;
		ScriptEvaluator script = new ScriptEvaluator();
		script.setReturnType(boolean.class);
		if (data != null){
			try {
				script.cook(data.toString());
			} catch (CompileException e) {
				e.printStackTrace();
			}
			try {
				result = (Boolean) script.evaluate(null);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} else
			result = true;
		return result;
	}
}