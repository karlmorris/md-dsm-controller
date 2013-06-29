package dsvm.executor;

import dsvm.dsc.DSC;
import dsvm.model.IntentModel;
import dsvm.procedure.Procedure;

public class Mapper {
	public static Procedure getProcedureForDSC(DSC dsc){
		return null;
	}
	
	public static Procedure getProcedureForDSC(IntentModel model, DSC dsc){
		return model.getProcedure(dsc);
	}

}
