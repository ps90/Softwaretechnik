package einkaufsListe;

import main.WGKasseException;

public class EinkaufReqFieldException extends WGKasseException{
	public EinkaufReqFieldException(){
		super("Nicht alle benötigen Felder wurden gefüllt");
	}
}
