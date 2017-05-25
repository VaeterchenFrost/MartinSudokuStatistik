package sudoku.kern.animator;

import sudoku.kern.feldmatrix.FeldNummer;

/**
 * @author heroe Es wird hier davaon ausgegangen, dass eine Verschiebung um
 *         einen Kasten stattfindet.
 */
public class Animator_Verschiebe1Kasten {
	/**
	 * @param feldNummer
	 * @return Zeile und Spalte der feldNummer im Bereich von 1 bis 9
	 */
	protected FeldNummer garantiereBereich(FeldNummer feldNummer, int nummerMax) {
		int spalte = garantiereBereich(feldNummer.gibSpalte(), nummerMax);
		int zeile = garantiereBereich(feldNummer.gibZeile(), nummerMax);
		return new FeldNummer(spalte, zeile);
	}

	/**
	 * @param nummer
	 * @return
	 */
	private int garantiereBereich(int nummer, int nummerMax) {
		if (nummer > nummerMax) {
			nummer -= nummerMax;
		}
		if (nummer <= 0) {
			nummer += nummerMax;
		}
		return nummer;
	}

}
