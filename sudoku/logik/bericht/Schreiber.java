package sudoku.logik.bericht;

import sudoku.kern.feldmatrix.FeldNummerMitZahl;
import sudoku.kern.feldmatrix.Problem;
import sudoku.kern.feldmatrix.ZahlenListe;
import sudoku.logik.Klugheit;
import sudoku.logik.Logik_ID;

public class Schreiber {
	private static boolean istSystemOut = false;

	public static boolean istSystemOut() {
		return istSystemOut;
	}

	private BerichtLogik bericht;

	public Schreiber() {
		bericht = new BerichtLogik();
	}

	public void addDurchlauf(int durchlauf) {
		bericht.add(new BE_Durchlauf(durchlauf));
	}

	public void addEnde(Problem problem, FeldNummerMitZahl eintrag) {
		bericht.add(new BE_Ende(problem, eintrag));
	}

	// /**
	// * Schreibt die Logik, die weder einen Eintrag noch L�schzahlen gefunden
	// hat
	// */
	// public void addLogik(Logik logik, GruppenLaeufeListe gruppenlaeufeListe){
	// bericht.add(new BE_Logik(logik, gruppenlaeufeListe, null, null));
	// }
	//
	// /**
	// * Schreibt die Logik, die einen Eintrag gefunden hat
	// */
	// public void addLogik(Logik logik, GruppenLaeufeListe gruppenlaeufeListe,
	// FeldNummerMitZahl eintrag) {
	// bericht.add(new BE_Logik(logik, gruppenlaeufeListe, eintrag, null));
	// }
	//
	// /**
	// * Schreibt die Logik, die loeschZahlen gefunden hat
	// */
	// public void addLogik(Logik logik, GruppenLaeufeListe gruppenlaeufeListe,
	// ZahlenListe loeschZahlen) {
	// bericht.add(new BE_Logik(logik, gruppenlaeufeListe, null, loeschZahlen));
	// }
	//
	/**
	 * Schreibt die Logik, die sowohl einen Eintrag als auch loeschZahlen
	 * gefunden hat
	 */
	public void addLogik(Logik_ID logik, GruppenLaeufeListe gruppenlaeufeListe, FeldNummerMitZahl eintrag,
			ZahlenListe loeschZahlen) {
		bericht.add(new BE_Logik(logik, gruppenlaeufeListe, eintrag, loeschZahlen));
	}

	public void addStart(Klugheit klugheit, int nFreieFelder, int nFreieZeilen, int nFreieSpalten, int nFreieKaesten) {
		bericht.add(new BE_Start(klugheit, nFreieFelder, nFreieZeilen, nFreieSpalten, nFreieKaesten));
	}

	public BerichtLogik gibBericht() {
		return bericht;
	}

	public void systemOut() {
		if (istSystemOut) {
			bericht.systemOut();
		}
		// System.out.println("komprimiert
		// -------------------------------------------");
		// BerichtLogik berichtKomprimiert = bericht.gibKomprimiert();
		// berichtKomprimiert.systemOut();
	}
}
