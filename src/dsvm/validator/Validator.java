package dsvm.validator;


import java.util.ArrayList;

import dsvm.model.IntentModel;

public interface Validator {
	public ArrayList<IntentModel> validateModels(ArrayList<IntentModel> models, Object... dsc);
}
