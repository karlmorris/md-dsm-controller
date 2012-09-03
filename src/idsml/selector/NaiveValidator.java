package idsml.selector;

import idsml.dsc.DSC;
import idsml.model.Model;
import idsml.procedure.Procedure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class NaiveValidator implements Validator{

	public ArrayList<Model> validateModels(ArrayList<Model> models, DSC dsc){
		ArrayList<Model> validModels = new ArrayList<Model>();
		
		
		for (int i = 0; i < models.size(); i++){
			if (models.get(i).getInit().getClassifier().equals(dsc)){
				validModels.add(models.get(i));
			} else {
				Map<String, ArrayList<Procedure>> procedures = models.get(i).getAllDependencies();
				Iterator<String> keyIterator = procedures.keySet().iterator();
				while (keyIterator.hasNext()){
					String currentKey = keyIterator.next();
					for (int k = 0; k < procedures.get(currentKey).size(); k++){
						if (procedures.get(currentKey).get(k).getClassifier().equals(dsc)){
							validModels.add(models.get(i));
						}
					}
				}
			}
		}
		
		return validModels;
		
	}
}