package einkaufsListe;

import main.WGKasseException;

public class EinkaufReqFieldException extends WGKasseException{
	public EinkaufReqFieldException(){
		super("Nicht alle ben�tigen Felder wurden gef�llt");
	}
}
