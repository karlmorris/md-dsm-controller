package idsml.repository.connector;

import idsml.dsc.DSC;
import idsml.procedure.ExecutionUnit;
import idsml.procedure.Procedure;
import java.util.ArrayList;

public interface Connector {
	public boolean connect(String user, String password, String server, String db);
	public boolean disconnect();
	public ArrayList<Procedure> getProcedureDescriptors(DSC dsc);
	public ArrayList<ExecutionUnit> geExecutionUnits(Procedure procedure);
}