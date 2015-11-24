package einkaufsListe;

class EinkaufListeRow{
	private Einkauf einkauf;
	private Rueckmeldung rueckmeldung;
	
	public EinkaufListeRow(Einkauf einkauf,Rueckmeldung rueckmeldung){
		this.setEinkauf(einkauf);
		this.setRueckmeldung(rueckmeldung);
	}
	// Object kopieren
	public EinkaufListeRow(EinkaufListeRow row){
		einkauf = new Einkauf();
		einkauf.setArtikel(row.getEinkauf().getArtikel());
		einkauf.setEinzukaufendeMenge(row.getEinkauf().getEinzukaufendeMenge());
		einkauf.setPrio(row.getEinkauf().getPrio());
		
		rueckmeldung = new Rueckmeldung();
		rueckmeldung.setArtikel(row.getRueckmeldung().getArtikel());
		rueckmeldung.setBenutzer(row.getRueckmeldung().getBenutzer());
		rueckmeldung.setEingekaufteMenge(row.getRueckmeldung().getEingekaufteMenge());
		rueckmeldung.setPreis(row.getRueckmeldung().getPreis());
		rueckmeldung.setZeit(row.getRueckmeldung().getZeit());
	}

	public Rueckmeldung getRueckmeldung() {
		return rueckmeldung;
	}

	public void setRueckmeldung(Rueckmeldung rueckmeldung) {
		this.rueckmeldung = rueckmeldung;
	}

	public Einkauf getEinkauf() {
		return einkauf;
	}

	public void setEinkauf(Einkauf einkauf) {
		this.einkauf = einkauf;
	}
}