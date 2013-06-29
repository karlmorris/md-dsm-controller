package dsvm.generator;

import java.util.ArrayList;

import dsvm.dsc.DSC;
import dsvm.model.IntentModel;

public interface Generator {
		public ArrayList<IntentModel> generateModels(DSC initDSC);
}
