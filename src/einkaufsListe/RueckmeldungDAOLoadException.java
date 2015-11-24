package einkaufsListe;

import main.WGKasseException;

public class RueckmeldungDAOLoadException extends WGKasseException{

	public RueckmeldungDAOLoadException(){
		super("Fehler beim laden der Rückmeldung");
	}
	
}
