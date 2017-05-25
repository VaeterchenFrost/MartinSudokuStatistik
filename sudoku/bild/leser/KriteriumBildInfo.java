package sudoku.bild.leser;

/**
 * @author heroe Kriterium f�r die Bestimmung einer Zahl
 */
interface KriteriumBildInfo {
	/**
	 * @param zahlBildInfo
	 *            Auf dieser Basis wird der Erf�llungsgrad dieses Kriteriums
	 *            bestimmt
	 * @param istSystemOut
	 * @return Erf�llungsgrad in Prozent
	 */
	public float gibErfuellungsGrad(ZahlBildInfo zahlBildInfo, boolean istSystemOut);

	public String gibName();
}
