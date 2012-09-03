package idsml.selector;

import idsml.model.Model;

import java.util.ArrayList;

public class NaiveSelector implements Selector{
	public Model getBestModel(ArrayList<Model> models){
		Model bestModel = null;
		int bestModelCost = Integer.MAX_VALUE;
		
		for (int i = 0; i < models.size(); i++){
			int cost = modelCost(models.get(i));
			if (cost < bestModelCost){
				bestModel = models.get(i);
				bestModelCost = cost;
			}
		}
		
		return bestModel;
	}
	
	static int modelCost(Model m){
		return m.getAllDependencies().size();
	}
}
