package einkaufsListe;

import main.WGKasseException;

public class RueckmeldungReqFieldException extends WGKasseException{
	RueckmeldungReqFieldException(){
		super("Nicht alle benötigen Felder wurden gefüllt");
	}

}
