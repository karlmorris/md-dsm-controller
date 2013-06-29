package dsvm.generator;


import java.util.ArrayList;

import dsvm.dsc.DSC;
import dsvm.dsc.Type;
import dsvm.model.IntentModel;
import dsvm.procedure.Procedure;
import dsvm.repository.Repository;
import dsvm.selector.NaiveSelector;
import dsvm.selector.NaiveValidator;

public class ModelGeneratorTester {
public static void main(String[] args) {

		// Set up DSCs

		DSC sendDSC = new DSC("Send", Type.OPER);
		DSC encryptDSC = new DSC("Encrypt", Type.OPER);
		DSC dummyDSC = new DSC("Dummy", Type.OPER);
		DSC compressDSC = new DSC("Compression", Type.OPER);

		ArrayList<DSC> dependencies1 = new ArrayList<DSC>();	
		dependencies1.add(encryptDSC);
		
		ArrayList<DSC> dependencies2 = new ArrayList<DSC>();	
		dependencies2.add(dummyDSC);
		
		ArrayList<DSC> dependencies3 = new ArrayList<DSC>();	
		dependencies3.add(compressDSC);
		
		Procedure procedure1 = new Procedure("0001", "SendBasic1", sendDSC, dependencies1);
		Procedure procedure2 = new Procedure("0002", "SendBasic2", sendDSC, dependencies1);
		Procedure procedure3 = new Procedure("0003", "SendBasic3", sendDSC, dependencies1);
		Procedure procedure4 = new Procedure("0004", "Encrypt1", encryptDSC, dependencies2);
		Procedure procedure5 = new Procedure("0005", "Encrypt2", encryptDSC, dependencies2);
		Procedure procedure6 = new Procedure("0006", "Encrypt3", encryptDSC, dependencies2);
		Procedure procedure7 = new Procedure("0007", "Dummy1", dummyDSC, dependencies3);
		Procedure procedure8 = new Procedure("0008", "Dummy2", dummyDSC, dependencies3);
		Procedure procedure9 = new Procedure("0009", "Compress1", compressDSC);
		Procedure procedure0 = new Procedure("0000", "Compress2", compressDSC);
		
		
		Repository.addProcedure(procedure1);
		Repository.addProcedure(procedure2);
		Repository.addProcedure(procedure3);
		Repository.addProcedure(procedure4);
		Repository.addProcedure(procedure5);
		Repository.addProcedure(procedure6);
		Repository.addProcedure(procedure7);
		Repository.addProcedure(procedure8);
		Repository.addProcedure(procedure9);
		Repository.addProcedure(procedure0);
		
		for (int i = 0; i < 90; i++){
			Procedure procedure = new Procedure(String.valueOf(i), "Compress2", new DSC("Test", Type.OPER));
			Repository.addProcedure(procedure);
		}
		
		/*
		// First basic set of procedures along with dependencies
		Procedure sendBasic = new Procedure("0001", "SendBasic", sendDSC, "send0001");
		
		ArrayList<DSC> secureDependencies = new ArrayList<DSC>();
		secureDependencies.add(encryptDSC);	

		Procedure sendSecure = new Procedure("0002", "SendSecure", sendDSC, "ss0001", secureDependencies);

		Procedure DHEncrypt = new Procedure("0003", "DHEncrypt", encryptDSC, "dh0001");
		Procedure PKIEncrypt = new Procedure("0004", "PKIEncrypt", encryptDSC, "pki0001");
		
		
		// Add first set of procedures to repository
		Repository.addProcedure(sendBasic);
		Repository.addProcedure(sendSecure);
		Repository.addProcedure(DHEncrypt);
		Repository.addProcedure(PKIEncrypt);
		
		
		// Second set of procedures
		ArrayList<DSC> dummyDependencies = new ArrayList<DSC>();	
		dummyDependencies.add(dummyDSC);
		dummyDependencies.add(compressDSC);
		Procedure dummyProcedure1 = new Procedure("0005", "DummyProcedure1", dummyDSC, "dummy");
		Procedure dummyProcedure2 = new Procedure("0006", "DummyProcedure2", dummyDSC, "dummy");
		Procedure compressProcedure = new Procedure("023", "CompressionProcedure", compressDSC, "compress001");	
		Procedure moreSecure = new Procedure("0010", "SendMoreSecure", sendDSC, "sser0001", secureDependencies);
		Procedure CaesarEncrypt = new Procedure("0020", "CaesarEncrypt", encryptDSC, "cae0001", dummyDependencies);
		
		// Add them to the repository
		Repository.addProcedure(CaesarEncrypt);
		Repository.addProcedure(dummyProcedure1);
		Repository.addProcedure(dummyProcedure2);
		Repository.addProcedure(moreSecure);
		Repository.addProcedure(compressProcedure);
		*/
		
		//Set up an initial DSC matching a command.
		DSC initialDSC = sendDSC;
		
		ArrayList<IntentModel> matchingModels = null;
		ArrayList<IntentModel> validModels = null;
		IntentModel bestModel = null;
		
		//Begin timing
		long startTime, endTime, duration = 0, totalTime = 0;
		int cycles = 10;
		for (int i = 0; i < cycles; i++){
			startTime = System.currentTimeMillis();

			// Find all models which match command
			matchingModels = (new NaiveGenerator()).generateModels(initialDSC);

			// Find valid models based on user preferences
			validModels = (new NaiveValidator()).validateModels(matchingModels, dummyDSC);

			// Find the best model based on cost
			bestModel = (new NaiveSelector()).getBestModel(validModels);

			//End timing
			endTime = System.currentTimeMillis();
			duration = endTime - startTime;
			totalTime = totalTime + duration;
		}

		System.out.println("Total time: \t" + totalTime);
		System.out.println("Average time: \t" + totalTime / cycles);
		
		
		// Format and print report
		System.out.println("We generated " + matchingModels.size() + " models");
		System.out.println();
		System.out.println(validModels.size() + " are valid based on user preferences");
		System.out.println();
		if (validModels.size() > 0)
			System.out.println("The best model is:\n" + bestModel.prettyPrinter());
		
		System.out.println("Total time for Model Generation: " + totalTime + " nanoseconds");
		
		
		System.out.println("\n\nThe full list of models:\n");
		for (int i = 0; i < matchingModels.size(); i++){
			System.out.println(matchingModels.get(i).prettyPrinter());
		}
		

	}
}