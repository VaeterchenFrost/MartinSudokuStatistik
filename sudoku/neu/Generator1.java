package sudoku.neu;

import java.util.ArrayList;

import sudoku.kern.exception.Exc;
import sudoku.kern.feldmatrix.FeldNummer;
import sudoku.kern.feldmatrix.FeldNummerListe;
import sudoku.kern.feldmatrix.FeldNummerMitZahl;
import sudoku.kern.feldmatrix.ZahlenListe;
import sudoku.kern.info.FeldInfo;
import sudoku.knacker.Ergebnis;
import sudoku.knacker.Knacker.VersuchsEbenen;
import sudoku.logik.Kasten;
import sudoku.logik.KastenIndex;
import sudoku.schwer.Analysator;

/**
 * @author Hendrick
 * Ist der Erzeuger für ein vollständig mit Vorgaben gefülltes Sudoku
 */
class Generator1 extends Analysator{
	protected  static boolean istSystemOut(){
		return false;
	}
	protected  static boolean istSystemOutAlles(){
		return false;
	}
	
	private  int nLaufMax = 0;
    
	private  void loescheVorgaben(ZahlenListe gesetzte) throws Exc{
		for(FeldNummerMitZahl feldNummerMitZahl: gesetzte){
			this.gibFeldmatrix().setzeVorgabe(feldNummerMitZahl.gibFeldNummer(), 0);
		}
	}

	/**
	 * Füllt eine - unabhängige! - Gruppe
	 * @param felder Indizees der zufällig zu füllenden 9 Felder
	 * @throws Exc 
	 */
	private  void fuelleGruppe(FeldNummerListe felder ) throws Exc{
		FeldNummerListe freie = new FeldNummerListe();
		freie.addAll(felder);
		
    	for (int i = 1; i < 9; i++) {
    		int indexInFelder = setze(freie, i);
    		freie.remove(indexInFelder);
		}
    	this.gibFeldmatrix().setzeVorgabe(freie.get(0), 9);
	}

	/**
	 * Setzt Zahl in ein zufällig ausgewähltes Feld
	 * @param felder In (zufällig) eines der Felder soll der Eintrag rein
	 * @param zahl soll gesetzt werden auf eines der Felder
	 * @return Index in Felder: Das Feld, das den Eintrag erhielt
	 * @throws Exc Bei falschen Parametern
	 */
	private  int setze(FeldNummerListe felder, int zahl) throws Exc{
    	int iFeld = Zufall.gib (felder.size());
    	this.gibFeldmatrix().setzeVorgabe(felder.get(iFeld), zahl);
    	return iFeld;
	}
	
	/**
	 * Setzt in das Feld eine zufällig ausgewählte mögliche Zahl
	 * @param feldNummer
	 * @return gesetzte Zahl oder null wenn ein Feld ohne Mögliche erwischt wurde.
	 * @throws Exc
	 */
	private  Integer setzeEinFeld(FeldNummer feldNummer) throws Exc{
    	FeldInfo feldInfo =  this.gibFeldmatrix().gibFeldInfo(feldNummer);
    	ArrayList<Integer> moegliche = feldInfo.gibMoegliche();
    	if (moegliche.isEmpty()){
    		return null;
    	}
    	int iZahl = Zufall.gib(moegliche.size());
    	int zahl = moegliche.get(iZahl);
    	this.gibFeldmatrix().setzeVorgabe(feldNummer, zahl);
    	return new Integer(zahl);
	}
	
    /**
     * Setzt in die Felder eine zufällig ausgewählte Vorgabe
     * @param felder
     * @return Gesetzte Felder mit ihrer Zahl jeweils
     * @throws Exc
     */
    private   ZahlenListe fuelleFelder(FeldNummerListe felder) throws Exc{
    	 ZahlenListe gesetzte = new ZahlenListe();
    	for(FeldNummer feldNummer: felder){
    		Integer zahl = setzeEinFeld(feldNummer);
    		if (zahl != null){
				FeldNummerMitZahl gesetzt = new FeldNummerMitZahl(feldNummer, zahl);
				gesetzte.add(gesetzt);
    		}
		}
    	return gesetzte;
    }
	
