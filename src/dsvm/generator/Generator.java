package dsvm.generator;

import java.util.ArrayList;

import dsvm.dsc.DSC;
import dsvm.model.IntentModel;
import dsvm.procedure.Procedure;

public interface Generator {
		public ArrayList<IntentModel> generateModels(DSC initDSC, ArrayList<Procedure> procedures);
}
