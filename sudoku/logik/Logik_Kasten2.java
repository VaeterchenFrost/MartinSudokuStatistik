package sudoku.logik;

import java.util.ArrayList;

import sudoku.ArrayListInt;
import sudoku.kern.exception.Exc;
import sudoku.kern.feldmatrix.FeldListe;
import sudoku.kern.feldmatrix.FeldNummerListe;
import sudoku.logik.Kasten.LinienListe;

class Logik_Kasten2 extends LogikKastenN {
	public Logik_Kasten2(ArrayList<Kasten> kaesten) {
		super(kaesten);
	}

	/**
	 * @param istSpalte
	 * @return null oder Ergibnis der aktiven Logik
	 * @throws Exc
	 */
	private KastenErgebnis beachteNachbarn(Kasten kasten, boolean istSpalte) throws Exc {
		ArrayList<LinienListe> linienDerNachbarn = kasten.gibNachbarLinienArray(istSpalte);
		LinienListe meineLinien = kasten.gibMeineLinien(istSpalte);

		// Jede Zahl wird kontrolliert
		for (int basisZahl = 1; basisZahl < 10; basisZahl++) {
			// F�r jeden der beiden Nachbarn (0 und 1) dessen Linien
			// bereitstellen
			LinienListe nachbar0Linien = linienDerNachbarn.get(0);
			LinienListe nachbar1Linien = linienDerNachbarn.get(1);

			// Die Indizees der Linien der beiden Nachbarn ermitteln, die Zahl
			// als M�gliche enthalten
			ArrayListInt iLinien0DerZahl = Kasten.gibLinienDerZahl(basisZahl, nachbar0Linien);
			ArrayListInt iLinien1DerZahl = Kasten.gibLinienDerZahl(basisZahl, nachbar1Linien);

			boolean gleichePaarLinien = false;
			boolean istPaarLinie0 = (2 == iLinien0DerZahl.size());
			boolean istPaarLinie1 = (2 == iLinien1DerZahl.size());

			if (istPaarLinie0 & istPaarLinie1) {
				// Sind die Indizees der Linien gleich?
				gleichePaarLinien = iLinien0DerZahl.equals(iLinien1DerZahl);
			}

			if (gleichePaarLinien) {
				// Dann ist diese Zahl auf meinen beiden Linien verboten

				// F�r jede der beiden Linien das separate Ergebnis erstellen
				ArrayList<KastenErgebnis> ergebnisTemp = new ArrayList<>();
				for (int iLinieDerZahl = 0; iLinieDerZahl < iLinien0DerZahl.size(); iLinieDerZahl++) {
					// Linien-Index dieser einen Linie mit der Zahl
					int iLinie = iLinien0DerZahl.get(iLinieDerZahl);
					// Meine Felder auf dieser Linie
					FeldListe linie = meineLinien.get(iLinie);
					// Meine Felder auf dieser Linie, in denen die (m�gliche)
					// Zahl gel�scht wird.
					FeldListe kannLoeschenIn = linie.gibFelderDerMoeglichenZahl(basisZahl);

					// Alle Felder des Nachbarn 0 auf dieser Linie
					FeldNummerListe nachbarUrsacheFelder = new FeldNummerListe(nachbar0Linien.get(iLinie));
					// Alle Felder des Nachbarn 1 auf dieser Linie
					FeldNummerListe nachbar1UrsacheFelder = new FeldNummerListe(nachbar1Linien.get(iLinie));
					nachbarUrsacheFelder.addAll(nachbar1UrsacheFelder);

					KastenErgebnis ergebnis1 = new KastenErgebnis(istSpalte, basisZahl,
							new FeldNummerListe(kannLoeschenIn), nachbarUrsacheFelder);
					ergebnisTemp.add(ergebnis1);
				} // for (int iLinie

				// F�r jede Linie muss ein Ergebnis vorliegen
				if (ergebnisTemp.size() != 2) {
					throw Exc.falscheAnzahl(ergebnisTemp.size(), 2);
				}

				KastenErgebnis ergebnis0 = ergebnisTemp.get(0);
				KastenErgebnis ergebnis1 = ergebnisTemp.get(1);

				// Wenn in einer meiner beiden Linien eine m�gliche Zahl
				// gel�scht wurde war diese Logik aktiv:
				boolean gibtEsLoeschFelder = (!ergebnis0.gibLoeschFelder().isEmpty())
						| (!ergebnis1.gibLoeschFelder().isEmpty());
				if (gibtEsLoeschFelder) {
					// Logik war aktiv: Aus zwei Ergebnissen mach ein Ergebnis
					ergebnis0.addloeschFelder(ergebnis1.gibLoeschFelder());
					ergebnis0.addUrsacheFelder(ergebnis1.gibUrsacheFelder());

					return ergebnis0;
				}
			} // if (gleichePaarLinien){
		} // for (int zahl

		return null;
	}

	@Override
	public String gibErgebnis() {
		return "Im Kasten wird die Zahl auf diesen 2 Linien aus den m�glichen dieser Felder gel�scht.";
	}

	@Override
	public double gibKontrollZeit1() {
		return 6;
	}

	@Override
	public String gibKurzName() {
		char c = Kasten.gibTypZeichen(Gruppe.Typ.KASTEN);
		String s = String.format("%c2", c);
		return s;
	}

	@Override
	public Logik_ID gibLogikID() {
		return Logik_ID.KASTEN2;
	}

	@Override
	public String gibName() {
		return "Kasten 2";
	}

	@Override
	public String[] gibSituation() {
		return new String[] {
				"Auf 2 gemeinsamen Linien (Spalte bzw. Zeile) sind in den 2 Nachbar-K�sten eine Zahl festgelegt." };
	}

	@Override
	public String[] gibSituationAbstrakt() {
		return new String[] { "Auf 2 gemeinsamen Linien sind in den 2 Nachbar-K�sten eine Zahl festgelegt." };
	}

	@Override
	public String[] gibWo() {
		return new String[] { "Im Kasten bezogen auf 2 Nachbark�sten waagerecht bzw. senkrecht" };
	}

	@Override
	protected KastenErgebnis laufen(Kasten kasten) throws Exc {
		// Spalten-Kontrolle
		KastenErgebnis ergebnisSpalten = beachteNachbarn(kasten, true);
		if (ergebnisSpalten != null) {
			return ergebnisSpalten;
		}
		// Zeilen-Kontrolle
		KastenErgebnis ergebnisZeilen = beachteNachbarn(kasten, false);
		if (ergebnisZeilen != null) {
			return ergebnisZeilen;
		}

		return null;
	}

}
