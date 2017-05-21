package sudoku.kern.animator;

import sudoku.kern.feldmatrix.FeldNummer;

/**
 * @author heroe
 * Animator für die unkritische Bewegung der Felder innerhalb des Sudokus.
 * Z.B. Drehen links bzw. rechts, Spiegeln u.s.w.
 */
public interface Animator {
	/**
	 * @param feldNummer (alte) Nummer des Feldes
	 * @param nummerMax größte Nummer einer Spalte bzw. Zeile eines Feldes
	 * @return Die für die Animation nötige neue Nummer des Feldes 
	 */
	public FeldNummer gibFeldNummer(FeldNummer feldNummer, int nummerMax);
	
	/**
	 * @return Name der Animation. Z.B. "Sudoku rechts drehen"
	 */
	public String gibName();
}
