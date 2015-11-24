package einkaufsListe;

import main.WGKasseException;

public class RueckmeldungDAOSaveException extends WGKasseException{

	public RueckmeldungDAOSaveException(){
		super("Fehler beim speichern der Rückmeldung");
	}
}
