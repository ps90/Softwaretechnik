package einkaufsListe;

import main.WGKasseException;

public class EinkaufDAOSaveException extends WGKasseException{
	public EinkaufDAOSaveException(){
		super("Fehler beim speichern der Einkaufsliste");
	}
}
