package idsml.selector;

import idsml.model.Model;

import java.util.ArrayList;

public interface Selector {
	public Model getBestModel(ArrayList<Model> models);
}