    /**
     * Setzt in anzahl zufällig ausgewählte Felder eine Vorgabe
    * @param felder
     * @param anzahl
     * @return Gesetzte Felder mit ihrer Zahl jeweils
     * @throws Exc
     */
    private   ZahlenListe fuelleFelder(FeldNummerListe felder, int anzahl) throws Exc{
		FeldNummerListe setzFelder = Zufall.gibAuswahlFelder(felder, anzahl, null);
		ZahlenListe gesetzte = fuelleFelder(setzFelder);
    	return gesetzte;
    }
	
	/**
	 * Die drei unabhängigen Kästen zufällig auswählen.
 	 * Jeden dieser 3 Kästen zufällig füllen (hier gibt es keine Probleme). 
	 * @return Indizees der Felder, die gesetzt wurden
	 * @throws Exc 
	 */
	private  FeldNummerListe fuelle3StartKaesten() throws Exc{
		FeldNummerListe gesetzte = new FeldNummerListe();
		
		// Die drei unabhängigen Kästen zufällig auswählen
        UnabhaengigeKasten kastengruppen = new UnabhaengigeKasten();
        int iGruppe = Zufall.gib(kastengruppen.size());
        ArrayList<KastenIndex> kastengruppe = kastengruppen.get(iGruppe);
       
        // Jeden dieser 3 Kästen zufällig füllen (hier gibt es keine Probleme) 
        for (int iKasten = 0; iKasten < kastengruppe.size(); iKasten++) {
        	KastenIndex kastenIndex = kastengruppe.get(iKasten);
        	FeldNummerListe kastenFelder = Kasten.gibKastenFeldNummern(kastenIndex);
        	// Die Felder dieser Indizees jetzt mit 1 bis 9 zufällig füllen
			fuelleGruppe(kastenFelder);
			gesetzte.addAll(kastenFelder);
		}
		if (istSystemOut() && istSystemOutAlles()){
			System.out.println(String.format("fülle3StartKaesten(): iGruppe=%d", iGruppe));
		}
        return gesetzte;
	}

	/**
	 * Knacker kommt zum Ende mit Versuchen
	 * @param freie
	 * @return
	 * @throws Exc
	 */
	private  Ergebnis fuelleMitKnackerVersuch() throws Exc
	{
		Ergebnis ergebnis = this.gibKnacker().loese(VersuchsEbenen.EINE, "Löse mit Versuch");
		if (ergebnis.gibArt() == Ergebnis.Art.FERTIG){
			this.gibFeldmatrix().wandleEintraegeZuVorgaben();
		}
		if (istSystemOut() && istSystemOutAlles()){
				System.out.println(String.format("fuelleMitKnackerVersuch() : %s", ergebnis));
		}
		return ergebnis;
	}

	/**
	 * Alle Klaren zu Vorgaben setzen
	 */
	private  void fuelleKlare(){
		try {
			this.gibFeldmatrix().setzeEintraegeAufKlare(this.gibKlugheit(), true, false);
			this.gibFeldmatrix().wandleEintraegeZuVorgaben();
		} catch (Exc e) {
			// ist hier völlig unerwartet
			e.printStackTrace();
		}
	}
	
	/**
	 * Füllt soweit auf bis Knacker (später) in der Lage ist,
	 *  mit Versuchen zum Ende zu kommen
	 * @param freie
	 * @return
	 * @throws Exc
	 */
	private  Ergebnis fuelleOhneKnackerVersuch(FeldNummerListe freie) throws Exc
	{
		Ergebnis ergebnis = null;
		int nZahlen = 8;
		int nFuellen;
		for (nFuellen=1; nFuellen < 100; nFuellen++) {
	        ZahlenListe gesetzte = fuelleFelder(freie, nZahlen);
        	ergebnis = this.gibKnacker().loese(VersuchsEbenen.KEINE, String.format("%d", nFuellen));
			if (ergebnis.gibArt() == Ergebnis.Art.PROBLEM){
				loescheVorgaben( gesetzte);
			}
			else {
				this.gibFeldmatrix().wandleEintraegeZuVorgaben();
				break;
			}
		}
		if ((nFuellen) > nLaufMax){
			nLaufMax = nFuellen;
		}
		if (istSystemOut() && istSystemOutAlles()){
			System.out.println(String.format("fuelleOhneKnackerVersuch() nZahlen=%d nLäufe=%d nLaufMax=%d: %s", 
					nZahlen, nFuellen, nLaufMax, ergebnis));
		}
		return ergebnis;
	}

