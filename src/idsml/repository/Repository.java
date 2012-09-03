package idsml.repository;

import idsml.dsc.DSC;
import idsml.procedure.Procedure;

import java.util.ArrayList;

public class Repository {
	static ArrayList<Procedure> procedures = new ArrayList<Procedure>();
	
	public static void addProcedure(Procedure procedure){
		procedures.add(procedure);
	}
	
	static public Procedure getProcedure(String id){
		
		for (int i = 0; i < procedures.size(); i++){
			if (procedures.get(i).getId().equals(id))
				return procedures.get(i);
		}
		return null;
	}
	
	
	static public ArrayList<Procedure> getProceduresWithDSC(DSC dsc){
		
		ArrayList<Procedure> matchingProcedures = new ArrayList<Procedure>();
		for (int i = 0; i < procedures.size(); i++){
			if (procedures.get(i).getClassifier().equals(dsc))
				matchingProcedures.add(procedures.get(i));
		}
		return matchingProcedures;
	}
}
