package einkaufsListe;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;


class EinkaufsListeModel extends AbstractTableModel {
	public final static int COMBOBOX_COLUMN = 0;
	public final static int ARTIKEL_COLUMN = 1;
	public final static int EINZUKMENG_COLUMN = 2;
	public final static int GEKAUFTEMENG_COLUMN = 3;
	public final static int PREIS_COLUMN = 4;
	public final static int BUTTON_COLUMN = 5;
	public final static String[] COMBOBOX_VALUES = {"1","2","3"};
	
    public final static String[] columnNames = {"Priorität",
                                    			"Artikel",
                                    			"Einzukaufende Menge(in Stk)",
                                    			"Gekaufte Menge (in Stk)",
                                    			"Preis (in €)",
                                    			"Zeile löschen"};
    public final static Class[] columnClasses = {
    	Integer.class,
    	String.class,
		Integer.class,
		Integer.class,
		Integer.class,
		JButton.class};
    
    private ArrayList<EinkaufListeRow> einkaufsListe = new ArrayList<EinkaufListeRow>();
    private RueckmeldungDAO rueckmeldungDAO;
    private EinkaufDAO einkaufDAO;
    private boolean editable;


    public EinkaufsListeModel(){
    	super();
    	einkaufDAO = new EinkaufDAO();
    	rueckmeldungDAO = new RueckmeldungDAO();
    }
    
    
    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return einkaufsListe.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }
    /*
     * default renderer/editor für jede cell
    */ 
    public Class getColumnClass(int column) {
        return columnClasses[column];
    }
    

    public Object getValueAt(int row, int col) {
    	if ( einkaufsListe.size() > 0){
            switch(col){
            case 0: return einkaufsListe.get(row).getEinkauf().getPrio();
            case 1: return einkaufsListe.get(row).getEinkauf().getArtikel();
            case 2: return einkaufsListe.get(row).getEinkauf().getEinzukaufendeMenge();
            case 3: return einkaufsListe.get(row).getRueckmeldung().getEingekaufteMenge();
            case 4: return einkaufsListe.get(row).getRueckmeldung().getPreis();
            case 5: return "-"; // Deletebutton-Text
            default: return null;
            }   	
    	}else return null;
   
    }




    public boolean isCellEditable(int row, int col) {
            return editable;
    }

    public void setValueAt(Object value, int row, int col) {
        switch(col){
        case 0: einkaufsListe.get(row).getEinkauf().setPrio(Integer.parseInt((String) value));break;
        case 1: einkaufsListe.get(row).getEinkauf().setArtikel((String)value);
        		einkaufsListe.get(row).getRueckmeldung().setArtikel((String)value);break;
        case 2: einkaufsListe.get(row).getEinkauf().setEinzukaufendeMenge((Integer)value);break;
        case 3: einkaufsListe.get(row).getRueckmeldung().setEingekaufteMenge((Integer)value);break;
        case 4: einkaufsListe.get(row).getRueckmeldung().setPreis((Double)value);break;
        case 5: break; // Delete-Button
        default: break;
        }
        fireTableCellUpdated(row, col);
    }
    public void deleteEinkauf(int row){
    	if(isEditable()){
    	einkaufsListe.remove(row);
    	fireTableDataChanged();
    	}
    }
    public void addEinkaufzeile(EinkaufListeRow einkaufzeile){
    	if(isEditable()){
        	einkaufsListe.add(einkaufzeile);
        	fireTableDataChanged();
    	}

    }


	public boolean isEditable() {
		return editable;
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
		fireTableDataChanged();
	}	
	
	public void loadEinkaufsListe() throws EinkaufDAOLoadException{
		einkaufsListe = new ArrayList<EinkaufListeRow>();
		for( Einkauf e: einkaufDAO.getEinkaeufe()){
			einkaufsListe.add(new EinkaufListeRow(e,new Rueckmeldung()));
		}
		fireTableDataChanged();
	}
	
	public void saveEinkaufsListe() throws RueckmeldungReqFieldException, EinkaufReqFieldException, EinkaufDAOLoadException, EinkaufDAOSaveException, RueckmeldungDAOSaveException{
		ArrayList<Einkauf> einkaeufe = new ArrayList<Einkauf>();
		ArrayList<Rueckmeldung> rueckmeldung = new ArrayList<Rueckmeldung>();
		ArrayList<EinkaufListeRow> copy = new ArrayList<EinkaufListeRow>();
		
		// Mit Kopie arbeiten, damit im Fehlerfall die Mengen nicht geändert wurden
		for(EinkaufListeRow r: einkaufsListe){
			copy.add(new EinkaufListeRow(r));
		}
		for(EinkaufListeRow r: copy){
			if(!r.getEinkauf().isEmpty() && r.getEinkauf().check()){
				einkaeufe.add(r.getEinkauf());
				// Artikel in die Rueckmeldung setzen -> wird ansonsten nur beim eintragen in die Tabelle gesetzt d.h. nicht beim Laden aus DB
				r.getRueckmeldung().setArtikel(r.getEinkauf().getArtikel());
				// Einkauf ist OK, d.h. es kann auch darauf rueckgemeldet werden
				if(!r.getRueckmeldung().isEmptry() && r.getRueckmeldung().check()){
					rueckmeldung.add(r.getRueckmeldung());
					// neue Menge setzen
					r.getEinkauf().setEinzukaufendeMenge(r.getEinkauf().getEinzukaufendeMenge() - r.getRueckmeldung().getEingekaufteMenge());
					// Es wurde mind. soviel wie erforderlich gekauft ( eventuell sogar mehr ) -> von der Liste nehmen
					if(r.getEinkauf().getEinzukaufendeMenge() <= 0) einkaeufe.remove(r.getEinkauf());
				}
			}
		}
		// es ist möglich, dass die Liste leer ist
		einkaufDAO.saveEinkaeufe(einkaeufe);
		if(rueckmeldung.size() > 0)rueckmeldungDAO.saveRueckmeldung(rueckmeldung);
		loadEinkaufsListe();
	}
	
    public void clearEmptyRows(){
    	// safely remove from a collection while iterating - instead of for
    	Iterator<EinkaufListeRow> iterator = einkaufsListe.iterator();
    	while(iterator.hasNext()){
    		EinkaufListeRow r = iterator.next();
    		if( r.getEinkauf().isEmpty() && r.getRueckmeldung().isEmptry()){
    			iterator.remove();
    		}
    	}
    	fireTableDataChanged();
    }

}


