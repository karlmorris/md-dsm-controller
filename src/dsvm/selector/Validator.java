package dsvm.selector;


import java.util.ArrayList;

import dsvm.dsc.DSC;
import dsvm.model.IntentModel;

public interface Validator {
	public ArrayList<IntentModel> validateModels(ArrayList<IntentModel> models, DSC dsc);
}
