package idsml.executor;

import idsml.dsc.DSC;
import idsml.model.Model;
import idsml.procedure.Procedure;

public class Mapper {
	public static Procedure getProcedureForDSC(DSC dsc){
		return null;
	}
	
	public static Procedure getProcedureForDSC(Model model, DSC dsc){
		return model.getProcedure(dsc);
	}

}