	/**
	 * Soviel Bestimmtheit herstellen, dass der Knacker sehr wahrscheinlich (!)löst.
	 */
	private  boolean schaffeBestimmtheit(){
		int nZahlen = 5;
		ZahlenListe neueVorgaben = new ZahlenListe();
		FeldNummerListe startFelder = this.gibFeldmatrix().gibFreieFelder();
		FeldNummerListe probierFelder = new FeldNummerListe();
		probierFelder.addAll(startFelder);
		int nZahl = 0;
		int nLauf = 0;
		if (istSystemOut() && istSystemOutAlles()){
			System.out.println(String.format("schaffeBestimmtheit(): nFreie=%d nZahlen=%d", probierFelder.size(), nZahlen));
		}
		while(true){
			nLauf++;
			try {
				ZahlenListe gesetzte = fuelleFelder(probierFelder, 1);
			    if (gesetzte.isEmpty()){
					if (istSystemOut() && istSystemOutAlles()){
						System.out.println("schaffeBestimmtheit(): Die Probier-Felder sind alle: Erneuter Versuch");
					}
					return false;
//					loescheVorgaben(neueVorgaben);
//					neueVorgaben = new ArrayList<>();
//					probierFelder.addAll(startFelder);					
//			    	break;
			    } else
			    {
					probierFelder.remove(gesetzte.get(0).gibFeldNummer());
		        	Ergebnis ergebnis = this.gibKnacker().loese(VersuchsEbenen.KEINE, "schaffeBestimmtheit");
					if (ergebnis.gibArt() == Ergebnis.Art.PROBLEM){
						loescheVorgaben(gesetzte);
					}
					else {
						this.gibFeldmatrix().wandleEintraegeZuVorgaben();
						neueVorgaben.add(gesetzte.get(0));
						nZahl++;
						if (nZahl == nZahlen){
							break;
						}
					}
			    } // if (! gesetzte.isEmpty
			} catch (Exc e) {
				e.printStackTrace();
				if (istSystemOut() && istSystemOutAlles()){
					System.out.println(String.format("schaffeBestimmtheit() : Exc=%s", e.getMessage()));
				}
				return false;
			}
		} //while
		if (istSystemOut() && istSystemOutAlles()){
			System.out.println(String.format("schaffeBestimmtheit() : nZahlen=%d nLäufe=%d", 
					nZahlen, nLauf));
		}
		return true;
	}

	/**
	 * Füllt feldmatrix vollständig mit Vorgaben eines neuen Sudoku
	 * @param neuTyp
	 * @throws Exc 
	 */
	protected boolean fuelleSudoku(FeldNummerListe alle) throws Exc{
		FeldNummerListe freie = new FeldNummerListe();
		freie.addAll(alle);
		
		this.gibKlugheit().setzeExtrem(true);
		this.gibFeldmatrix().setzeMoegliche(this.gibKlugheit(), false);
		// a) 3 unabhängige Kästen füllen
		{
			FeldNummerListe belegte = fuelle3StartKaesten(); 
	        freie.removeAll(belegte);
		}
		this.gibFeldmatrix().setzeMoegliche(this.gibKlugheit(), false);
		
        // b) Zufällige Felder befüllen bis FERTIG 
		// Hört sich gefährlich an, ist es aber nicht:
		for (int nFuellen = 1; true; nFuellen++) {
			if (istSystemOut() && istSystemOutAlles()){
				System.out.println(String.format("Generator1.fuelleSudoku() nFuellen=%d", nFuellen));
			}
			
	    	// Weiter um n Zahlen auffüllen: Knacker kontrolliert ohne Versuch
			Ergebnis ergebnis = fuelleOhneKnackerVersuch(freie);
			if (ergebnis.gibArt() == Ergebnis.Art.FERTIG){
				break;
			}

			fuelleKlare();
			
			// Soviel Bestimmtheit herstellen, dass der Knacker sehr wahrscheinlich löst 
			boolean geschafft = schaffeBestimmtheit();
			if (! geschafft){
				return false;
			}
			
			// Knacker löst mit EINER Versuchsebene (wegen Schnelligkeit)
			ergebnis = fuelleMitKnackerVersuch();
			if (ergebnis.gibArt() == Ergebnis.Art.FERTIG){
				break;
			}
			
			if (nFuellen > 10000){
				Exc.endlosSchleife();
			}
			
		} // for endlos 
		return true;
	}
}
