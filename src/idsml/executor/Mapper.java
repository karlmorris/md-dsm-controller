package idsml.executor;

import idsml.dsc.DSC;
import idsml.model.IntentModel;
import idsml.procedure.Procedure;

public class Mapper {
	public static Procedure getProcedureForDSC(DSC dsc){
		return null;
	}
	
	public static Procedure getProcedureForDSC(IntentModel model, DSC dsc){
		return model.getProcedure(dsc);
	}

}
