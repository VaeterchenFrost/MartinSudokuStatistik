package sudoku.logik;

import java.util.ArrayList;

import sudoku.ArrayListInt;
import sudoku.kern.exception.Exc;
import sudoku.kern.feldmatrix.FeldListe;
import sudoku.kern.feldmatrix.FeldNummerListe;
import sudoku.logik.Kasten.LinienListe;

class Logik_Kasten1 extends LogikKastenN {
	public Logik_Kasten1(ArrayList<Kasten> kaesten) {
		super(kaesten);
	}

	@Override
	public Logik_ID gibLogikID() {
		return Logik_ID.KASTEN1;
	}

	@Override
	public String gibKurzName() {
		char c = Kasten.gibTypZeichen(Gruppe.Typ.KASTEN);
		String s = String.format("%c1", c);
		return s;
	}

	@Override
	public String gibName() {
		return "Kasten 1";
	}

	@Override
	public String[] gibWo() {
		return new String[] { "Im Kasten bezogen auf 1 Nachbar-Kasten" };
	}

	@Override
	public String[] gibSituationAbstrakt() {
		return new String[] { "Auf 1 gemeinsamen Linie ist im Nachbar-Kasten 1 Zahl festgelegt." };
	}

	@Override
	public String[] gibSituation() {
		return new String[] { "Auf 1 gemeinsamen Linie (Spalte bzw. Zeile) ist im Nachbar-Kasten 1 Zahl festgelegt." };
	}

	@Override
	public String gibErgebnis() {
		return "Im Kasten wird die Zahl auf dieser 1 Linie aus den m�glichen dieser Felder gel�scht.";
	}

	@Override
	public double gibKontrollZeit1() {
		return 4;
	}

	/**
	 * Wenn hier im Kasten eine m�gliche Zahl auf mehreren Spalten / Zeilen
	 * steht: Bezug auf die m�glichen Zahlen jeweils eines Nachbar-Kastens:
	 * Erkenne m�gliche Zahl auf nur einer seiner Zeile bzw. Spalte: Im Kasten
	 * wird diese Zahl hier gel�scht.
	 * 
	 * @return null oder die gel�schten Zahlen mit ihren Feldern und ...
	 * @throws Exc
	 */
	private KastenErgebnis beachteNachbarn(Kasten kasten, boolean istSpalte, int zahl) throws Exc {
		ArrayList<LinienListe> linienDerNachbarn = kasten.gibNachbarLinienArray(istSpalte);
		LinienListe meineLinien = kasten.gibMeineLinien(istSpalte);

		// Mit allen (beiden) Nachbarn
		for (int iNachbar = 0; iNachbar < linienDerNachbarn.size(); iNachbar++) {
			LinienListe nachbarLinien = linienDerNachbarn.get(iNachbar);

			// Wenn die Zahl bei mir auf 2 oder 3 Linien verteilt ist
			ArrayListInt iMeineLinienDerZahl = Kasten.gibLinienDerZahl(zahl, meineLinien);
			int nMeineLinien = iMeineLinienDerZahl.size();
			if (nMeineLinien > 1) {
				// Suche Nachbar-Solo-Linie
				ArrayListInt iNachbarLinienDerZahl = Kasten.gibLinienDerZahl(zahl, nachbarLinien);
				if (1 == iNachbarLinienDerZahl.size()) {
					// Dann ist diese Zahl auf meiner Linie verboten
					int iLinieVerboten = iNachbarLinienDerZahl.get(0);
					FeldListe linie = meineLinien.get(iLinieVerboten);
					FeldListe kannLoeschenIn = linie.gibFelderDerMoeglichenZahl(zahl);
					// In jeder Logik, die m�gliche Zahlen l�scht, kann der Fall
					// eintreten,
					// dass dies Feld nach dem L�schen nur noch eine m�gliche
					// Zahl hat.
					// Aber diese m�gliche Solo-Zahl muss schon wirklich eine
					// andere Logik erkennen!

					if (!kannLoeschenIn.isEmpty()) {
						// Es gibt L�schZahlen: Diese Logik war aktiv
						FeldNummerListe nachbarUrsacheFelder = new FeldNummerListe(nachbarLinien.get(iLinieVerboten));

						KastenErgebnis ergebnis1 = new KastenErgebnis(istSpalte, zahl,
								new FeldNummerListe(kannLoeschenIn), nachbarUrsacheFelder);
						return ergebnis1;
					}
				}
			}
		} // for (int iNachbar

		return null;
	}

	@Override
	protected KastenErgebnis laufen(Kasten kasten) throws Exc {
		for (int zahl = 1; zahl < 10; zahl++) {
			// Spalten-Kontrolle
			KastenErgebnis ergebnisSpalten = beachteNachbarn(kasten, true, zahl);
			if (ergebnisSpalten != null) {
				return ergebnisSpalten;
			}
			// Zeilen-Kontrolle
			KastenErgebnis ergebnisZeilen = beachteNachbarn(kasten, false, zahl);
			if (ergebnisZeilen != null) {
				return ergebnisZeilen;
			}
		}
		return null;
	}

}
