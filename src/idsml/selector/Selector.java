package idsml.selector;

import idsml.model.IntentModel;

import java.util.ArrayList;

public interface Selector {
	public IntentModel getBestModel(ArrayList<IntentModel> models);
}
