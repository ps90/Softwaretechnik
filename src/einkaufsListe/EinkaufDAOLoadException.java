package einkaufsListe;

import main.WGKasseException;

public class EinkaufDAOLoadException extends WGKasseException{
	public EinkaufDAOLoadException(){
		super("Fehler beim laden der Einkaufsliste");
	}
}
