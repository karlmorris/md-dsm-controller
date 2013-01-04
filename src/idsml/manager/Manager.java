package idsml.manager;

import idsml.dsc.DSC;
import idsml.model.*;
import idsml.procedure.Procedure;

public class Manager {
	private IntentModel currentModel;
	
	public Procedure getProcedureForDSC(DSC dsc){
		return currentModel.getProcedure(dsc);
	}
}
