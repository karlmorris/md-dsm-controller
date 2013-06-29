package dsvm.manager;

import dsvm.dsc.DSC;
import dsvm.model.*;
import dsvm.procedure.Procedure;

public class Manager {
	private IntentModel currentModel;
	
	public Procedure getProcedureForDSC(DSC dsc){
		return currentModel.getProcedure(dsc);
	}
}
