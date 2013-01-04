package idsml.selector;

import idsml.dsc.DSC;
import idsml.model.IntentModel;

import java.util.ArrayList;

public interface Validator {
	public ArrayList<IntentModel> validateModels(ArrayList<IntentModel> models, DSC dsc);
}
