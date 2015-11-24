package einkaufsListe;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import main.DAO;



public class EinkaufDAO extends DAO{
	private Statement stmt = null;
	private ResultSet rs = null;
	private Connection con = null;
	
	
	public ArrayList<Einkauf> getEinkaeufe() throws EinkaufDAOLoadException{

		try {
		con = getDBCon();
		ArrayList<Einkauf> einkaufsListe = null;
		String sqlStatement = "select * from einkauf";

			// Laden des ResultSets
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sqlStatement);
			einkaufsListe = new ArrayList<Einkauf>();
			rs.beforeFirst();
			while (rs.next()){
				Einkauf einkauf = new Einkauf();
				einkauf.setArtikel(rs.getString("Artikel"));
				einkauf.setEinzukaufendeMenge(rs.getInt("EinzukaufendeMenge"));
				einkauf.setPrio(rs.getInt("Prio"));
				einkaufsListe.add(einkauf);
			}
		rs.close();
		stmt.close();
		closeConnection(con);
		return einkaufsListe;
		} catch (SQLException e) {
			throw new EinkaufDAOLoadException();
		}
	}

	public void saveEinkaeufe(ArrayList<Einkauf> einkaufsListe) throws EinkaufDAOSaveException{
		try{
		con = getDBCon();
		String sqlStatement = "select * from einkauf";
		// Laden des ResultSets
		stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);
		rs = stmt.executeQuery(sqlStatement);
		rs.beforeFirst();
		// Alten Stand löschen
		while(rs.next()){
			rs.deleteRow();
		}
		// Hinzufügen der neuen Datensatze
			rs.beforeFirst();
			for ( Einkauf e: einkaufsListe){
				rs.moveToInsertRow();
				rs.updateString("artikel", e.getArtikel());
				rs.updateInt("prio", e.getPrio());
				rs.updateInt("einzukaufendeMenge", e.getEinzukaufendeMenge());
				rs.insertRow();
			}
			rs.close();
			stmt.close();
			closeConnection(con);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new EinkaufDAOSaveException();
		}
	}
}
