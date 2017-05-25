package sudoku.kern.feldmatrix;

public interface ProtokollSchreiber {
	/**
	 * protokolliert das Setzen eines Eintrags in einem Feld.
	 * 
	 * @param feldAlt
	 * @param feldNeu
	 */
	public void protokolliere(FeldNummer feldNummer, Eintrag alt, Eintrag neu);
}
