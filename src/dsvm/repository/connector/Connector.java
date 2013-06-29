package dsvm.repository.connector;

import java.util.ArrayList;

import dsvm.dsc.DSC;
import dsvm.procedure.ExecutionUnit;
import dsvm.procedure.Procedure;

public interface Connector {
	public boolean connect(String user, String password, String server, String db);
	public boolean disconnect();
	public ArrayList<Procedure> getProcedureDescriptors(DSC dsc);
	public ArrayList<ExecutionUnit> geExecutionUnits(Procedure procedure);
}