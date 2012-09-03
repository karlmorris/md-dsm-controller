package idsml.selector;

import idsml.dsc.DSC;
import idsml.model.Model;

import java.util.ArrayList;

public interface Validator {
	public ArrayList<Model> validateModels(ArrayList<Model> models, DSC dsc);
}
