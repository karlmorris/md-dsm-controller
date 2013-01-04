package idsml.generator;
import idsml.dsc.DSC;
import idsml.model.IntentModel;

import java.util.ArrayList;

public interface Generator {
		public ArrayList<IntentModel> generateModels(DSC initDSC);
}
