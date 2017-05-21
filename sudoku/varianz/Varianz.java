package sudoku.varianz;

import java.util.ArrayList;

import sudoku.kern.exception.Exc;
import sudoku.kern.exception.SudokuFertig;
import sudoku.kern.feldmatrix.FeldNummer;
import sudoku.kern.feldmatrix.FeldNummerListe;
import sudoku.kern.feldmatrix.FeldNummerMitZahl;
import sudoku.kern.info.FeldInfo;
import sudoku.kern.protokoll.ProtokollMarkierer;
import sudoku.logik.Klugheit;
import sudoku.logik.SudokuLogik;
import sudoku.logik.SudokuLogik.SetzeMoeglicheErgebnis;

public class Varianz {
//	 		Und wieder besser mit awt: (http://www.willemer.de/informatik/java/awtdia.htm)
//	static private int nMaxVarianten = 100;
	static private boolean istSystemOut = false;

	static public Loesungen gibLoesungen(SudokuLogik sudoku, ProtokollMarkierer protokollMarkierer, int maxAnzahl) throws Exc  {
		if (istSystemOut){
			System.out.println("Varianz.gibLoesungen()");
			ArrayList<String> kopftexte = Loesung.gibKopftexte();
			for(String s: kopftexte)	System.out.println(s);
		}
		
		Klugheit klugheit = new Klugheit(true);
		Loesungen loesungen= new Loesungen();
		try {
			sucheLoesungen(sudoku, klugheit, protokollMarkierer, new VersuchStarts(), loesungen, maxAnzahl);
		} catch (SudokuFertig e) {
			// Das sollte hier nie kommen wegen loesungen != null!
			e.printStackTrace();
		}

		return loesungen;
	}

	static public boolean loese(SudokuLogik sudoku, ProtokollMarkierer protokollMarkierer) throws Exc  {
		Klugheit klugheit = new Klugheit(true);
		try {
			sucheLoesungen(sudoku, klugheit, protokollMarkierer, new VersuchStarts(), null, 0);
			return false;
		} catch (SudokuFertig e) {
			return true;
		}
	}

	// --------------------------------------------------------------------------------------------------------------
	/**
	 * @param sudoku
	 * @param klugheit
	 * @param protokollMarkierer
	 * @param erfolgteVersuchStarts
	 * @param loesungen Bei loesungen==null wird die erste Lösung per Exception "SudokuFertig" gemeldet.
	 * 					Das sudoku enthält dann diese Lösung. 
	 * @param maxAnzahl Ist nur relevant bei loesungen != null: 
	 * 					Ist die maximale Anzahl Lösungen, die gewünscht werden.	 
	 * @throws Exc
	 * @throws SudokuFertig 
	 */
	static private void sucheLoesungen(SudokuLogik sudoku, Klugheit klugheit, 
			ProtokollMarkierer protokollMarkierer,
			VersuchStarts erfolgteVersuchStarts, Loesungen loesungen, int maxAnzahl) throws Exc, SudokuFertig  {
		/*
	  	  * 1. sudoku.setzeEintraegeAufKlareAlle(); 
	  	  * 1. a) fertig => n=1;
	  	  * 1. b) Problem => unlösbar
	  	  * 1. c) unfertig => 2.
	  	  * 2. Mithilfe Protokoll und den Ebenen stückweise vorgehen:
	  	  * 	Alles abbrechbar. Mit allen freien Feldern, mit allen Möglichen: Mögliche Zahl setzen +  sudoku.setzeEintraegeAufKlareAlle()
	  	  * 	Mit ev. Verzweigungen.
	  	  */

		// ======================================= Startzustand vermerken
		int markierungStart = protokollMarkierer.markierungSetzen();

		try{
			// ======================================= Klare treiben
			while (true) {
				SetzeMoeglicheErgebnis ergebnis = sudoku.setzeMoegliche(klugheit, false);
				if (ergebnis.gibProblem() != null) {
					// Das Sudoku ist versaut: Für diesen Zustand gibt es keine Lösung
					protokollMarkierer.markierungAnsteuern(markierungStart);
					return; // =======================>
				}

				if (ergebnis.gibEintrag() != null) {
					sudoku.setzeEintrag(ergebnis.gibEintrag());
				}
				
				if ( ! sudoku.istUnFertig() ) {
					// Fertig => Lösung melden oder eintragen 
					if (loesungen == null){
						throw new SudokuFertig();
					}
					Loesung loesung = new Loesung(erfolgteVersuchStarts, sudoku.gibFeldInfos());
					loesungen.add(loesung);
					if(istSystemOut){
						System.out.println(loesung.gibText());
					}
					protokollMarkierer.markierungAnsteuern(markierungStart);
					return; // =======================>
				}

				if (ergebnis.gibEintrag() == null) {
					// Für diesen Zustand gibt es keine logisch eindeutige Lösung (mit der Klugheit)
					protokollMarkierer.markierungAnsteuern(markierungStart);
					break; // =======================> unten weiter
				}
				
			}
	
			// ======================================= Weiter versuchen
			
			FeldNummerListe freieFelder = sudoku.gibFreieFelder();
			FeldNummer feldNummer = freieFelder.get(0);
			sudoku.setzeMoegliche(klugheit, false);
			ArrayList<Integer> moegliche = sudoku.gibFeldInfo(feldNummer).gibMoegliche();
	
			for (int VersuchNr = 0; VersuchNr < moegliche.size(); VersuchNr++) {
				// Protokollmarkierung setzen
				int markierungStartVersuch = protokollMarkierer.markierungSetzen();
	
				// Mögliche Zahl setzen
				sudoku.setzeMoegliche(klugheit, false);
				int zahl = moegliche.get(VersuchNr);
				sudoku.setzeEintrag(new FeldNummerMitZahl(feldNummer, zahl));
				
				// VersuchStart vermerken
				FeldInfo feldInfo = sudoku.gibFeldInfo(feldNummer);
				VersuchStart versuchStart = new VersuchStart(VersuchNr + 1, feldInfo);
				VersuchStarts versuchStarts2 = new VersuchStarts(erfolgteVersuchStarts, versuchStart);
				
				// Weiter suchen ob so wohl eine Lösung möglich ist
				sucheLoesungen(sudoku, klugheit, protokollMarkierer, versuchStarts2, loesungen, maxAnzahl);
				if (loesungen != null){
					if (loesungen.gibAnzahl() >= maxAnzahl){
						// Abbruch wegen sinnlos vieler Lösungen
						break;
					}
				}
				// Protokollmarkierung rücksetzen
				protokollMarkierer.markierungAnsteuern(markierungStartVersuch);
			} // for (Integer m: moegliche
		} catch(Exc e){
			// Unbedingt Protokoll zurückstellen
			protokollMarkierer.markierungAnsteuern(markierungStart);
			throw(e);
		}
		protokollMarkierer.markierungAnsteuern(markierungStart);
	}
}
