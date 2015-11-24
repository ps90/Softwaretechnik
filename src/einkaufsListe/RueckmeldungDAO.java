package einkaufsListe;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import benutzer.Benutzer;

import einstellungen.DAO;


public class RueckmeldungDAO {
	private Statement stmt = null;
	private ResultSet rs = null;
	private Connection con = null;
	
	public ArrayList<Rueckmeldung> getRueckmeldung(Benutzer benutzer)throws RueckmeldungDAOLoadException{
		return new ArrayList<Rueckmeldung>();
	}
	public ArrayList<Rueckmeldung> getRueckmeldung(Timestamp start, Timestamp end)throws RueckmeldungDAOLoadException{
		return new ArrayList<Rueckmeldung>();
	}
	public ArrayList<Rueckmeldung> getRueckmeldung(Benutzer user, Timestamp start, Timestamp end)throws RueckmeldungDAOLoadException{
		return new ArrayList<Rueckmeldung>();
	}
	public void saveRueckmeldung(ArrayList<Rueckmeldung> rueckmeldung)throws RueckmeldungDAOSaveException{
		try{
		con = DAO.getDBCon();
		String sqlStatement = "insert into rueckmeldung values (DEFAULT,'"
																+Benutzer.getUser()+"','"
																+ new Timestamp(System.currentTimeMillis())+"')";
		// Laden des ResultSets
		stmt = con.createStatement();
		stmt.executeUpdate(sqlStatement,Statement.RETURN_GENERATED_KEYS);
		rs = stmt.getGeneratedKeys();
		rs.next();
		int key = rs.getInt(1);
		
		for(Rueckmeldung r: rueckmeldung){
			sqlStatement = "insert into rueckmeldungPosition values (DEFAULT,"
							+key+",'"
							+r.getArtikel()+"',"
							+r.getEingekaufteMenge()+","
							+r.getPreis()+")";
			stmt.executeUpdate(sqlStatement);
		}
			rs.close();
			stmt.close();
			DAO.closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RueckmeldungDAOSaveException();
		}
		
	}
}
