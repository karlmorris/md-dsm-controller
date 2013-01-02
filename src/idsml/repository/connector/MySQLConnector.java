package idsml.repository.connector;

import idsml.dsc.DSC;
import idsml.procedure.ExecutionUnit;
import idsml.procedure.Procedure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MySQLConnector implements Connector {

	Connection con = null;
    Statement st = null;
    ResultSet rs = null;
	
	@Override
	public boolean connect(String user, String password, String server, String db) {
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + server + ":3306/" + db, user, password);
			return true;
		} catch (Exception e){
			return false;
		}
	}

	@Override
	public boolean disconnect() {
		try {
			if (!rs.isClosed())
				rs.close();
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
	@Override
	public ArrayList<Procedure> getProcedureDescriptors(DSC dsc) {
		ArrayList<Procedure> procedures = new ArrayList<Procedure>();
		try {
			st = con.createStatement();
			String query = "SELECT id from classifiers where name = '" + dsc.getName() + "'";
			System.out.println(query);
			rs = st.executeQuery(query);
			String dsc_id = "";
			if(rs.next())
				dsc_id = rs.getString(1);
			rs = st.executeQuery("SELECT id, name, initial_execution_unit_id from procedures where classifier_id = '" + dsc_id + "'");
			
			while (rs.next())
				procedures.add(new Procedure(rs.getString(1), rs.getString(2), dsc, new ExecutionUnit(rs.getString(3), "")));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(procedures);
		return procedures;
	}
	
	public ArrayList<ExecutionUnit> geExecutionUnits(Procedure procedure){
		ArrayList<ExecutionUnit> executionUnits = new ArrayList<ExecutionUnit>();
		try {
			st = con.createStatement();
			rs = st.executeQuery("SELECT id, body from execution_units where procedure_id = '" + procedure.getId() + "'");
			
			while (rs.next())
				executionUnits.add(new ExecutionUnit(rs.getString(1), rs.getString(2)));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return executionUnits;
	}

}
