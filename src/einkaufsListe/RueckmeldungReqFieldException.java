package einkaufsListe;

import main.WGKasseException;

public class RueckmeldungReqFieldException extends WGKasseException{
	RueckmeldungReqFieldException(){
		super("Nicht alle ben�tigen Felder wurden gef�llt");
	}

}
