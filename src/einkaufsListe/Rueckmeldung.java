package einkaufsListe;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.FieldPosition;

import util.Decimal2PlacesFormat;

public class Rueckmeldung {
	private String artikel = "";
	private int eingekaufteMenge = 0;
	private double preis = 0;
	private String benutzer = "";
	private Timestamp zeit;
	
	public boolean check() throws RueckmeldungReqFieldException{
		if( eingekaufteMenge != 0 && preis != 0 && artikel.compareTo("") != 0 ){
			return true;
		}
		else{
			throw new RueckmeldungReqFieldException();
		}
	}
	public boolean isEmptry(){
		if (eingekaufteMenge == 0 && preis == 0) return true;
		else return false;
	}
	
	public int getEingekaufteMenge() {
		return eingekaufteMenge;
	}
	public void setEingekaufteMenge(int eingekaufteMenge) {
		this.eingekaufteMenge = eingekaufteMenge;
	}
	public double getPreis() {
		return preis;
	}
	public void setPreis(double preis) {
		this.preis = preis;
	}
	public String getBenutzer() {
		return benutzer;
	}
	public void setBenutzer(String benutzer) {
		this.benutzer = benutzer;
	}
	public Timestamp getZeit() {
		return zeit;
	}
	public void setZeit(Timestamp zeit) {
		this.zeit = zeit;
	}
	public String getArtikel() {
		return artikel;
	}
	public void setArtikel(String artikel) {
		this.artikel = artikel;
	}
}
