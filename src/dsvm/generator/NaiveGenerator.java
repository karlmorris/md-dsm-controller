package dsvm.generator;


import java.util.ArrayList;

import dsvm.dsc.DSC;
import dsvm.model.IntentModel;
import dsvm.procedure.Procedure;
import dsvm.repository.Repository;

public class NaiveGenerator implements Generator{
	
	public ArrayList<IntentModel> generateModels(DSC initDSC) {
		/** Collection to store final set of models */
		ArrayList<IntentModel> matchingModels = new ArrayList<IntentModel>();
		
		/** Get procedures for the current DSC */
		ArrayList<Procedure> matchingProcedures = Repository.getInstance().getProceduresWithDSC(initDSC);
		
		if (matchingProcedures.isEmpty()){
			return null;
		}
		
		for (int i = 0; i < matchingProcedures.size(); i++){
			
			/** Temporary collection of models for current level */
			ArrayList<IntentModel> tempMatchingModels = new ArrayList<IntentModel>();
			ArrayList<DSC> dependencies = matchingProcedures.get(i).getDependency();
			
			
			/** If no dependencies, return model with only current procedure */
			if (dependencies.isEmpty()){
				matchingModels.add(new IntentModel(matchingProcedures.get(i)));
			} else {
				/** If further dependencies, make recursive call, then join */
				ArrayList<IntentModel> subModels = null;
				for (int j = 0; j < dependencies.size(); j++){
					subModels = generateModels(dependencies.get(j));
					
					/** If a procedure is not available to meet a stated dependency, that model is removed. */
					if (subModels != null){
						/**If first dependency, create new set of models, otherwise merge new and current models*/
						if (j == 0){
							tempMatchingModels.addAll(joinModels(matchingProcedures.get(i), subModels));
						} else {
							mergeModels(tempMatchingModels, subModels);
						}
					} else {
						tempMatchingModels.clear();
					}
				}
			}
			/**Add lower layer models to final collection*/
			matchingModels.addAll(tempMatchingModels);
		}
		return matchingModels;
	}
	
	/*
	 * Create new set of models from initial procedure and sub models
	 */
	static ArrayList<IntentModel> joinModels(Procedure p, ArrayList<IntentModel> ml){
		ArrayList<IntentModel> newModels = new ArrayList<IntentModel>();
		
		for (int i = 0; i < ml.size(); i++){
			IntentModel m = new IntentModel(p);
			m.addDependency(p.getId(), ml.get(i).getInit());
			m.addAllDependencies(ml.get(i).getAllDependencies());
			newModels.add(m);
		}
		return newModels;
	}
	
	/*
	 * Join models by merging on initial procedure of first set of models (cross product).
	 */
	static void mergeModels(ArrayList<IntentModel> models, ArrayList<IntentModel> sub){
		for (int i = 0; i < models.size(); i++){
			for (int j = 0; j < sub.size(); j++){
				models.get(i).addDependency(models.get(i).getInit().getId(), sub.get(j).getInit());
				//models.get(i).addAllDependencies(sub.get(j).getAllDependencies());
			}
		}
	}
}
