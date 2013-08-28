package dsvm.generator;


import java.util.ArrayList;

import com.rits.cloning.*;

import dsvm.dsc.DSC;
import dsvm.model.IntentModel;
import dsvm.procedure.Procedure;
import dsvm.repository.Repository;

public class NaiveGenerator implements Generator{
	
	static Cloner cloner = new Cloner();
	
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
							ArrayList<IntentModel> singleModelList = new ArrayList<IntentModel>();
							singleModelList.add(new IntentModel(matchingProcedures.get(i)));
							tempMatchingModels = mergeModels(singleModelList, subModels);
						} else {
							tempMatchingModels = mergeModels(tempMatchingModels, subModels);
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
	 * Join models by merging on initial procedure of first set of models (cross product).
	 */
	static ArrayList<IntentModel> mergeModels(ArrayList<IntentModel> models, ArrayList<IntentModel> sub){
		ArrayList<IntentModel> newModels = new ArrayList<IntentModel>();
		
		for (int i = 0; i < models.size(); i++){
			for (int j = 0; j < sub.size(); j++){
				IntentModel newModel = cloner.deepClone(models.get(i));
				newModel.addDependency(newModel.getInit().getId(), sub.get(j).getInit());
				newModel.addAllDependencies(sub.get(j).getAllDependencies());
				newModels.add(newModel);
			}
		}
		return newModels;
	}
}
