package sudoku.bild;

import java.awt.Point;

import sudoku.Paar;

/**
 * @author heroe Die Striche, deren Au�enkanten ein Rechteck benennen
 */
class StricheKreuz {

	/**
	 * Transformiert alle Indizees so, dass sie sich auf den neuen
	 * Indize-Ursprung beziehen.
	 * 
	 * @param stricheArray
	 * @param neuerUrsprung
	 */
	static private void transformiereIndizees(StrichListe[] stricheArray, int neuerUrsprung) {
		if (stricheArray != null) {
			for (int i = 0; i < stricheArray.length; i++) {
				StrichListe strichListe = stricheArray[i];
				strichListe.transformiereIndizees(neuerUrsprung);
			}
		}
	}

	/**
	 * @param striche
	 * @return Index-Bereich, den die Striche insgesamt benennen: Pair.Key den
	 *         1. Index (linken bzw. oberen Index) Pair.Value den letzten Index
	 *         (rechten bzw. unteren Index)
	 */
	static private Paar<Integer, Integer> gibBereich(StrichListe[] striche) {
		StrichListe anfang = striche[0];
		int ersterIndex = anfang.get(0).von.gibVonIndex() + 1;

		StrichListe ende = striche[striche.length - 1];

		Strich letzterStrich = ende.get(ende.size() - 1);
		int letzterIndex = letzterStrich.nach.gibVonIndex();
		Paar<Integer, Integer> pair = new Paar<Integer, Integer>(ersterIndex, letzterIndex);

		return pair;
	}

	// =======================================================
	/**
	 * Senkrechte Sudoku-Striche
	 */
	public final StrichListe[] spaltenStriche;
	/**
	 * Waagerechte Sudoku-Striche
	 */
	public final StrichListe[] zeilenStriche;

	/**
	 * Es wird davon ausgegangen, dass die Arrays entweder als null kommen oder
	 * ansonsten mit Feldelementen die ungleich null sind
	 * 
	 * @param spaltenStriche
	 * @param zeilenStriche
	 */
	public StricheKreuz(StrichListe[] spaltenStriche, StrichListe[] zeilenStriche) {
		this.spaltenStriche = spaltenStriche;
		this.zeilenStriche = zeilenStriche;
	}

	/**
	 * @return true wenn f�r Spalte und Zeile jeweils mindestens eine
	 *         StrichListe existiert
	 */
	public boolean istOK() {
		if (spaltenStriche == null) {
			return false;
		}
		if (zeilenStriche == null) {
			return false;
		}

		if (spaltenStriche.length == 0) {
			return false;
		}
		if (zeilenStriche.length == 0) {
			return false;
		}
		return true;
	}

	/**
	 * @return true wenn f�r Spalte mindestens eine StrichListe existiert
	 */
	public boolean istSpaltenOk() {
		if (spaltenStriche != null) {
			if (spaltenStriche.length > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return true wenn f�r Zeile mindestens eine StrichListe existiert
	 */
	public boolean istZeilenOk() {
		if (zeilenStriche != null) {
			if (zeilenStriche.length > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return true wenn f�r Spalte oder Zeile jeweils mindestens eine
	 *         StrichListe existiert
	 */
	public boolean istEineRichtungBekannt() {
		if (istSpaltenOk()) {
			return true;
		}

		if (istZeilenOk()) {
			return true;
		}
		return false;
	}

	/**
	 * @return Index-Bereich, den die Striche insgesamt benennen: Pair.Key den
	 *         1. Index (linken bzw. oberen Index) Pair.Value den letzten Index
	 *         (rechten bzw. unteren Index)
	 */
	public Paar<Integer, Integer> gibBereichSpalten() {
		Paar<Integer, Integer> bereich = gibBereich(this.spaltenStriche);
		return bereich;
	}

	/**
	 * @return Index-Bereich, den die Striche insgesamt benennen: Pair.Key den
	 *         1. Index (linken bzw. oberen Index) Pair.Value den letzten Index
	 *         (rechten bzw. unteren Index)
	 */
	public Paar<Integer, Integer> gibBereichZeilen() {
		Paar<Integer, Integer> bereich = gibBereich(this.zeilenStriche);
		return bereich;
	}

	/**
	 * Transformiert alle Indizees so, dass sie sich auf den neuen
	 * Indize-Ursprung beziehen.
	 * 
	 * @param neuerUrsprung
	 */
	public void transformiereIndizees(Point neuerUrsprung) {
		transformiereIndizees(spaltenStriche, neuerUrsprung.x);
		transformiereIndizees(zeilenStriche, neuerUrsprung.y);
	}
}
