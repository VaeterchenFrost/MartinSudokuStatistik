package sudoku.kern.animator;

import sudoku.kern.feldmatrix.FeldNummer;

/**
 * @author heroe Animator f�r die unkritische Bewegung der Felder innerhalb des
 *         Sudokus. Z.B. Drehen links bzw. rechts, Spiegeln u.s.w.
 */
public interface Animator {
	/**
	 * @param feldNummer
	 *            (alte) Nummer des Feldes
	 * @param nummerMax
	 *            gr��te Nummer einer Spalte bzw. Zeile eines Feldes
	 * @return Die f�r die Animation n�tige neue Nummer des Feldes
	 */
	public FeldNummer gibFeldNummer(FeldNummer feldNummer, int nummerMax);

	/**
	 * @return Name der Animation. Z.B. "Sudoku rechts drehen"
	 */
	public String gibName();
}
