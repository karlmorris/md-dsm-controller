package idsml.generator;
import idsml.dsc.DSC;
import idsml.model.Model;

import java.util.ArrayList;

public interface Generator {
		public ArrayList<Model> generateModels(DSC initDSC);
}
