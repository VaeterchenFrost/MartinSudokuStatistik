package sudoku.logik.bericht;

import java.util.ArrayList;

import sudoku.logik.Gruppe;

public class GruppenLaeufeListe {
	private ArrayList<GruppenLaeufe> gruppenLaeufeListe;

	public GruppenLaeufeListe(Gruppe.Typ gruppenTyp) {
		gruppenLaeufeListe = new ArrayList<>();
		gruppenLaeufeListe.add(new GruppenLaeufe(gruppenTyp, 0));
	}

	public int gibLaeufeAnzahlSumme() {
		int summe = 0;
		for (GruppenLaeufe gruppenLaeufe : gruppenLaeufeListe) {
			summe += gruppenLaeufe.gibAnzahlLaeufe();
		}
		return summe;
	}

	/**
	 * @param gruppenTyp
	 *            Wenn der gruppenTyp dem letzten eingetragenen entspricht, wird
	 *            der letzte Eintrag um 1 erh�ht. Falls nicht wird ein neuer
	 *            Eintrag erstellt.
	 */
	public void add(Gruppe.Typ gruppenTyp) {
		GruppenLaeufe letzterLauf = gruppenLaeufeListe.get(gruppenLaeufeListe.size() - 1);
		Gruppe.Typ letzterTyp = letzterLauf.gruppenTyp;

		if (letzterTyp == gruppenTyp) {
			letzterLauf.plus1();
		} else {
			GruppenLaeufe neuerLauf = new GruppenLaeufe(gruppenTyp, 1);
			gruppenLaeufeListe.add(neuerLauf);
		}
	}

	public Gruppe.Typ gibLetztenTyp() {
		if (gruppenLaeufeListe.isEmpty()) {
			return null;
		}
		return gruppenLaeufeListe.get(gruppenLaeufeListe.size() - 1).gruppenTyp;
	}

	private String gibInhaltKurz() {
		String s = "";
		for (int iLaeufe = 0; iLaeufe < gruppenLaeufeListe.size(); iLaeufe++) {
			GruppenLaeufe gruppenLaeufe = gruppenLaeufeListe.get(iLaeufe);
			s += " ";
			s += gruppenLaeufe.gibTextKurz();
		}
		return "[" + s + "]";
	}

	public String gibKurzText() {
		String s = "L�ufe: " + gibInhaltKurz();
		return s;
	}

	@Override
	public String toString() {
		String s = "GruppenLaeufeListe " + gibInhaltKurz();
		return s;
	}

}
