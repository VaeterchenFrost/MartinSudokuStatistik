package sudoku.bild.leser;

/**
 * @author heroe
 * Kriterium für die Bestimmung einer Zahl
 */
interface KriteriumBildInfo {
	/**
	 * @param zahlBildInfo Auf dieser Basis wird der Erfüllungsgrad dieses Kriteriums bestimmt
	 * @param istSystemOut
	 * @return Erfüllungsgrad in Prozent
	 */
	public float gibErfuellungsGrad(ZahlBildInfo zahlBildInfo, boolean istSystemOut);
	
	public String gibName();
}
