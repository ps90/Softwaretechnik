package einkaufsListe;

public class Einkauf {
	private int prio = 3;
	private String artikel = "";
	private int einzukaufendeMenge = 0;
	
	
	public boolean isEmpty(){
		if( artikel.compareTo("") == 0 && einzukaufendeMenge == 0 && prio == 3)return true;
		else return false;
	}
	
	public boolean check() throws EinkaufReqFieldException{
		if( artikel.compareTo("") != 0 && einzukaufendeMenge != 0){
			return true;
		}
		else{
			throw new EinkaufReqFieldException();
		}
	}
	
	public int getPrio() {
		return prio;
	}
	public void setPrio(int prio) {
		this.prio = prio;
	}
	public String getArtikel() {
		return artikel;
	}
	public void setArtikel(String artikel) {
		this.artikel = artikel;
	}
	public int getEinzukaufendeMenge() {
		return einzukaufendeMenge;
	}
	public void setEinzukaufendeMenge(int einzukaufendeMenge) {
		this.einzukaufendeMenge = einzukaufendeMenge;
	}
}
