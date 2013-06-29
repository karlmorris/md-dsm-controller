package dsvm.selector;


import java.util.ArrayList;

import dsvm.model.IntentModel;

public interface Selector {
	public IntentModel getBestModel(ArrayList<IntentModel> models);
}
